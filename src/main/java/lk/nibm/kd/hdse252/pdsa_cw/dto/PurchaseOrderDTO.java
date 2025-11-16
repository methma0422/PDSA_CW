package lk.nibm.kd.hdse252.pdsa_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDTO {
    private Long id;
    private String orderNumber;
    private Long supplierId;
    private SupplierDTO supplier;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;
    private String status;
    private Double totalAmount;
    private String notes;
    private List<PurchaseOrderItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

