package backend.component.psu.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PsuService {
    ResponseEntity<Object> findByProperties(String name, String chipset, String manufacturer, String standard_80, Integer power, Pageable pageable);

    ResponseEntity<Object> findById(String id, Integer userId);

    ResponseEntity<Object> getRecommendItemForUser(Integer userId);

    ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId);
}
