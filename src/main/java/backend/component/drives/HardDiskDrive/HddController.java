package backend.component.drives.HardDiskDrive;


import backend.recommendation.repository.HddRatingRepository;
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
public class HddController {
    private static final Logger logger = LogManager.getLogger(HddController.class);

    @Autowired
    private JwtUtils jwtUtil;
    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;
    private final HddRepository hddRepository;
    private final HddRatingRepository hddRatingRepository;

    public HddController(UserActivityRepository userActivityRepository, UserRepository userRepository, HddRepository hddRepository, HddRatingRepository hddRatingRepository) {
        this.userActivityRepository = userActivityRepository;
        this.userRepository = userRepository;
        this.hddRepository = hddRepository;
        this.hddRatingRepository = hddRatingRepository;
    }

    @GetMapping("/api/hdd")
    public Page<HardDiskDrive> list(@RequestParam(name = "name", required = false) String name,
                                    @RequestParam(name = "chipset", required = false) String chipset,
                                    @RequestParam(name = "manufacturer", required = false) String manufacturer,
                                    @RequestParam(name = "storage", required = false) String size,
                                    Pageable pageable) {
        Page<HardDiskDrive> hdd = hddRepository.findAll((Specification<HardDiskDrive>) (root, cq, cb) -> {
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
        return hdd;
    }

    @GetMapping("/api/hdd/{id}")
    public HardDiskDrive SearchByID(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        HardDiskDrive hdd = hddRepository.findByID(id);

        try {
            User user = userRepository.findByID(userId);
            if (user != null) {
                userActivityRepository.save(new UserActivity(user, "view", hdd.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), hdd.getId());
                hddRepository.update(id);
            }
            hdd.setHddRating(hddRatingRepository.findById(user.getId() + "-" + id));
            logger.log(ClientLevel.CLIENT, "Success");
            return hdd;

        } catch (Exception e) {
            logger.log(ClientLevel.CLIENT, "Unsuccess");
            return hdd;
        }
    }

    @GetMapping("/api/recommend/hdd")
    public Page<HardDiskDrive> recommendFront(@CookieValue(value = "userId", required = false) Integer userId) {
        List<HardDiskDrive> hardDiskDrives = new ArrayList<>();
        try {
            Result result = Utility.returnReccomendedItem(null, "hdd", userId);
            hardDiskDrives = doRecommender(result);
            Page<HardDiskDrive> hddPage = new PageImpl<>(hardDiskDrives);
            return hddPage;
        } catch (Exception e) {
            Page<HardDiskDrive> hddPage = new PageImpl<>(hardDiskDrives);
            return hddPage;
        }
    }

    @GetMapping("/api/recommend/hdd/{id}")
    public Page<HardDiskDrive> recommendList(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        HardDiskDrive hdd = hddRepository.findByID(id);
        List<HardDiskDrive> hardDiskDrives = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(hdd.getId(), "hdd", userId);
            hardDiskDrives = doRecommender(result);
            Page<HardDiskDrive> hddPage = new PageImpl<>(hardDiskDrives);
            return hddPage;
        } catch (Exception e) {
            Page<HardDiskDrive> hddPage = new PageImpl<>(hardDiskDrives);
            return hddPage;
        }
    }

    public List<HardDiskDrive> doRecommender(Result result) {
        List<HardDiskDrive> recommendList = new ArrayList<>();
        for (int i = 0; i <10; ++i) {
            Recommender recommender = result.getResult().get(i);
            System.out.println(recommender.getItem() + " " + recommender.getScore());
            recommendList.add(hddRepository.findByID(recommender.getItem()));
        }
        return recommendList;
    }
}
