package lk.nibm.kd.hdse252.pdsa_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderItemDTO {
    private Long id;
    private Long salesOrderId;
    private Long productId;
    private ProductDTO product;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}

