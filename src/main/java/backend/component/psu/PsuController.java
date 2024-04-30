package backend.component.psu;


import backend.recommendation.repository.PsuRatingRepository;
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
public class PsuController {

    private static final Logger logger = LogManager.getLogger(PsuController.class);

    @Autowired
    private JwtUtils jwtUtil;
    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;
    private final PsuRepository psuRepository;
    private final PsuRatingRepository psuRatingRepository;

    public PsuController(UserActivityRepository userActivityRepository, UserRepository userRepository, PsuRepository psuRepository, PsuRatingRepository psuRatingRepository) {
        this.userActivityRepository = userActivityRepository;
        this.userRepository = userRepository;
        this.psuRepository = psuRepository;
        this.psuRatingRepository = psuRatingRepository;
    }

    @GetMapping("/api/psu")
    public Page<PowerSupplyUnit> list(@RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "chipset", required = false) String chipset,
                                      @RequestParam(name = "manufacturer", required = false) String manufacturer,
                                      @RequestParam(name = "standard_80", required = false) String standard_80,
                                      @RequestParam(name = "power", required = false) Integer power,
                                      Pageable pageable) {
        Page<PowerSupplyUnit> psu = psuRepository.findAll((Specification<PowerSupplyUnit>) (root, cq, cb) -> {
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
        return psu;
    }

    @GetMapping("/api/psu/{id}")
    public PowerSupplyUnit SearchById(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        PowerSupplyUnit psu = psuRepository.findByID(id);

        try {
            User user = userRepository.findByID(userId);
            if (user != null) {
                userActivityRepository.save(new UserActivity(user, "view", psu.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), psu.getId());
                psuRepository.update(id);
            }
            psu.setPsuRating(psuRatingRepository.findById(user.getId() + "-" + id));
            logger.log(ClientLevel.CLIENT, "Success");
            return psu;

        } catch (Exception e) {
            logger.log(ClientLevel.CLIENT, "Unsuccess");
            return psu;
        }
    }

    @GetMapping("/api/recommend/psu")
    public Page<PowerSupplyUnit> reccomendFront(@CookieValue(value = "userId", required = false) Integer userId) {
        List<PowerSupplyUnit> powerSupplyUnits = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(null, "psu", userId);
            powerSupplyUnits = doRecommender(result);
            Page<PowerSupplyUnit> psuPage = new PageImpl<>(powerSupplyUnits);
            return psuPage;
        } catch (Exception e) {
            Page<PowerSupplyUnit> psuPage = new PageImpl<>(powerSupplyUnits);
            return psuPage;
        }
    }

    @GetMapping("/api/recommend/psu/{id}")
    public Page<PowerSupplyUnit> recommendList(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        PowerSupplyUnit psu = psuRepository.findByID(id);
        List<PowerSupplyUnit> powerSupplyUnits = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(psu.getId(), "psu", userId);
            powerSupplyUnits = doRecommender(result);
            Page<PowerSupplyUnit> psuPage = new PageImpl<>(powerSupplyUnits);
            return psuPage;
        } catch (Exception e) {
            Page<PowerSupplyUnit> psuPage = new PageImpl<>(powerSupplyUnits);
            return psuPage;
        }
    }

    public List<PowerSupplyUnit> doRecommender(Result result) {
        List<PowerSupplyUnit> recommendList = new ArrayList<>();
        for (int i = 0; i <10; ++i) {
            Recommender recommender = result.getResult().get(i);
            System.out.println(recommender.getItem() + " " + recommender.getScore());
            recommendList.add(psuRepository.findByID(recommender.getItem()));
        }
        return recommendList;
    }

}
