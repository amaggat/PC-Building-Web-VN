package backend.component.ram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RamRepository extends JpaRepository<Ram, Integer>, JpaSpecificationExecutor<Ram> {

    @Query("SELECT DISTINCT ram From Ram ram WHERE ram.id= :id")
    @Transactional(readOnly = true)
    Ram findByID(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("UPDATE Ram ram SET ram.view = ram.view + 1 WHERE ram.id = :id")
    void update(@Param("id") String id);
}
