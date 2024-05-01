package backend.component.mainboard.service;

import backend.component.mainboard.entity.Mainboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MainboardService {

    Object findByProperties(String name, String chipset, String socket, String manufacturer, String sizeOfRam,
                                     String memorySlot, String formFactor, Pageable pageable);

    Object findById(String id, Integer userId);

    Object getRecommendItemForUser(Integer userId);

    Object getRecommendItemForUserWithItemId(String id, Integer userId);

}
