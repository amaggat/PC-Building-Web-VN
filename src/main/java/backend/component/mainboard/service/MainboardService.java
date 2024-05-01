package backend.component.mainboard.service;

import backend.component.mainboard.entity.Mainboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface MainboardService {

    ResponseEntity<Object> findByProperties(String name, String chipset, String socket, String manufacturer, String sizeOfRam,
                                            String memorySlot, String formFactor, Pageable pageable);

    ResponseEntity<Object> findById(String id, Integer userId);

    ResponseEntity<Object> getRecommendItemForUser(Integer userId);

    ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId);

}
