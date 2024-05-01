package backend.component.mainboard.service.impl;


import backend.component.mainboard.controller.MainboardController;
import backend.component.mainboard.dto.response.MainboardResponse;
import backend.component.mainboard.entity.Mainboard;
import backend.component.mainboard.repo.MainboardRepository;
import backend.component.mainboard.service.MainboardService;
import backend.recommendation.repository.MainRatingRepository;
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
public class MainboardServiceImpl implements MainboardService {

    private static final Logger logger = LogManager.getLogger(MainboardController.class);

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MainboardRepository mainboardRepository;

    @Autowired
    private MainRatingRepository mainRatingRepository;

    @Override
    public Object findByProperties(String name, String chipset, String socket, String manufacturer, String sizeOfRam,
                                            String memorySlot, String formFactor, Pageable pageable) {
        List<MainboardResponse> responseList = new ArrayList<>();
        Page<Mainboard> mainboardPage = mainboardRepository.findAll((Specification<Mainboard>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (Objects.nonNull(chipset)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("chipset"), "%" + chipset + "%"));
            }
            if (Objects.nonNull(manufacturer)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("manufacturer"), "%" + manufacturer + "%"));
            }
            if (Objects.nonNull(socket)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("socket"), "%" + socket + "%"));
            }
            if (Objects.nonNull(sizeOfRam)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("sizeOfRam"), "%" + sizeOfRam + "%"));
            }
            if (Objects.nonNull(memorySlot)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("memorySlot"), memorySlot));
            }
            if (Objects.nonNull(formFactor)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("formFactor"), formFactor));
            }
            if (!StringUtils.isEmpty(name)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("fullname"), "%" + name + "%"));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("fullname")), criteriaBuilder.asc(root.get("id")));
            return predicate;
        }, pageable);

        for (Mainboard mainboard : mainboardPage) {
            responseList.add(new MainboardResponse(mainboard));
        }

        Page<MainboardResponse> mainboardResponsePage = new PageImpl<>(responseList, pageable, mainboardPage.getTotalElements());
        return mainboardResponsePage;
    }

    @Override
    public Object findById(String id, Integer userId) {
        Mainboard mainboard = mainboardRepository.findByID(id);
        try {
            User user = userRepository.findByID(userId);

            if (user != null) {
                userActivityRepository.save(new UserActivity(user, "view", mainboard.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), mainboard.getId());
                mainboardRepository.update(id);
            }
            mainboard.setMainboardRating(mainRatingRepository.findById(user.getId() + "-" + id));
            logger.info("Save success");
            MainboardResponse response = new MainboardResponse(mainboard);
            return response;

        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            MainboardResponse response = new MainboardResponse(mainboard);
            return response;
        }
    }

    @Override
    public Object getRecommendItemForUser(Integer userId) {
        List<Mainboard> mainboards = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(null, "mainboard", userId);
            mainboards = doRecommender(result);
            Page<Mainboard> mainboardPage = new PageImpl<>(mainboards);
            return mainboardPage;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            Page<Mainboard> mainboardPage = new PageImpl<>(mainboards);
            return mainboardPage;
        }
    }

    @Override
    public Object getRecommendItemForUserWithItemId(String id, Integer userId) {
        Mainboard mainboard = mainboardRepository.findByID(id);
        List<Mainboard> mainboards = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(mainboard.getId(), "mainboard", userId);
            mainboards = doRecommender(result);
            Page<Mainboard> mainboardPage = new PageImpl<>(mainboards);
            return mainboardPage;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            Page<Mainboard> mainboardPage = new PageImpl<>(mainboards);
            return mainboardPage;
        }
    }

    public List<Mainboard> doRecommender(Result result) {
        List<Mainboard> recommendList = new ArrayList<>();
        for (int i = 0; i <10; ++i) {
            Recommender recommender = result.getResult().get(i);
            System.out.println(recommender.getItem() + " " + recommender.getScore());
            recommendList.add(mainboardRepository.findByID(recommender.getItem()));
        }
        return recommendList;
    }
}
