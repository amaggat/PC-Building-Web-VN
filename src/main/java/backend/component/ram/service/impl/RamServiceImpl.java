package backend.component.ram.service.impl;

import backend.component.ram.dto.response.RamResponse;
import backend.component.ram.entity.Ram;
import backend.component.ram.repo.RamRepository;
import backend.component.ram.service.RamService;
import backend.recommendation.repository.RamRatingRepository;
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
public class RamServiceImpl implements RamService {


    private static final Logger logger = LogManager.getLogger(RamServiceImpl.class);

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RamRepository ramRepository;

    @Autowired
    private RamRatingRepository ramRatingRepository;

    @Value("${pcrs.recommend.item.no}")
    private Integer recNo;

    @Override
    public ResponseEntity<Object> findByProperties(String name, String chipset, String manufacturer, String sizeOfRam, Integer clockSpeed, Pageable pageable) {
        List<RamResponse> responseList = new ArrayList<>();

        logger.info("Start find RAM with param");
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
        logger.info("Finish find RAM with param, size [" + ramPage.getTotalElements() + "]");

        logger.info("Create DTO response");
        for (Ram ram : ramPage) {
            responseList.add(new RamResponse(ram));
        }

        Page<RamResponse> responsePage = new PageImpl<>(responseList, pageable, ramPage.getTotalElements());
        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> findById(String id, Integer userId) {
        logger.info("Start find RAM by ID [" + id + "]");
        Ram ram = ramRepository.findByID(id);
        if(ram == null) {
            logger.error("RAM by ID [" + id + "] not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("RAM by ID [" + id + "] found");

        try {
            User user = userRepository.findByID(userId);
            if (user != null) {
                logger.info("Add user [" + user.getId() + "] activities");
                userActivityRepository.save(new UserActivity(user, "view", ram.getId()));
                Utility.sendActivity(Utility.URL, "view", user.getId(), ram.getId());
                ramRepository.update(id);
                logger.info("Save success");
            }
            ram.setRamRating(ramRatingRepository.findById(user.getId() + "-" + id));

        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        RamResponse response = new RamResponse(ram);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getRecommendItemForUser(Integer userId) {
        logger.info("Find recommend RAM for User ID [" + userId + "]");
        List<RamResponse> rams = new ArrayList<>();

        try {
            logger.info("Start get recommend item");
            Result result = Utility.returnReccomendedItem(null, "ram", userId);
            logger.info("Recommend item received");
            rams = doRecommender(result);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        Page<RamResponse> ramPage = new PageImpl<>(rams);
        return new ResponseEntity<>(ramPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId) {
        logger.info("Find recommend RAM with [" + id + "] for User ID [" + userId + "]");
        Ram ram = ramRepository.findByID(id);
        if(ram == null) {
            logger.error("RAM by ID [" + id + "] not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("RAM by ID [" + id + "] found");

        List<RamResponse> rams = new ArrayList<>();
        try {
            logger.info("Start get recommend item");
            Result result = Utility.returnReccomendedItem(ram.getId(), "ram", userId);
            logger.info("Recommend item received");
            rams = doRecommender(result);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
        }

        logger.info("Create DTO response");
        Page<RamResponse> ramPage = new PageImpl<>(rams);
        return new ResponseEntity<>(ramPage, HttpStatus.OK);
    }

    public List<RamResponse> doRecommender(Result result) {
        List<RamResponse> recommendList = new ArrayList<>();
        for (int i = 0; i < recNo; ++i) {
            Recommender recommender = result.getResult().get(i);
            logger.info(recommender.getItem() + " score: " + recommender.getScore());
            recommendList.add(new RamResponse(ramRepository.findByID(recommender.getItem())));
        }
        return recommendList;
    }
}
