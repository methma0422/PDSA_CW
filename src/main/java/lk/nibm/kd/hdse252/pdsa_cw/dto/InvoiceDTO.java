package lk.nibm.kd.hdse252.pdsa_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private Long id;
    private String invoiceNumber;
    private Long salesOrderId;
    private SalesOrderDTO salesOrder;
    private Long customerId;
    private CustomerDTO customer;
    private LocalDateTime invoiceDate;
    private LocalDateTime dueDate;
    private String status;
    private Double subtotal;
    private Double tax;
    private Double discount;
    private Double totalAmount;
    private Double paidAmount;
    private Double balance;
    private String notes;
    private List<InvoiceItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

