package lk.nibm.kd.hdse252.pdsa_cw.repositories;

import lk.nibm.kd.hdse252.pdsa_cw.entities.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    Optional<SalesOrder> findByOrderNumber(String orderNumber);
    List<SalesOrder> findByStatus(String status);
    List<SalesOrder> findByCustomerId(Long customerId);
    boolean existsByOrderNumber(String orderNumber);
}

