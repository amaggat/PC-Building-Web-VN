package backend.component.drives.sdd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SsdRepository extends JpaRepository<SolidStateDrive, String>, JpaSpecificationExecutor<SolidStateDrive> {

    @Query("SELECT DISTINCT ssd FROM SolidStateDrive ssd WHERE ssd.id = :id")
    @Transactional(readOnly = true)
    SolidStateDrive findByID(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("UPDATE SolidStateDrive ssd SET ssd.view = ssd.view + 1 WHERE ssd.id = :id")
    void update(@Param("id") String id);

}
