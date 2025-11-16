package lk.nibm.kd.hdse252.pdsa_cw.repositories;

import lk.nibm.kd.hdse252.pdsa_cw.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCode(String productCode);
    
    List<Product> findByCategory(String category);
    
    List<Product> findByWoodType(String woodType);
    
    @Query("SELECT p FROM Product p WHERE p.stock <= p.minStockLevel")
    List<Product> findLowStockProducts();
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:searchTerm% OR p.description LIKE %:searchTerm% OR p.productCode LIKE %:searchTerm%")
    List<Product> searchProducts(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT p FROM Product p WHERE p.sellingPrice BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.stock >= :minStock")
    List<Product> findByMinStock(@Param("minStock") Integer minStock);
}

