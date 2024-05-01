package backend.component.drives.hdd.controller;


import backend.component.drives.hdd.service.HddService;
import backend.security.utils.JwtUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HddController {
    private static final Logger logger = LogManager.getLogger(HddController.class);

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private HddService hddService;

    @GetMapping("/api/hdd")
    public ResponseEntity<Object> findByProperties(@RequestParam(name = "name", required = false) String name,
                                    @RequestParam(name = "chipset", required = false) String chipset,
                                    @RequestParam(name = "manufacturer", required = false) String manufacturer,
                                    @RequestParam(name = "storage", required = false) String storage,
                                    Pageable pageable) {
        logger.info("##### REQUEST RECEIVED (HDD - Find) #####");

        try {
            logger.info("Request Data: {name:" + name + ", chipset:" + chipset +
                    ", manufacturer:" + manufacturer + ", storage:" + storage + "}");
            ResponseEntity<Object> response = hddService.findByProperties(name, chipset, manufacturer, storage, pageable);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (HDD - Find) #####");
        }
    }

    @GetMapping("/api/hdd/{id}")
    public ResponseEntity<Object> SearchByID(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (HDD - Find By ID) #####");
        try {
            logger.info("Request Data: {userID:" + userId + ", cpu_id:" + id + "}");
            ResponseEntity<Object> response = hddService.findById(id, userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (HDD - Find By ID) #####");
        }
    }

    @GetMapping("/api/recommend/hdd")
    public ResponseEntity<Object> getRecommendItemForUser(@CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (HDD - Recommend) #####");

        try {
            logger.info("Request Data: {userID:" + userId + "}");
            ResponseEntity<Object> response = hddService.getRecommendItemForUser(userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (HDD - Recommend) #####");
        }
    }

    @GetMapping("/api/recommend/hdd/{id}")
    public ResponseEntity<Object> getRecommendItemForUserWithItemId(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (HDD - Recommend by ID) #####");
        try {
            logger.info("Request Data: {user_id:" + userId + ", item_id:" + id + "}");
            ResponseEntity<Object> response = hddService.getRecommendItemForUserWithItemId(id, userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (HDD - Recommend by ID) #####");
        }
    }
}
