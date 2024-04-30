package backend.component.ram;


import backend.recommendation.repository.RamRatingRepository;
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
public class RamController {

    private static final Logger logger = LogManager.getLogger(RamController.class);

    @Autowired
    private JwtUtils jwtUtil;
    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;
    private final RamRepository ramRepository;
    private final RamRatingRepository ramRatingRepository;

    public RamController(UserActivityRepository userActivityRepository, UserRepository userRepository, RamRepository ramRepository, RamRatingRepository ramRatingRepository) {
        this.userActivityRepository = userActivityRepository;
        this.userRepository = userRepository;
        this.ramRepository = ramRepository;
        this.ramRatingRepository = ramRatingRepository;
    }

    @GetMapping("/api/ram")
    public Page<Ram> list(@RequestParam(name = "name", required = false) String name,
                          @RequestParam(name = "chipset", required = false) String chipset,
                          @RequestParam(name = "manufacturer", required = false) String manufacturer,
                          @RequestParam(name = "sizeOfRam", required = false) String sizeOfRam,
                          @RequestParam(name = "clockSpeed", required = false) Integer clockSpeed,
                          Pageable pageable) {
        Page<Ram> ramPage = ramRepository.findAll((Specification<Ram>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(chipset)) {
                p = cb.and(p, cb.like(root.get("chipset"), "%" + chipset + "%"));
            }
            if (Objects.nonNull(manufacturer)) {
                p = cb.and(p, cb.like(root.get("manufacturer"), "%" + manufacturer + "%"));
            }
            if (Objects.nonNull(sizeOfRam)) {
                p = cb.and(p, cb.like(root.get("sizeOfRam"), "%" + sizeOfRam + "%"));
            }
            if (Objects.nonNull(clockSpeed)) {
                p = cb.and(p, cb.equal(root.get("clockSpeed"), clockSpeed));
            }
            if (!StringUtils.isEmpty(name)) {
                p = cb.and(p, cb.like(root.get("fullname"), "%" + name + "%"));
            }
            cq.orderBy(cb.desc(root.get("fullname")), cb.asc(root.get("id")));
            return p;
        }, pageable);

        return ramPage;
    }

    @GetMapping("/api/ram/{RamID}")
    public Ram SearchByID(@PathVariable("RamID") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        Ram ram = ramRepository.findByID(id);
        try {
            User user = userRepository.findByID(userId);
            if (user != null) {
                userActivityRepository.save(new UserActivity(user, "view", ram.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), ram.getId());
                ramRepository.update(id);
            }
            ram.setRamRating(ramRatingRepository.findById(user.getId() + "-" + id));
            logger.log(ClientLevel.CLIENT, "Success");
            return ram;

        } catch (Exception e) {
            logger.log(ClientLevel.CLIENT, "Unsuccess");
            return ram;
        }
    }

    @GetMapping("/api/recommend/ram")
    public Page<Ram> reccomendFront(@CookieValue(value = "userId", required = false) Integer userId) {
        List<Ram> rams = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(null, "ram", userId);
            rams = doRecommender(result);
            Page<Ram> psuPage = new PageImpl<>(rams);
            return psuPage;
        } catch (Exception e) {
            Page<Ram> psuPage = new PageImpl<>(rams);
            return psuPage;
        }
    }

    @GetMapping("/api/recommend/ram/{id}")
    public Page<Ram> recommendList(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        Ram ram = ramRepository.findByID(id);
        List<Ram> rams = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(ram.getId(), "ram", userId);
            rams = doRecommender(result);
            Page<Ram> psuPage = new PageImpl<>(rams);
            return psuPage;
        } catch (Exception e) {
            Page<Ram> psuPage = new PageImpl<>(rams);
            return psuPage;
        }
    }

    public List<Ram> doRecommender(Result result) {
        List<Ram> recommendList = new ArrayList<>();
        for (int i = 0; i <10; ++i) {
            Recommender recommender = result.getResult().get(i);
            System.out.println(recommender.getItem() + " " + recommender.getScore());
            recommendList.add(ramRepository.findByID(recommender.getItem()));
        }
        return recommendList;
    }
}
