package backend.component.drives.sdd.service.impl;

import backend.component.drives.sdd.controller.SsdController;
import backend.component.drives.sdd.dto.response.SsdResponse;
import backend.component.drives.sdd.entity.SolidStateDrive;
import backend.component.drives.sdd.repo.SsdRepository;
import backend.component.drives.sdd.service.SsdService;
import backend.recommendation.repository.SsdRatingRepository;
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
public class SsdServiceImpl implements SsdService {

    private static final Logger logger = LogManager.getLogger(SsdController.class);

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SsdRepository ssdRepository;

    @Autowired
    private SsdRatingRepository ssdRatingRepository;

    @Override
    public ResponseEntity<Object> findByProperties(String name, String chipset, String manufacturer, String storage, Pageable pageable) {
        List<SsdResponse> responseList = new ArrayList<>();

        logger.info("Start find SSD with param");
        Page<SolidStateDrive> ssdPage = ssdRepository.findAll((Specification<SolidStateDrive>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(chipset)) {
                p = cb.and(p, cb.like(root.get("chipset"), "%" + chipset + "%"));
            }
            if (Objects.nonNull(manufacturer)) {
                p = cb.and(p, cb.like(root.get("manufacturer"), "%" + manufacturer + "%"));
            }
            if (Objects.nonNull(storage)) {
                p = cb.and(p, cb.like(root.get("storage"), "%" + storage + "%"));
            }
            if (!StringUtils.isEmpty(name)) {
                p = cb.and(p, cb.like(root.get("fullname"), "%" + name + "%"));
            }
            cq.orderBy(cb.desc(root.get("fullname")), cb.asc(root.get("id")));
            return p;
        }, pageable);
        logger.info("Finish find SSD with param, size [" + ssdPage.getTotalElements() + "]");


        logger.info("Create DTO response");
        for (SolidStateDrive solidStateDrive : ssdPage) {
            responseList.add(new SsdResponse(solidStateDrive));
        }

        Page<SsdResponse> responsePage = new PageImpl<>(responseList, pageable, ssdPage.getTotalElements());
        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> findById(String id, Integer userId) {
        logger.info("Start find SSD by ID [" + id + "]");
        SolidStateDrive ssd = ssdRepository.findByID(id);
        if(ssd == null) {
            logger.error("SSD by ID [" + id + "] not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("SSD by ID [" + id + "] found");

        try {
            User user = userRepository.findByID(userId);
            if (user != null) {
                logger.info("Add user [" + user.getId() + "] activities");
                userActivityRepository.save(new UserActivity(user, "view", ssd.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), ssd.getId());
                ssdRepository.update(id);
                logger.info("Save success");
                ssd.setSsdRating(ssdRatingRepository.findById(user.getId() + "-" + id));
            }

        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        SsdResponse response = new SsdResponse(ssd);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getRecommendItemForUser(Integer userId) {
        logger.info("Find recommend SSD for User ID [" + userId + "]");
        List<SsdResponse> solidStateDrives = new ArrayList<>();

        try {
            logger.info("Start get recommend item");
            Result result = Utility.returnReccomendedItem(null, "ssd", userId);
            logger.info("Recommend item received");
            solidStateDrives = doRecommender(result);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        Page<SsdResponse> hddPage = new PageImpl<>(solidStateDrives);
        return new ResponseEntity<>(hddPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId) {
        logger.info("Find recommend SSD with [" + id + "] for User ID [" + userId + "]");
        SolidStateDrive ssd = ssdRepository.findByID(id);
        if(ssd == null) {
            logger.error("SSD by ID [" + id + "] not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("SSD by ID [" + id + "] found");

        List<SsdResponse> solidStateDrives = new ArrayList<>();
        try {
            logger.info("Start get recommend item");
            Result result = Utility.returnReccomendedItem(ssd.getId(), "ssd", userId);
            logger.info("Recommend item received");
            solidStateDrives = doRecommender(result);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        Page<SsdResponse> ssdPage = new PageImpl<>(solidStateDrives);
        return new ResponseEntity<>(ssdPage, HttpStatus.OK);
    }

    public List<SsdResponse> doRecommender(Result result) {
        List<SsdResponse> recommendList = new ArrayList<>();
        for (int i = 0; i <10; ++i) {
            Recommender recommender = result.getResult().get(i);
            logger.info(recommender.getItem() + " score: " + recommender.getScore());
            recommendList.add(new SsdResponse(ssdRepository.findByID(recommender.getItem())));
        }
        return recommendList;
    }
}
