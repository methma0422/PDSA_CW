package lk.nibm.kd.hdse252.pdsa_cw.repositories;

import lk.nibm.kd.hdse252.pdsa_cw.entities.GRN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GRNRepository extends JpaRepository<GRN, Long> {
    Optional<GRN> findByGrnNumber(String grnNumber);
    List<GRN> findByStatus(String status);
    List<GRN> findByPurchaseOrderId(Long purchaseOrderId);
    boolean existsByGrnNumber(String grnNumber);
}


