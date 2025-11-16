package lk.nibm.kd.hdse252.pdsa_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GRNItemDTO {
    private Long id;
    private Long grnId;
    private Long productId;
    private ProductDTO product;
    private Integer orderedQuantity;
    private Integer receivedQuantity;
    private Double unitPrice;
    private Double totalPrice;
}

