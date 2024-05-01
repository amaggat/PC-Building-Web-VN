package backend.component.cpu.controller;

import backend.component.cpu.service.CpuService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CpuController {

    private static final Logger logger = LogManager.getLogger(CpuController.class);

    @Autowired
    private CpuService cpuService;


    @GetMapping("/api/cpu")
    public ResponseEntity<Object> findCpuByProperties(@RequestParam(name = "name", required = false) String name,
                                                      @RequestParam(name = "chipset", required = false) String chipset,
                                                      @RequestParam(name = "manufacturer", required = false) String manufacturer,
                                                      @RequestParam(name = "socket", required = false) String socket,
                                                      @RequestParam(name = "cores", required = false) Integer cores,
                                                      Pageable pageable) {
        logger.info("##### REQUEST RECEIVED (CPU - Find) #####");

        try {
            logger.info("Request Data: {name:" + name + ", chipset:" + chipset + ", manufacturer:" + manufacturer + ", socket:" + socket + ", cores:" + cores + "}");
            ResponseEntity<Object> response = cpuService.findByProperties(name, chipset, manufacturer, socket, cores, pageable);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (CPU - Find) #####");
        }
    }

    @GetMapping("/api/cpu/{CpuID}")
    public ResponseEntity<Object> findById(@PathVariable("CpuID") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (CPU - Find By ID) #####");
        try {
            logger.info("Request Data: {user_id:" + userId + ", item_id:" + id + "}");
            ResponseEntity<Object> response = cpuService.findById(id, userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (CPU - Find By ID) #####");
        }
    }


    @GetMapping("/api/recommend/cpu")
    public ResponseEntity<Object> getRecommendCpuForUser(@CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (CPU - Recommend) #####");

        try {
            logger.info("Request Data: {userID:" + userId + "}");
            ResponseEntity<Object> response = cpuService.getRecommendItemForUser(userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (CPU - Recommend) #####");
        }

    }
    @GetMapping("/api/recommend/cpu/{id}")
    public ResponseEntity<Object> getRecommendCpuForUserWithCpuId(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (CPU - Recommend by ID) #####");
        try {
            logger.info("Request Data: {user_id:" + userId + ", item_id:" + id + "}");
            ResponseEntity<Object> response = cpuService.getRecommendItemForUserWithItemId(id, userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (CPU - Recommend by ID) #####");
        }
    }
}
