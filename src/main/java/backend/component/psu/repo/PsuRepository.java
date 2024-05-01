package backend.component.psu.repo;


import backend.component.psu.entity.PowerSupplyUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PsuRepository extends JpaRepository<PowerSupplyUnit, Integer>, JpaSpecificationExecutor<PowerSupplyUnit> {

//    @Query("SELECT DISTINCT psu FROM PowerSupplyUnit psu")
//    @Transactional(readOnly = true)
//    List<PowerSupplyUnit> findAll();

    @Query("SELECT DISTINCT psu FROM PowerSupplyUnit psu WHERE psu.id=:id")
    PowerSupplyUnit findByID(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("UPDATE PowerSupplyUnit psu SET psu.view = psu.view + 1 WHERE psu.id = :id")
    void update(@Param("id") String id);
}
