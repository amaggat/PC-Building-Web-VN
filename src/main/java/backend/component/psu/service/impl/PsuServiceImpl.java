package backend.component.psu.service.impl;

import backend.component.psu.dto.response.PsuResponse;
import backend.component.psu.entity.PowerSupplyUnit;
import backend.component.psu.repo.PsuRepository;
import backend.component.psu.service.PsuService;
import backend.recommendation.repository.PsuRatingRepository;
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
public class PsuServiceImpl implements PsuService {


    private static final Logger logger = LogManager.getLogger(PsuServiceImpl.class);

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PsuRepository psuRepository;

    @Autowired
    private PsuRatingRepository psuRatingRepository;

    @Value("${pcrs.recommend.item.no}")
    private Integer recNo;

    @Override
    public ResponseEntity<Object> findByProperties(String name, String chipset, String manufacturer, String standard_80, Integer power, Pageable pageable) {
        List<PsuResponse> responseList = new ArrayList<>();

        logger.info("Start find Mainboard with param");
        Page<PowerSupplyUnit> psuPage = psuRepository.findAll((Specification<PowerSupplyUnit>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(chipset)) {
                p = cb.and(p, cb.like(root.get("chipset"), "%" + chipset + "%"));
            }
            if (Objects.nonNull(manufacturer)) {
                p = cb.and(p, cb.like(root.get("manufacturer"), "%" + manufacturer + "%"));
            }
            if (Objects.nonNull(standard_80)) {
                p = cb.and(p, cb.like(root.get("standard_80"), "%" + standard_80 + "%"));
            }
            if (Objects.nonNull(power)) {
                p = cb.and(p, cb.equal(root.get("power"), power));
            }
            if (!StringUtils.isEmpty(name)) {
                p = cb.and(p, cb.like(root.get("fullname"), "%" + name + "%"));
            }
            cq.orderBy(cb.desc(root.get("fullname")), cb.asc(root.get("id")));
            return p;
        }, pageable);
        logger.info("Finish find Mainboard with param, size [" + psuPage.getTotalElements() + "]");

        logger.info("Create DTO response");
        for (PowerSupplyUnit psu : psuPage) {
            responseList.add(new PsuResponse(psu));
        }

        Page<PsuResponse> responsePage = new PageImpl<>(responseList, pageable, psuPage.getTotalElements());
        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> findById(String id, Integer userId) {
        logger.info("Start find PSU by ID [" + id + "]");
        PowerSupplyUnit psu = psuRepository.findByID(id);
        if(psu == null) {
            logger.error("PSU by ID [" + id + "] not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("PSU by ID [" + id + "] found");

        try {
            User user = userRepository.findByID(userId);
            if (user != null) {
                logger.info("Add user [" + user.getId() + "] activities");
                userActivityRepository.save(new UserActivity(user, "view", psu.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), psu.getId());
                psuRepository.update(id);
                logger.info("Save success");
            }
            psu.setPsuRating(psuRatingRepository.findById(user.getId() + "-" + id));
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        PsuResponse response = new PsuResponse(psu);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getRecommendItemForUser(Integer userId) {
        logger.info("Find recommend PSU for User ID [" + userId + "]");
        List<PsuResponse> powerSupplyUnits = new ArrayList<>();

        try {
            logger.info("Start get recommend item");
            Result result = Utility.returnReccomendedItem(null, "psu", userId);
            logger.info("Recommend item received");
            powerSupplyUnits = doRecommender(result);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        Page<PsuResponse> psuPage = new PageImpl<>(powerSupplyUnits);
        return new ResponseEntity<>(psuPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId) {
        logger.info("Find recommend PSU with [" + id + "] for User ID [" + userId + "]");
        PowerSupplyUnit psu = psuRepository.findByID(id);
        if(psu == null) {
            logger.error("PSU by ID [" + id + "] not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("PSU by ID [" + id + "] found");


        List<PsuResponse> powerSupplyUnits = new ArrayList<>();
        try {
            logger.info("Start get recommend item");
            Result result = Utility.returnReccomendedItem(psu.getId(), "psu", userId);
            logger.info("Recommend item received");
            powerSupplyUnits = doRecommender(result);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        Page<PsuResponse> psuPage = new PageImpl<>(powerSupplyUnits);
        return new ResponseEntity<>(psuPage, HttpStatus.OK);
    }

    public List<PsuResponse> doRecommender(Result result) {
        List<PsuResponse> recommendList = new ArrayList<>();
        for (int i = 0; i < recNo; ++i) {
            Recommender recommender = result.getResult().get(i);
            logger.info(recommender.getItem() + " score: " + recommender.getScore());
            recommendList.add(new PsuResponse(psuRepository.findByID(recommender.getItem())));
        }
        return recommendList;
    }

}
