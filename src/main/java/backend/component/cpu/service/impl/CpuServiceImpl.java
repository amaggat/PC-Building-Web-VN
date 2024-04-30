package backend.component.cpu.service.impl;

import backend.component.cpu.controller.CpuController;
import backend.component.cpu.dto.response.CentralProcessorResponse;
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

    private static final Logger logger = LogManager.getLogger(CpuController.class);

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CpuRatingRepository cpuRatingRepository;

    @Autowired
    private CpuRepository cpuRepository;

    @Override
    public Object findCpuByProperties(String name, String chipset, String manufacturer, String socket, Integer cores, Pageable pageable) {
        List<CentralProcessorResponse> responseList = new ArrayList<>();

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

        for (CentralProcessor cpu : cpuPages) {
            responseList.add(new CentralProcessorResponse(cpu));
        }

        Page<CentralProcessorResponse> responsePage = new PageImpl<>(responseList, pageable, cpuPages.getTotalElements());
        return responsePage;
    }

    @Override
    public Object recommendCpuForUser(Integer userId) {
        List<CentralProcessor> centralProcessors = new ArrayList<>();
        try {
            Result result = Utility.returnReccomendedItem(null, "cpu", userId);
            centralProcessors = doRecommender(result);
            Page<CentralProcessor> cpuPage = new PageImpl<>(centralProcessors);
            return cpuPage;
        } catch (Exception e) {
            Page<CentralProcessor> cpuPage = new PageImpl<>(centralProcessors);
            return cpuPage;
        }
    }

    @Override
    public Object findById(String id, Integer userId) {
        CentralProcessor cpu = cpuRepository.findByID(id);
        try {
            User user = userRepository.findByID(userId);
            if (user != null) {
                userActivityRepository.save(new UserActivity(user, "view", cpu.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), cpu.getId());
                cpuRepository.update(id);
            }
            cpu.setCpuRating(cpuRatingRepository.findById(user.getId() + "-" + id));
            logger.log(ClientLevel.CLIENT, "Success");
            return cpu;

        } catch (Exception e) {
            logger.log(ClientLevel.CLIENT, "Unsuccess");
            return cpu;
        }
    }

    @Override
    public Object recommendList(String id, Integer userId) {
        CentralProcessor cpu = cpuRepository.findByID(id);
        List<CentralProcessor> centralProcessors = new ArrayList<>();
        System.out.println("User: " + userId);
        try {
            Result result = Utility.returnReccomendedItem(cpu.getId(), "cpu", userId);
            centralProcessors = doRecommender(result);
            Page<CentralProcessor> cpuPage = new PageImpl<>(centralProcessors);
            return cpuPage;
        } catch (Exception e) {
            Page<CentralProcessor> cpuPage = new PageImpl<>(centralProcessors);
            return cpuPage;
        }
    }

    private List<CentralProcessor> doRecommender(Result result) {
        List<CentralProcessor> recommendList = new ArrayList<>();
        for (int i = 0; i <10; ++i) {
            Recommender recommender = result.getResult().get(i);
            System.out.println(recommender.getItem() + " " + recommender.getScore());
            recommendList.add(cpuRepository.findByID(recommender.getItem()));
        }
        return recommendList;
    }
}
