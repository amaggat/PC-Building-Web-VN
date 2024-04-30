package backend.component.cpu.service;

import org.springframework.data.domain.Pageable;

public interface CpuService {

    Object findCpuByProperties(String name, String chipset, String manufacturer, String socket, Integer cores, Pageable pageable);

    Object findById(String id, Integer userId);

    Object getRecommendCpuForUserWithCpuId(String id, Integer userId);

    Object recommendCpuForUser(Integer userId);
}
