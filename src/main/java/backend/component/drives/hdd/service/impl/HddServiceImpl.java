package backend.component.drives.hdd.service.impl;

import backend.component.drives.hdd.controller.HddController;
import backend.component.drives.hdd.dto.response.HddResponse;
import backend.component.drives.hdd.entity.HardDiskDrive;
import backend.component.drives.hdd.repo.HddRepository;
import backend.component.drives.hdd.service.HddService;
import backend.recommendation.repository.HddRatingRepository;
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
public class HddServiceImpl implements HddService {

    private static final Logger logger = LogManager.getLogger(HddServiceImpl.class);

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HddRepository hddRepository;

    @Autowired
    private HddRatingRepository hddRatingRepository;

    @Override
    public ResponseEntity<Object> findByProperties(String name, String chipset, String manufacturer, String size, Pageable pageable) {
        List<HddResponse> responseList = new ArrayList<>();

        logger.info("Start find HDD with param");
        Page<HardDiskDrive> hddPage = hddRepository.findAll((Specification<HardDiskDrive>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(chipset)) {
                p = cb.and(p, cb.like(root.get("chipset"), "%" + chipset + "%"));
            }
            if (Objects.nonNull(manufacturer)) {
                p = cb.and(p, cb.like(root.get("manufacturer"), "%" + manufacturer + "%"));
            }
            if (Objects.nonNull(size)) {
                p = cb.and(p, cb.like(root.get("storage"), "%" + size + "%"));
            }
            if (!StringUtils.isEmpty(name)) {
                p = cb.and(p, cb.like(root.get("fullname"), "%" + name + "%"));
            }
            cq.orderBy(cb.desc(root.get("fullname")), cb.asc(root.get("id")));
            return p;
        }, pageable);
        logger.info("Finish find HDD with param, size [" + hddPage.getTotalElements() + "]");

        logger.info("Create DTO response");
        for (HardDiskDrive hardDiskDrive : hddPage) {
            responseList.add(new HddResponse(hardDiskDrive));
        }

        Page<HddResponse> responsePage = new PageImpl<>(responseList, pageable, hddPage.getTotalElements());
        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> findById(String id, Integer userId) {
        logger.info("Start find HDD by ID [" + id + "]");
        HardDiskDrive hdd = hddRepository.findByID(id);
        if(hdd == null) {
            logger.error("HDD by ID [" + id + "] not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("HDD by ID [" + id + "] found");

        try {
            User user = userRepository.findByID(userId);
            if (user != null) {
                logger.info("Add user [" + user.getId() + "] activities");
                userActivityRepository.save(new UserActivity(user, "view", hdd.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), hdd.getId());
                hddRepository.update(id);
                logger.info("Save success");
                hdd.setHddRating(hddRatingRepository.findById(user.getId() + "-" + id));
            }

        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        HddResponse response = new HddResponse(hdd);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getRecommendItemForUser(Integer userId) {
        logger.info("Find recommend HDD for User ID [" + userId + "]");
        List<HddResponse> hardDiskDrives = new ArrayList<>();
        try {
            logger.info("Start get recommend item");
            Result result = Utility.returnReccomendedItem(null, "hdd", userId);
            logger.info("Recommend item received");
            hardDiskDrives = doRecommender(result);

        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        Page<HddResponse> hddPage = new PageImpl<>(hardDiskDrives);
        return new ResponseEntity<>(hddPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId) {
        logger.info("Find recommend HDD with [" + id + "] for User ID [" + userId + "]");
        HardDiskDrive hdd = hddRepository.findByID(id);
        if(hdd == null) {
            logger.error("HDD by ID [" + id + "] not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("HDD by ID [" + id + "] found");

        List<HddResponse> hardDiskDrives = new ArrayList<>();
        try {
            logger.info("Start get recommend item");
            Result result = Utility.returnReccomendedItem(hdd.getId(), "hdd", userId);
            logger.info("Recommend item received");
            hardDiskDrives = doRecommender(result);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        Page<HddResponse> hddPage = new PageImpl<>(hardDiskDrives);
        return new ResponseEntity<>(hddPage, HttpStatus.OK);
    }

    public List<HddResponse> doRecommender(Result result) {
        List<HddResponse> recommendList = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            Recommender recommender = result.getResult().get(i);
            logger.info(recommender.getItem() + " score: " + recommender.getScore());
            recommendList.add(new HddResponse(hddRepository.findByID(recommender.getItem())));
        }
        return recommendList;
    }
}
