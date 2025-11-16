package lk.nibm.kd.hdse252.pdsa_cw.repositories;

import lk.nibm.kd.hdse252.pdsa_cw.entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);
    List<PurchaseOrder> findByStatus(String status);
    List<PurchaseOrder> findBySupplierId(Long supplierId);
    boolean existsByOrderNumber(String orderNumber);
}

