package lk.nibm.kd.hdse252.pdsa_cw.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String productCode;
    
    @Column(nullable = false)
    private String name; // Item name
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private String itemType; // Item type (e.g., Statue, Furniture, Decoration)
    
    @Column(nullable = false)
    private String woodType;
    
    @Column(nullable = false)
    private String finishedType; // Finished type (e.g., Polished, Unfinished, Varnished)
    
    @Column(nullable = false)
    private Boolean isCarved; // Carved or not
    
    @Column(nullable = false)
    private Double cost; // Cost price
    
    @Column(nullable = false)
    private Double sellingPrice; // Selling price (renamed from price)
    
    @Column(nullable = false)
    private Integer stock; // Stock quantity (renamed from stockQuantity)
    
    @Column(nullable = false)
    private Integer minStockLevel;
    
    @Column(nullable = false)
    private String dimensions;
    
    @Column(nullable = false)
    private Double weight;
    
    // Keep price for backward compatibility (maps to sellingPrice)
    @Transient
    private Double price;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

