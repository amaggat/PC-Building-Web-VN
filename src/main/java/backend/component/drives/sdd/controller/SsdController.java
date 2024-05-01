package backend.component.drives.sdd.controller;

import backend.component.drives.sdd.entity.SolidStateDrive;
import backend.component.drives.sdd.repo.SsdRepository;
import backend.recommendation.repository.SsdRatingRepository;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class SsdController {

    private static final Logger logger = LogManager.getLogger(SsdController.class);

    @Autowired
    private JwtUtils jwtUtil;
    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;
    private final SsdRepository ssdRepository;
    private final SsdRatingRepository ssdRatingRepository;

    public SsdController(UserActivityRepository userActivityRepository, UserRepository userRepository, SsdRepository ssdRepository, SsdRatingRepository ssdRatingRepository) {
        this.userActivityRepository = userActivityRepository;
        this.userRepository = userRepository;
        this.ssdRepository = ssdRepository;
        this.ssdRatingRepository = ssdRatingRepository;
    }

    @GetMapping("/api/ssd")
    public Page<SolidStateDrive> list(@RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "chipset", required = false) String chipset,
                                      @RequestParam(name = "manufacturer", required = false) String manufacturer,
                                      @RequestParam(name = "storage", required = false) String storage,
                                      Pageable pageable) {
        Page<SolidStateDrive> ssd = ssdRepository.findAll((Specification<SolidStateDrive>) (root, cq, cb) -> {
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
        return ssd;
    }

    @GetMapping("/api/ssd/{id}")
    public SolidStateDrive SearchById(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        SolidStateDrive ssd = ssdRepository.findByID(id);

        try {
            User user = userRepository.findByID(userId);
            if (user != null) {
                userActivityRepository.save(new UserActivity(user, "view", ssd.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), ssd.getId());
                ssdRepository.update(id);
            }
            ssd.setSsdRating(ssdRatingRepository.findById(user.getId() + "-" + id));
            logger.log(ClientLevel.CLIENT, "Success");
            return ssd;

        } catch (Exception e) {
            logger.log(ClientLevel.CLIENT, "Unsuccess");
            return ssd;
        }
    }

    @GetMapping("/api/recommend/ssd")
    public Page<SolidStateDrive> recommendFront(@CookieValue(value = "userId", required = false) Integer userId) {
        List<SolidStateDrive> solidStateDrives = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(null, "ssd", userId);
            solidStateDrives = doRecommender(result);
            Page<SolidStateDrive> ssdPage = new PageImpl<>(solidStateDrives);
            return ssdPage;
        } catch (Exception e) {
            Page<SolidStateDrive> ssdPage = new PageImpl<>(solidStateDrives);
            return ssdPage;
        }
    }

    @GetMapping("/api/recommend/ssd/{id}")
    public Page<SolidStateDrive> recommendList(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        SolidStateDrive ssd = ssdRepository.findByID(id);
        List<SolidStateDrive> solidStateDrives = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(ssd.getId(), "ssd", userId);
            solidStateDrives = doRecommender(result);
            Page<SolidStateDrive> ssdPage = new PageImpl<>(solidStateDrives);
            return ssdPage;
        } catch (Exception e) {
            Page<SolidStateDrive> ssdPage = new PageImpl<>(solidStateDrives);
            return ssdPage;
        }
    }

    public List<SolidStateDrive> doRecommender(Result result) {
        List<SolidStateDrive> recommendList = new ArrayList<>();
        for (int i = 0; i <10; ++i) {
            Recommender recommender = result.getResult().get(i);
            System.out.println(recommender.getItem() + " " + recommender.getScore());
            recommendList.add(ssdRepository.findByID(recommender.getItem()));
        }
        return recommendList;
    }
}
