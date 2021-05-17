package backend.recommendation.type;

import backend.recommendation.type.rating.CpuRating;
import backend.recommendation.type.rating.GpuRating;
import backend.recommendation.type.rating.HddRating;
import backend.recommendation.type.rating.MainboardRating;
import backend.recommendation.type.rating.PsuRating;
import backend.recommendation.type.rating.RamRating;
import backend.recommendation.type.rating.RetailerRating;
import backend.recommendation.type.rating.SsdRating;
import backend.recommendation.type.repository.CpuRatingRepository;
import backend.recommendation.type.repository.GpuRatingRepository;
import backend.recommendation.type.repository.HddRatingRepository;
import backend.recommendation.type.repository.MainRatingRepository;
import backend.recommendation.type.repository.PsuRatingRepository;
import backend.recommendation.type.repository.RamRatingRepository;
import backend.recommendation.type.repository.SsdRatingRepository;
import backend.retailer.RetailerRatingRepository;
import backend.security.model.AuthenticationResponse;
import backend.user.User;
import backend.user.UserActivity;
import backend.user.UserActivityRepository;
import backend.user.UserRepository;
import backend.util.ClientLevel;
import backend.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingController {

    private static final Logger logger = LogManager.getLogger(RatingController.class);

    private final CpuRatingRepository cpuRatingRepository;
    private final GpuRatingRepository gpuRatingRepository;
    private final MainRatingRepository mainRatingRepository;
    private final RamRatingRepository ramRatingRepository;
    private final SsdRatingRepository ssdRatingRepository;
    private final HddRatingRepository hddRatingRepository;
    private final PsuRatingRepository psuRatingRepository;
    private final RetailerRatingRepository retailerRatingRepository;
    private final UserRepository userRepository;
    private final UserActivityRepository userActivityRepository;

    public RatingController(CpuRatingRepository cpuRatingRepository,
                            GpuRatingRepository gpuRatingRepository,
                            MainRatingRepository mainRatingRepository,
                            RamRatingRepository ramRatingRepository,
                            SsdRatingRepository ssdRatingRepository,
                            HddRatingRepository hddRatingRepository,
                            PsuRatingRepository psuRatingRepository, RetailerRatingRepository retailerRatingRepository, UserRepository userRepository, UserActivityRepository userActivityRepository) {
        this.cpuRatingRepository = cpuRatingRepository;
        this.gpuRatingRepository = gpuRatingRepository;
        this.mainRatingRepository = mainRatingRepository;
        this.ramRatingRepository = ramRatingRepository;
        this.ssdRatingRepository = ssdRatingRepository;
        this.hddRatingRepository = hddRatingRepository;
        this.psuRatingRepository = psuRatingRepository;
        this.retailerRatingRepository = retailerRatingRepository;
        this.userRepository = userRepository;
        this.userActivityRepository = userActivityRepository;
    }

    @PostMapping("/user/rating/cpu")
    public ResponseEntity<?> rating(@RequestBody CpuRating cpuRating, @CookieValue(value = "userid") Integer userid) {
        cpuRatingRepository.save(cpuRating);
        updateLog(userid, cpuRating.getCentralProcessor(), cpuRating.getRating());
        return ResponseEntity.ok(new AuthenticationResponse("Rated", userid.toString()));
    }

    @PostMapping("/user/rating/ssd")
    public ResponseEntity<?> rating(@RequestBody SsdRating ssdRating, @CookieValue(value = "userid") Integer userid) {
        ssdRatingRepository.save(ssdRating);
        updateLog(userid, ssdRating.getSsd(), ssdRating.getRating());
        return ResponseEntity.ok(new AuthenticationResponse("Rated", userid.toString()));
    }

    @PostMapping("/user/rating/gpu")
    public ResponseEntity<?> rating(@RequestBody GpuRating gpuRating, @CookieValue(value = "userid") Integer userid) {
        gpuRatingRepository.save(gpuRating);
        updateLog(userid, gpuRating.getGraphicProcessor(), gpuRating.getRating());
        return ResponseEntity.ok(new AuthenticationResponse("Rated", userid.toString()));
    }

    @PostMapping("/user/rating/mainboard")
    public ResponseEntity<?> rating(@RequestBody MainboardRating mainboardRating, @CookieValue(value = "userid") Integer userid) {
        mainRatingRepository.save(mainboardRating);
        updateLog(userid, mainboardRating.getMainboard(), mainboardRating.getRating());
        return ResponseEntity.ok(new AuthenticationResponse("Rated", userid.toString()));
    }

    @PostMapping("/user/rating/ram")
    public ResponseEntity<?> rating(@RequestBody RamRating ramRating, @CookieValue(value = "userid") Integer userid) {
        ramRatingRepository.save(ramRating);
        updateLog(userid, ramRating.getRam(), ramRating.getRating());
        return ResponseEntity.ok(new AuthenticationResponse("Rated", userid.toString()));
    }

    @PostMapping("/user/rating/hdd")
    public ResponseEntity<?> rating(@RequestBody HddRating hddRating, @CookieValue(value = "userid") Integer userid) {
        hddRatingRepository.save(hddRating);
        updateLog(userid, hddRating.getHdd(), hddRating.getRating());
        return ResponseEntity.ok(new AuthenticationResponse("Rated", userid.toString()));
    }

    @PostMapping("/user/rating/psu")
    public ResponseEntity<?> rating(@RequestBody PsuRating psuRating, @CookieValue(value = "userid") Integer userid) {
        psuRatingRepository.save(psuRating);
        updateLog(userid, psuRating.getPsu(), psuRating.getRating());
        return ResponseEntity.ok(new AuthenticationResponse("Rated", userid.toString()));
    }

    @PostMapping("/user/rating/retailer")
    public ResponseEntity<?> rating(@RequestBody RetailerRating retailerRating, @CookieValue(value = "userid") Integer userid) {
        retailerRatingRepository.save(retailerRating);
        return ResponseEntity.ok(new AuthenticationResponse("Rated", userid.toString()));
    }

    void updateLog(Integer userid, String componentId, double rating) {
        User user = userRepository.findByID(userid);
        if (user != null) {
            String action = "rate " + (int) rating + " star";
            userActivityRepository.save(new UserActivity(user, action, componentId));
            try {
                Utility.sendActivity(Utility.URL_GET, action, userid, componentId);
                logger.log(ClientLevel.CLIENT, "Success");
            } catch (Exception e) {
                logger.log(ClientLevel.CLIENT, "Unsuccess");
            }
        }
    }
}
