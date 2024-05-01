package backend.component.gpu.service;

import org.springframework.data.domain.Pageable;

public interface GpuService {

    Object findByProperties(String name, String chipset, String manufacturer, Integer VRam, Pageable pageable);

    Object findById(String id, Integer userId);

    Object getRecommendItemForUser(Integer userId);

    Object getRecommendItemForUserWithItemId(String id, Integer userId);
}
