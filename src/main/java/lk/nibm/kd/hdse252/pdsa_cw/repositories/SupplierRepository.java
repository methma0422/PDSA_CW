package lk.nibm.kd.hdse252.pdsa_cw.repositories;

import lk.nibm.kd.hdse252.pdsa_cw.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findBySupplierCode(String supplierCode);
    List<Supplier> findByStatus(String status);
    boolean existsBySupplierCode(String supplierCode);
}

