package backend.component.mainboard;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    @Autowired
    private JwtUtils jwtUtil;
    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;
    private final MainRepository mainRepository;
    private final MainRatingRepository mainRatingRepository;

    public MainController(UserActivityRepository userActivityRepository, UserRepository userRepository, MainRepository mainRepository, MainRatingRepository mainRatingRepository) {
        this.userActivityRepository = userActivityRepository;
        this.userRepository = userRepository;
        this.mainRepository = mainRepository;
        this.mainRatingRepository = mainRatingRepository;
    }

    @GetMapping("/api/mainboard")
    public Page<Mainboard> list(@RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "chipset", required = false) String chipset,
                                @RequestParam(name = "socket", required = false) String socket,
                                @RequestParam(name = "manufacturer", required = false) String manufacturer,
                                @RequestParam(name = "sizeofram", required = false) String sizeofram,
                                @RequestParam(name = "memory_slot", required = false) String memorySlot,
                                @RequestParam(name = "formfactor", required = false) String formfactor,
                                Pageable pageable) {
        Page<Mainboard> mainboard = mainRepository.findAll((Specification<Mainboard>) (root, criteriaQuery, criteriaBuilder) -> {
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
            if (Objects.nonNull(sizeofram)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("sizeofram"), "%" + sizeofram + "%"));
            }
            if (Objects.nonNull(memorySlot)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("memory_slot"), memorySlot));
            }
            if (Objects.nonNull(formfactor)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("formfactor"), formfactor));
            }
            if (!StringUtils.isEmpty(name)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("fullname"), "%" + name + "%"));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("fullname")), criteriaBuilder.asc(root.get("id")));
            return predicate;
        }, pageable);
        return mainboard;
    }

    @GetMapping("/api/mainboard/{id}")
    public Mainboard SearchByID(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        Mainboard mainboard = mainRepository.findByID(id);
        try {
            User user = userRepository.findByID(userId);

            if (user != null) {
                userActivityRepository.save(new UserActivity(user, "view", mainboard.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), mainboard.getId());
                mainRepository.update(id);
            }
            mainboard.setMainboardRating(mainRatingRepository.findById(user.getId() + "-" + id));
            logger.log(ClientLevel.CLIENT, "Success");
            return mainboard;

        } catch (Exception e) {
            logger.log(ClientLevel.CLIENT, "Unsuccess");
            return mainboard;
        }
    }

    @GetMapping("/api/recommend/mainboard")
    public Page<Mainboard> reccomendFront(@CookieValue(value = "userId", required = false) Integer userId) {
        List<Mainboard> mainboards = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(null, "mainboard", userId);
            mainboards = doRecommender(result);
            Page<Mainboard> mainboardPage = new PageImpl<>(mainboards);
            return mainboardPage;
        } catch (Exception e) {
            Page<Mainboard> mainboardPage = new PageImpl<>(mainboards);
            return mainboardPage;
        }
    }

    @GetMapping("/api/recommend/mainboard/{id}")
    public Page<Mainboard> recommendList(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        Mainboard mainboard = mainRepository.findByID(id);
        List<Mainboard> mainboards = new ArrayList<>();

        try {
            Result result = Utility.returnReccomendedItem(mainboard.getId(), "mainboard", userId);
            mainboards = doRecommender(result);
            Page<Mainboard> mainboardPage = new PageImpl<>(mainboards);
            return mainboardPage;
        } catch (Exception e) {
            Page<Mainboard> mainboardPage = new PageImpl<>(mainboards);
            return mainboardPage;
        }
    }

    public List<Mainboard> doRecommender(Result result) {
        List<Mainboard> recommendList = new ArrayList<>();
        for (int i = 0; i <10; ++i) {
            Recommender recommender = result.getResult().get(i);
            System.out.println(recommender.getItem() + " " + recommender.getScore());
            recommendList.add(mainRepository.findByID(recommender.getItem()));
        }
        return recommendList;
    }

}
