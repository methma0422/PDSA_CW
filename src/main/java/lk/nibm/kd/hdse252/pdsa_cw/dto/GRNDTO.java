package lk.nibm.kd.hdse252.pdsa_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GRNDTO {
    private Long id;
    private String grnNumber;
    private Long purchaseOrderId;
    private PurchaseOrderDTO purchaseOrder;
    private LocalDateTime receivedDate;
    private String status;
    private String notes;
    private List<GRNItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


