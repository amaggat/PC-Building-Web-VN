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
import backend.utility.Recommender;
import backend.utility.Result;
import backend.utility.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MainboardServiceImpl implements MainboardService {

    private static final Logger logger = LogManager.getLogger(MainboardServiceImpl.class);

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

    @Value("${pcrs.recommend.item.no}")
    private Integer recNo;

    @Override
    public ResponseEntity<Object> findByProperties(String name, String chipset, String socket, String manufacturer, String sizeOfRam,
                                                   String memorySlot, String formFactor, Pageable pageable) {
        List<MainboardResponse> responseList = new ArrayList<>();

        logger.info("Start find Mainboard with param");
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
        logger.info("Finish find Mainboard with param, size [" + mainboardPage.getTotalElements() + "]");

        logger.info("Create DTO response");
        for (Mainboard mainboard : mainboardPage) {
            responseList.add(new MainboardResponse(mainboard));
        }

        Page<MainboardResponse> mainboardResponsePage = new PageImpl<>(responseList, pageable, mainboardPage.getTotalElements());
        return new ResponseEntity<>(mainboardResponsePage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> findById(String id, Integer userId) {
        logger.info("Start find Mainboard by ID [" + id + "]");
        Mainboard mainboard = mainboardRepository.findByID(id);
        if(mainboard == null) {
            logger.error("Mainboard by ID [" + id + "] not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Mainboard by ID [" + id + "] found");
        try {
            User user = userRepository.findByID(userId);

            if (user != null) {
                logger.info("Add user [" + user.getId() + "] activities");
                userActivityRepository.save(new UserActivity(user, "view", mainboard.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), mainboard.getId());
                mainboardRepository.update(id);
                logger.info("Save success");
                mainboard.setMainboardRating(mainRatingRepository.findById(user.getId() + "-" + id));
            }

        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        MainboardResponse response = new MainboardResponse(mainboard);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getRecommendItemForUser(Integer userId) {
        logger.info("Find recommend mainboard for User ID [" + userId + "]");
        List<MainboardResponse> mainboards = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(null, "mainboard", userId);
            mainboards = doRecommender(result);
            logger.info("Recommend item received");
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        Page<MainboardResponse> mainboardPage = new PageImpl<>(mainboards);
        return new ResponseEntity<>(mainboardPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId) {
        logger.info("Find recommend mainboard with [" + id + "] for User ID [" + userId + "]");
        Mainboard mainboard = mainboardRepository.findByID(id);
        if(mainboard == null) {
            logger.error("GPU by ID [" + id + "] not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<MainboardResponse> mainboards = new ArrayList<>();
        try {
            logger.info("Start get recommend item");
            Result result = Utility.returnReccomendedItem(mainboard.getId(), "mainboard", userId);
            logger.info("Recommend item received");
            mainboards = doRecommender(result);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        Page<MainboardResponse> mainboardPage = new PageImpl<>(mainboards);
        return new ResponseEntity<>(mainboardPage, HttpStatus.OK);
    }

    public List<MainboardResponse> doRecommender(Result result) {
        List<MainboardResponse> recommendList = new ArrayList<>();
        for (int i = 0; i < recNo; ++i) {
            Recommender recommender = result.getResult().get(i);
            logger.info(recommender.getItem() + " score: " + recommender.getScore());
            recommendList.add(new MainboardResponse(mainboardRepository.findByID(recommender.getItem())));
        }
        return recommendList;
    }
}
