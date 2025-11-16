package lk.nibm.kd.hdse252.pdsa_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderDTO {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private CustomerDTO customer;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;
    private String status;
    private Double subtotal;
    private Double tax;
    private Double discount;
    private Double totalAmount;
    private String notes;
    private List<SalesOrderItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

