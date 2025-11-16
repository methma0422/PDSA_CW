package lk.nibm.kd.hdse252.pdsa_cw.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity for restock requests managed in a Queue
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestockRequest {
    private Long id;
    private String productCode;
    private String productName;
    private Integer requestedQuantity;
    private Integer currentStock;
    private String priority; // HIGH, MEDIUM, LOW
    private LocalDateTime requestedAt;
    private String status; // PENDING, PROCESSED, CANCELLED
    
    public RestockRequest(String productCode, String productName, Integer requestedQuantity, 
                         Integer currentStock, String priority) {
        this.productCode = productCode;
        this.productName = productName;
        this.requestedQuantity = requestedQuantity;
        this.currentStock = currentStock;
        this.priority = priority;
        this.requestedAt = LocalDateTime.now();
        this.status = "PENDING";
    }
}


