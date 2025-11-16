package lk.nibm.kd.hdse252.pdsa_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestockRequestDTO {
    private Long id;
    private String productCode;
    private String productName;
    private Integer requestedQuantity;
    private Integer currentStock;
    private String priority; // HIGH, MEDIUM, LOW
    private LocalDateTime requestedAt;
    private String status; // PENDING, PROCESSED, CANCELLED
}


