package backend.component.cpu.service.impl;

import backend.component.cpu.dto.response.CpuResponse;
import backend.component.cpu.entity.CentralProcessor;
import backend.component.cpu.repo.CpuRepository;
import backend.component.cpu.service.CpuService;
import backend.recommendation.repository.CpuRatingRepository;
import backend.user.User;
import backend.user.UserActivity;
import backend.user.UserActivityRepository;
import backend.user.UserRepository;
import backend.utility.ClientLevel;
import backend.utility.Recommender;
import backend.utility.Result;
import backend.utility.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CpuServiceImpl implements CpuService {

    private static final Logger logger = LogManager.getLogger(CpuServiceImpl.class);

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CpuRatingRepository cpuRatingRepository;

    @Autowired
    private CpuRepository cpuRepository;

    @Override
    public Object findByProperties(String name, String chipset, String manufacturer, String socket, Integer cores, Pageable pageable) {
        List<CpuResponse> responseList = new ArrayList<>();

        logger.info("Start find CPU with param");
        Page<CentralProcessor> cpuPages = cpuRepository.findAll((Specification<CentralProcessor>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(chipset)) {
                p = cb.and(p, cb.like(root.get("chipset"), "%" + chipset + "%"));
            }
            if (Objects.nonNull(manufacturer)) {
                p = cb.and(p, cb.like(root.get("manufacturer"), "%" + manufacturer + "%"));
            }
            if (Objects.nonNull(socket)) {
                p = cb.and(p, cb.like(root.get("socket"), "%" + socket + "%"));
            }
            if (Objects.nonNull(cores)) {
                p = cb.and(p, cb.equal(root.get("cores"), cores));
            }
            if (!StringUtils.isEmpty(name)) {
                p = cb.and(p, cb.like(root.get("fullname"), "%" + name + "%"));
            }
            cq.orderBy(cb.desc(root.get("fullname")), cb.asc(root.get("id")));
            return p;
        }, pageable);
        logger.info("Finish find CPU with param");

        logger.info("Create DTO response");
        for (CentralProcessor cpu : cpuPages) {
            responseList.add(new CpuResponse(cpu));
        }

        Page<CpuResponse> responsePage = new PageImpl<>(responseList, pageable, cpuPages.getTotalElements());
        return responsePage;
    }

    @Override
    public Object findById(String id, Integer userId) {
        logger.info("Start find CPU by ID [" + id + "]");
        CentralProcessor cpu = cpuRepository.findByID(id);
        logger.info("CPU by ID [" + id + "] found");
        try {
            User user = userRepository.findByID(userId);
            if (user != null) {
                userActivityRepository.save(new UserActivity(user, "view", cpu.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), cpu.getId());
                cpuRepository.update(id);
            }
            cpu.setCpuRating(cpuRatingRepository.findById(user.getId() + "-" + id));
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }
        logger.info("Create DTO response");
        CpuResponse response = new CpuResponse(cpu);
        return response;
    }

    @Override
    public Object getRecommendItemForUser(Integer userId) {
        List<CpuResponse> centralProcessors = new ArrayList<>();
        try {
            Result result = Utility.returnReccomendedItem(null, "cpu", userId);
            centralProcessors = doRecommender(result);
            Page<CpuResponse> cpuPage = new PageImpl<>(centralProcessors);
            return cpuPage;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            Page<CpuResponse> cpuPage = new PageImpl<>(centralProcessors);
            return cpuPage;
        }
    }

    @Override
    public Object getRecommendItemForUserWithItemId(String id, Integer userId) {
        CentralProcessor cpu = cpuRepository.findByID(id);
        List<CpuResponse> centralProcessors = new ArrayList<>();
        System.out.println("User: " + userId);
        try {
            Result result = Utility.returnReccomendedItem(cpu.getId(), "cpu", userId);
            centralProcessors = doRecommender(result);
            Page<CpuResponse> cpuPage = new PageImpl<>(centralProcessors);
            return cpuPage;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            Page<CpuResponse> cpuPage = new PageImpl<>(centralProcessors);
            return cpuPage;
        }
    }

    private List<CpuResponse> doRecommender(Result result) {
        List<CpuResponse> recommendList = new ArrayList<>();
        for (int i = 0; i <10; ++i) {
            Recommender recommender = result.getResult().get(i);
            System.out.println(recommender.getItem() + " " + recommender.getScore());
            recommendList.add(new CpuResponse(cpuRepository.findByID(recommender.getItem())));
        }
        return recommendList;
    }
}
