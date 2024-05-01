package backend.component.cpu.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CpuService {

    ResponseEntity<Object> findByProperties(String name, String chipset, String manufacturer, String socket, Integer cores, Pageable pageable);

    ResponseEntity<Object> findById(String id, Integer userId);

    ResponseEntity<Object> getRecommendItemForUserWithItemId(String id, Integer userId);

    ResponseEntity<Object> getRecommendItemForUser(Integer userId);

}
