package backend.component.drives.sdd.controller;

import backend.component.drives.sdd.entity.SolidStateDrive;
import backend.component.drives.sdd.repo.SsdRepository;
import backend.component.drives.sdd.service.SsdService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private SsdService ssdService;

    @GetMapping("/api/ssd")
    public ResponseEntity<Object> list(@RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "chipset", required = false) String chipset,
                                      @RequestParam(name = "manufacturer", required = false) String manufacturer,
                                      @RequestParam(name = "storage", required = false) String storage,
                                      Pageable pageable) {
        logger.info("##### REQUEST RECEIVED (SSD - Find) #####");

        try {
            logger.info("Request Data: {name:" + name + ", chipset:" + chipset +
                    ", manufacturer:" + manufacturer + ", storage:" + storage + "}");
            ResponseEntity<Object> response = ssdService.findByProperties(name, chipset, manufacturer, storage, pageable);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (SSD - Find) #####");
        }
    }

    @GetMapping("/api/ssd/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (SSD - Find By ID) #####");
        try {
            logger.info("Request Data: {userID:" + userId + ", cpu_id:" + id + "}");
            ResponseEntity<Object> response = ssdService.findById(id, userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (SSD - Find By ID) #####");
        }
    }

    @GetMapping("/api/recommend/ssd")
    public ResponseEntity<Object> getRecommendItemForUser(@CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (SSD - Recommend) #####");

        try {
            logger.info("Request Data: {userID:" + userId + "}");
            ResponseEntity<Object> response = ssdService.getRecommendItemForUser(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (SSD - Recommend) #####");
        }
    }

    @GetMapping("/api/recommend/ssd/{id}")
    public ResponseEntity<Object> recommendList(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (SSD - Recommend by ID) #####");
        try {
            logger.info("Request Data: {user_id:" + userId + ", item_id:" + id + "}");
            ResponseEntity<Object> response = ssdService.getRecommendItemForUserWithItemId(id, userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (SSD - Recommend by ID) #####");
        }
    }
}
