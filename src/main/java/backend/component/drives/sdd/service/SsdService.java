package backend.component.drives.sdd.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface SsdService {
    ResponseEntity<Object> findByProperties(String name, String chipset, String manufacturer, String storage, Pageable pageable);

    ResponseEntity<Object> findById(String id, Integer userId);

    ResponseEntity<Object> getRecommendItemForUser(Integer userId);

    ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId);
}
