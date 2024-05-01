package backend.component.ram.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RamService {
    ResponseEntity<Object> findByProperties(String name, String chipset, String manufacturer, String sizeOfRam, Integer clockSpeed, Pageable pageable);

    ResponseEntity<Object> findById(String id, Integer userId);

    ResponseEntity<Object> getRecommendItemForUser(Integer userId);

    ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId);
}
