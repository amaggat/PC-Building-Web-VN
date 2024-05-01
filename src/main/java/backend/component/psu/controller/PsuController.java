package backend.component.psu.controller;


import backend.component.psu.entity.PowerSupplyUnit;
import backend.component.psu.repo.PsuRepository;
import backend.component.psu.service.PsuService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private PsuService psuService;

    @GetMapping("/api/psu")
    public ResponseEntity<Object> findByProperties(@RequestParam(name = "name", required = false) String name,
                                                   @RequestParam(name = "chipset", required = false) String chipset,
                                                   @RequestParam(name = "manufacturer", required = false) String manufacturer,
                                                   @RequestParam(name = "standard_80", required = false) String standard_80,
                                                   @RequestParam(name = "power", required = false) Integer power,
                                                   Pageable pageable) {
        logger.info("##### REQUEST RECEIVED (PSU - Find) #####");

        try {
            logger.info("Request Data: {name:" + name + ", chipset:" + chipset +
                    ", manufacturer:" + manufacturer + ", standard_80:" + standard_80+ ", power:" + power + "}");
            ResponseEntity<Object> response = psuService.findByProperties(name, chipset, manufacturer, standard_80, power, pageable);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (PSU - Find) #####");
        }
    }

    @GetMapping("/api/psu/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (PSU - Find By ID) #####");
        try {
            logger.info("Request Data: {user_id:" + userId + ", item_id:" + id + "}");
            ResponseEntity<Object> response = psuService.findById(id, userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (PSU - Find By ID) #####");
        }
    }

    @GetMapping("/api/recommend/psu")
    public ResponseEntity<Object> getRecommendItemForUser(@CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (PSU - Recommend) #####");

        try {
            logger.info("Request Data: {userID:" + userId + "}");
            ResponseEntity<Object> response = psuService.getRecommendItemForUser(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (PSU - Recommend) #####");
        }
    }

    @GetMapping("/api/recommend/psu/{id}")
    public ResponseEntity<Object> getRecommendItemForUserWithItemId(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (PSU - Recommend by ID) #####");
        try {
            logger.info("Request Data: {user_id:" + userId + ", item_id:" + id + "}");
            ResponseEntity<Object> response = psuService.getRecommendItemForUserWithItemId(id, userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (PSU - Recommend by ID) #####");
        }
    }

}
