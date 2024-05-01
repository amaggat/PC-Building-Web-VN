package backend.component.gpu.controller;

import backend.component.gpu.service.GpuService;
import backend.security.utils.JwtUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GpuController {

    private static final Logger logger = LogManager.getLogger(GpuController.class);

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private GpuService gpuService;

    @GetMapping("/api/gpu")
    public ResponseEntity<Object> findByProperties(@RequestParam(name = "name", required = false) String name,
                                                   @RequestParam(name = "chipset", required = false) String chipset,
                                                   @RequestParam(name = "manufacturer", required = false) String manufacturer,
                                                   @RequestParam(name = "VRam", required = false) Integer VRam,
                                                   Pageable pageable) {
        logger.info("##### REQUEST RECEIVED (GPU - Find) #####");

        try {
            logger.info("Request Data: {name:" + name + ", chipset:" + chipset + ", manufacturer:" + manufacturer + ", VRam:" + VRam + "}");
            ResponseEntity<Object> response = gpuService.findByProperties(name, chipset, manufacturer, VRam, pageable);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (GPU - Find) #####");
        }
    }

    @GetMapping("/api/gpu/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (GPU - Find By ID) #####");
        try {
            logger.info("Request Data: {userID:" + userId + ", cpu_id:" + id + "}");
            ResponseEntity<Object> response = gpuService.findById(id, userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (GPU - Find By ID) #####");
        }
    }

    @GetMapping("/api/recommend/gpu")
    public ResponseEntity<Object> getRecommendItemForUser(@CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (GPU - Recommend) #####");

        try {
            logger.info("Request Data: {userID:" + userId + "}");
            ResponseEntity<Object> response = gpuService.getRecommendItemForUser(userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (GPU - Recommend) #####");
        }
    }

    @GetMapping("/api/recommend/gpu/{id}")
    public ResponseEntity<Object> getRecommendItemForUserWithItemId(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (CPU - Recommend with Item ID) #####");
        try {
            logger.info("Request Data: {userID:" + userId + ", cpu_id:" + id + "}");
            ResponseEntity<Object> response = gpuService.getRecommendItemForUserWithItemId(id, userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (GPU - Recommend with Item ID) #####");
        }
    }

}
