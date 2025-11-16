package lk.nibm.kd.hdse252.pdsa_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String productCode;
    private String name; // Item name
    private String description;
    private String category;
    private String itemType; // Item type
    private String woodType;
    private String finishedType; // Finished type
    private Boolean isCarved; // Carved or not
    private Double cost; // Cost price
    private Double sellingPrice; // Selling price
    private Integer stock; // Stock quantity
    private Integer minStockLevel;
    private Double price; // For backward compatibility (maps to sellingPrice)
    private Integer stockQuantity; // For backward compatibility (maps to stock)
    private String dimensions;
    private Double weight;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double similarityScore; // For recommendation algorithm
}

