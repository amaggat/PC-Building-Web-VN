package backend.component.gpu.service.impl;

import backend.component.gpu.dto.response.GpuResponse;
import backend.component.gpu.entity.GraphicProcessor;
import backend.component.gpu.repo.GpuRepository;
import backend.component.gpu.service.GpuService;
import backend.recommendation.repository.GpuRatingRepository;
import backend.security.utils.JwtUtils;
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
public class GpuServiceImpl implements GpuService {


    private static final Logger logger = LogManager.getLogger(GpuServiceImpl.class);

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GpuRepository gpuRepository;

    @Autowired
    private GpuRatingRepository gpuRatingRepository;

    @Override
    public Object findByProperties(String name, String chipset, String manufacturer, Integer VRam, Pageable pageable) {
        List<GpuResponse> responseList = new ArrayList<>();
        Page<GraphicProcessor> gpuPage = gpuRepository.findAll((Specification<GraphicProcessor>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(chipset)) {
                p = cb.and(p, cb.like(root.get("chipset"), "%" + chipset + "%"));
            }
            if (Objects.nonNull(manufacturer)) {
                p = cb.and(p, cb.like(root.get("manufacturer"), "%" + manufacturer + "%"));
            }
            if (Objects.nonNull(VRam)) {
                p = cb.and(p, cb.equal(root.get("VRam"), VRam));
            }
            if (!StringUtils.isEmpty(name)) {
                p = cb.and(p, cb.like(root.get("fullname"), "%" + name + "%"));
            }
            cq.orderBy(cb.desc(root.get("fullname")), cb.asc(root.get("id")));
            return p;
        }, pageable);

        for(GraphicProcessor gpu : gpuPage) {
            responseList.add(new GpuResponse(gpu));
        }

        Page<GpuResponse> responsePage = new PageImpl<>(responseList, pageable, gpuPage.getTotalElements());
        return responsePage;
    }

    @Override
    public Object findById(String id, Integer userId) {
        GraphicProcessor gpu = gpuRepository.findByID(id);

        try {
            User user = userRepository.findByID(userId);
            if (user != null) {
                userActivityRepository.save(new UserActivity(user, "view", gpu.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), gpu.getId());
                gpuRepository.update(id);
            }
            gpu.setGpuRating(gpuRatingRepository.findById(user.getId() + "-" + id));
            return gpu;

        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return gpu;
        }
    }

    @Override
    public Object getRecommendItemForUser(Integer userId) {
        List<GraphicProcessor> graphicProcessors = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(null, "gpu", userId);
            graphicProcessors = doRecommender(result);
            Page<GraphicProcessor> gpuPage = new PageImpl<>(graphicProcessors);
            return gpuPage;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            Page<GraphicProcessor> gpuPage = new PageImpl<>(graphicProcessors);
            return gpuPage;
        }
    }

    @Override
    public Object getRecommendItemForUserWithItemId(String id, Integer userId) {
        GraphicProcessor gpu = gpuRepository.findByID(id);
        List<GraphicProcessor> graphicProcessors = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(gpu.getId(), "gpu", userId);
            graphicProcessors = doRecommender(result);
            Page<GraphicProcessor> gpuPage = new PageImpl<>(graphicProcessors);
            return gpuPage;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            Page<GraphicProcessor> gpuPage = new PageImpl<>(graphicProcessors);
            return gpuPage;
        }
    }

    public List<GraphicProcessor> doRecommender(Result result) {
        List<GraphicProcessor> recommendList = new ArrayList<>();
        for (int i = 0; i <10; ++i) {
            Recommender recommender = result.getResult().get(i);
            logger.info(recommender.getItem() + " " + recommender.getScore());
            recommendList.add(gpuRepository.findByID(recommender.getItem()));
        }
        return recommendList;
    }

}
