package backend.component.mainboard.controller;

import backend.component.mainboard.repo.MainboardRepository;
import backend.component.mainboard.entity.Mainboard;
import backend.component.mainboard.service.MainboardService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class MainboardController {

    private static final Logger logger = LogManager.getLogger(MainboardController.class);

    @Autowired
    private MainboardService mainboardService;

    @GetMapping("/api/mainboard")
    public ResponseEntity<Object> findByProperties(@RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "chipset", required = false) String chipset,
                                @RequestParam(name = "socket", required = false) String socket,
                                @RequestParam(name = "manufacturer", required = false) String manufacturer,
                                @RequestParam(name = "sizeofram", required = false) String sizeOfRam,
                                @RequestParam(name = "memory_slot", required = false) String memorySlot,
                                @RequestParam(name = "formfactor", required = false) String formFactor,
                                Pageable pageable) {
        logger.info("##### REQUEST RECEIVED (Mainboard - Find) #####");

        try {
            logger.info("Request Data: {name:" + name + ", chipset:" + chipset +
                    ", manufacturer:" + manufacturer + ", size_of_ram:" + sizeOfRam
                    + ", memorySlot:" + memorySlot+ ", formFactor:" + formFactor + "}");
            ResponseEntity<Object> response = mainboardService.findByProperties(name, chipset, socket, manufacturer, sizeOfRam, memorySlot, formFactor, pageable);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (Mainboard - Find) #####");
        }
    }

    @GetMapping("/api/mainboard/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (Mainboard - Find By ID) #####");
        try {
            logger.info("Request Data: {userID:" + userId + ", cpu_id:" + id + "}");
            ResponseEntity<Object> response = mainboardService.findById(id, userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (Mainboard - Find By ID) #####");
        }
    }

    @GetMapping("/api/recommend/mainboard")
    public ResponseEntity<Object> getRecommendItemForUser(@CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (GPU - Recommend) #####");

        try {
            logger.info("Request Data: {userID:" + userId + "}");
            ResponseEntity<Object> response = mainboardService.getRecommendItemForUser(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (GPU - Recommend) #####");
        }
    }

    @GetMapping("/api/recommend/mainboard/{id}")
    public ResponseEntity<Object> getRecommendItemForUserWithItemId(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (CPU - Recommend by ID) #####");
        try {
            logger.info("Request Data: {userID:" + userId + ", cpu_id:" + id + "}");
            ResponseEntity<Object> response = mainboardService.getRecommendItemForUserWithItemId(id, userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (CPU - Recommend by ID) #####");
        }
    }

}
