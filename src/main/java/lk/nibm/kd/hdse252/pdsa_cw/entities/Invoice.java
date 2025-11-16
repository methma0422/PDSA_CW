package lk.nibm.kd.hdse252.pdsa_cw.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String invoiceNumber;
    
    @ManyToOne
    @JoinColumn(name = "sales_order_id", nullable = false)
    private SalesOrder salesOrder;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(nullable = false)
    private LocalDateTime invoiceDate;
    
    @Column(nullable = false)
    private LocalDateTime dueDate;
    
    @Column(nullable = false)
    private String status; // DRAFT, ISSUED, PAID, OVERDUE, CANCELLED
    
    @Column(nullable = false)
    private Double subtotal;
    
    @Column(nullable = false)
    private Double tax;
    
    @Column(nullable = false)
    private Double discount;
    
    @Column(nullable = false)
    private Double totalAmount;
    
    @Column(nullable = false)
    private Double paidAmount;
    
    @Column(nullable = false)
    private Double balance;
    
    @Column(length = 1000)
    private String notes;
    
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InvoiceItem> items = new ArrayList<>();
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "DRAFT";
        }
        if (invoiceDate == null) {
            invoiceDate = LocalDateTime.now();
        }
        if (tax == null) {
            tax = 0.0;
        }
        if (discount == null) {
            discount = 0.0;
        }
        if (paidAmount == null) {
            paidAmount = 0.0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (balance == null && totalAmount != null && paidAmount != null) {
            balance = totalAmount - paidAmount;
        }
    }
}

