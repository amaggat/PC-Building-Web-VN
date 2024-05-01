package backend.pcprofile;


import backend.security.model.AuthenticationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Objects;

@RestController
public class PcProfileController {

    private final PcProfileRepository pcProfileRepository;

    public PcProfileController(PcProfileRepository pcProfileRepository) {
        this.pcProfileRepository = pcProfileRepository;
    }

    @GetMapping("/api/pcprofile")
    public Page<PcProfile> list(@RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "type", required = false) String type,
                                @RequestParam(name = "id", required = false) Integer id,
                                Pageable pageable) {

        Page<PcProfile> pcProfiles = pcProfileRepository.findAll((Specification<PcProfile>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(name)) {
                p = cb.and(p, cb.like(root.get("name"), "%" + name + "%"));
            }
            if (Objects.nonNull(type)) {
                p = cb.and(p, cb.like(root.get("type"), "%" + type + "%"));
            }
            if (Objects.nonNull(id)) {
                p = cb.and(p, cb.equal(root.get("id"), id));
            }
            cq.orderBy(cb.desc(root.get("name")), cb.asc(root.get("id")));
            return p;
        }, pageable);
        return pcProfiles;
    }

    @GetMapping("/api/pcprofile/{PcID}")
    public PcProfile findByID(@PathVariable("PcID") String id) {
        return pcProfileRepository.findByID(id);
    }

    @GetMapping("/api/user/{UserID}/pcprofile")
    public Page<PcProfile> findByUserID(@PathVariable("UserID") Integer id) {
        List<PcProfile> pcProfiles = pcProfileRepository.findByUserID(id);
        Page<PcProfile> cpuPage = new PageImpl<>(pcProfiles);
        return cpuPage;
    }

    @PostMapping("user/addPc")
    public ResponseEntity<?> addNewPcProfile(@RequestBody PcProfile pcProfile) {
        pcProfile.setId(pcProfile.getUser().getId() + "-" + pcProfile.getName());
        pcProfileRepository.save(pcProfile);
        return ResponseEntity.ok(new AuthenticationResponse("Added", pcProfile.getUser().getName()));
    }
}
