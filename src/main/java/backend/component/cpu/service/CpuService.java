package backend.component.cpu.service;

import backend.component.cpu.entity.CentralProcessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CpuService {

    Object findCpuByProperties(String name, String chipset, String manufacturer, String socket, Integer cores, Pageable pageable);

    Object findById(String id, Integer userId);

    Object recommendList(String id, Integer userId);

    Object recommendCpuForUser(Integer userId);
}
