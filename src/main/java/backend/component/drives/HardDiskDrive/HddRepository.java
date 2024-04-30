package backend.component.drives.HardDiskDrive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface HddRepository extends JpaRepository<HardDiskDrive, Integer>, JpaSpecificationExecutor<HardDiskDrive> {

    @Query("SELECT DISTINCT hdd FROM HardDiskDrive hdd WHERE hdd.id= :id")
    @Transactional(readOnly = true)
    HardDiskDrive findByID(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("UPDATE HardDiskDrive hdd SET hdd.view = hdd.view + 1 WHERE hdd.id = :id")
    void update(@Param("id") String id);

}
