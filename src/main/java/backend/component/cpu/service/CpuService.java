package backend.component.cpu.service;

import org.springframework.data.domain.Pageable;

public interface CpuService {

    Object findByProperties(String name, String chipset, String manufacturer, String socket, Integer cores, Pageable pageable);

    Object findById(String id, Integer userId);

    Object getRecommendItemForUserWithItemId(String id, Integer userId);

    Object getRecommendItemForUser(Integer userId);

}
