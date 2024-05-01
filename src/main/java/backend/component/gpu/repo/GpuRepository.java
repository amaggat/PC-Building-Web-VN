package backend.component.gpu.repo;

import backend.component.gpu.entity.GraphicProcessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GpuRepository extends JpaRepository<GraphicProcessor, Integer>, JpaSpecificationExecutor<GraphicProcessor> {

    @Query("SELECT DISTINCT gpu FROM GraphicProcessor gpu WHERE gpu.id =:id")
    @Transactional(readOnly = true)
    GraphicProcessor findByID(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("UPDATE GraphicProcessor gpu SET gpu.view = gpu.view + 1 WHERE gpu.id = :id")
    void update(@Param("id") String id);
}
