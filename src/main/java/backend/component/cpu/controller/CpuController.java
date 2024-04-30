package backend.component.cpu.controller;



import backend.component.cpu.entity.CentralProcessor;
import backend.component.cpu.service.CpuService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
            Object response = cpuService.findCpuByProperties(name, chipset, manufacturer, socket, cores, pageable);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (CPU - Find) #####");
        }
    }

    @GetMapping("/api/recommend/cpu")
    public ResponseEntity<Object> recommendFront(@CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (CPU - Recommend) #####");

        try {
            logger.info("Request Data: {userID:" + userId + "}");
            Object response = cpuService.recommendCpuForUser(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (CPU - Recommend) #####");
        }

    }

    @GetMapping("/api/cpu/{CpuID}")
    public ResponseEntity<Object> SearchById(@PathVariable("CpuID") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (CPU - Find By ID) #####");
        try {
            logger.info("Request Data: {userID:" + userId + ", cpu_id:" + id + "}");
            Object response = cpuService.findById(id, userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("Response sent");
            logger.info("##### FINISH REQUEST (CPU - Find By ID) #####");
        }
    }

    @GetMapping("/api/recommend/cpu/{id}")
    public ResponseEntity<Object> recommendList(@PathVariable("id") String id, @CookieValue(value = "userId", required = false) Integer userId) {
        logger.info("##### REQUEST RECEIVED (CPU - Recommend by ID) #####");
        try {
            logger.info("Request Data: {userID:" + userId + ", cpu_id:" + id + "}");
            Object response = cpuService.recommendList(id, userId);
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
