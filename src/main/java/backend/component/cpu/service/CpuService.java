package backend.component.cpu.service;

import backend.component.cpu.entity.CentralProcessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CpuService {

    Page<CentralProcessor> findCpuByProperties(String name, String chipset, String manufacturer, String socket, Integer cores, Pageable pageable);

    CentralProcessor findById(String id, Integer userId);

    Page<CentralProcessor> recommendList(String id, Integer userId);

    Page<CentralProcessor> recommendCpuForUser(Integer userId);
}
