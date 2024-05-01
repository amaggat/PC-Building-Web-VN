package backend.component.ram.controller;


import backend.component.ram.repo.RamRepository;
import backend.component.ram.entity.Ram;
import backend.component.ram.service.RamService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private RamService ramService;

    @GetMapping("/api/ram")
    public ResponseEntity<Object> findByProperties(@RequestParam(name = "name", required = false) String name,
                                                   @RequestParam(name = "chipset", required = false) String chipset,
                                                   @RequestParam(name = "manufacturer", required = false) String manufacturer,
                                                   @RequestParam(name = "sizeOfRam", required = false) String sizeOfRam,
                                                   @RequestParam(name = "clockSpeed", required = false) Integer clockSpeed,
                                                   Pageable pageable) {
        logger.info("##### REQUEST RECEIVED (RAM - Find) #####");

        try {
            logger.info("Request Data: {name:" + name + ", chipset:" + chipset +
                    ", manufacturer:" + manufacturer + ", sizeOfRam:" + sizeOfRam + ", clockSpeed:" + clockSpeed + "}");
            ResponseEntity<Object> response = ramService.findByProperties(name, chipset, manufacturer, sizeOfRam, clockSpeed, pageable);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (RAM - Find) #####");
        }
    }

    @GetMapping("/api/ram/{RamID}")
    public ResponseEntity<Object> findById(@PathVariable("RamID") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (RAM - Find By ID) #####");
        try {
            logger.info("Request Data: {user_id:" + userId + ", item_id:" + id + "}");
            ResponseEntity<Object> response = ramService.findById(id, userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (RAM - Find By ID) #####");
        }
    }

    @GetMapping("/api/recommend/ram")
    public ResponseEntity<Object> getRecommendItemForUser(@CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (RAM - Recommend) #####");

        try {
            logger.info("Request Data: {userID:" + userId + "}");
            ResponseEntity<Object> response = ramService.getRecommendItemForUser(userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (RAM - Recommend) #####");
        }
    }

    @GetMapping("/api/recommend/ram/{id}")
    public ResponseEntity<Object> getRecommendItemForUserWithItemId(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (RAM - Recommend by ID) #####");
        try {
            logger.info("Request Data: {user_id:" + userId + ", item_id:" + id + "}");
            ResponseEntity<Object> response = ramService.getRecommendItemForUserWithItemId(id, userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (RAM - Recommend by ID) #####");
        }
    }

}
