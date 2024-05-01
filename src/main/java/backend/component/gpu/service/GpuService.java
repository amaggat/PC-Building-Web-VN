package backend.component.gpu.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface GpuService {

    ResponseEntity<Object> findByProperties(String name, String chipset, String manufacturer, Integer VRam, Pageable pageable);

    ResponseEntity<Object> findById(String id, Integer userId);

    ResponseEntity<Object> getRecommendItemForUser(Integer userId);

    ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId);
}
