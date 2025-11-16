package lk.nibm.kd.hdse252.pdsa_cw.repositories;

import lk.nibm.kd.hdse252.pdsa_cw.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerCode(String customerCode);
    List<Customer> findByStatus(String status);
    List<Customer> findByCustomerType(String customerType);
    boolean existsByCustomerCode(String customerCode);
}

