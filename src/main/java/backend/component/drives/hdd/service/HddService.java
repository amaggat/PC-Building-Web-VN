package backend.component.drives.hdd.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface HddService {
    ResponseEntity<Object> findByProperties(String name, String chipset, String manufacturer, String size, Pageable pageable);

    ResponseEntity<Object> findById(String id, Integer userId);

    ResponseEntity<Object> getRecommendItemForUser(Integer userId);

    ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId);
}
