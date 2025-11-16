package lk.nibm.kd.hdse252.pdsa_cw.services;

import lk.nibm.kd.hdse252.pdsa_cw.dto.InvoiceDTO;
import lk.nibm.kd.hdse252.pdsa_cw.dto.InvoiceItemDTO;
import lk.nibm.kd.hdse252.pdsa_cw.entities.Invoice;
import lk.nibm.kd.hdse252.pdsa_cw.entities.InvoiceItem;
import lk.nibm.kd.hdse252.pdsa_cw.entities.SalesOrder;
import lk.nibm.kd.hdse252.pdsa_cw.entities.Customer;
import lk.nibm.kd.hdse252.pdsa_cw.entities.Product;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.InvoiceRepository;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.SalesOrderRepository;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.CustomerRepository;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceService {
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private SalesOrderRepository salesOrderRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    private String generateInvoiceNumber() {
        String prefix = "INV";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + timestamp;
    }
    
    public InvoiceDTO createInvoiceFromSalesOrder(Long salesOrderId, InvoiceDTO invoiceDTO) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new RuntimeException("Sales order not found"));
        
        if (!"CONFIRMED".equals(salesOrder.getStatus()) && !"SHIPPED".equals(salesOrder.getStatus())) {
            throw new RuntimeException("Sales order must be confirmed or shipped before creating invoice");
        }
        
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setSalesOrder(salesOrder);
        invoice.setCustomer(salesOrder.getCustomer());
        invoice.setInvoiceDate(invoiceDTO.getInvoiceDate() != null ? 
                invoiceDTO.getInvoiceDate() : LocalDateTime.now());
        invoice.setDueDate(invoiceDTO.getDueDate() != null ? 
                invoiceDTO.getDueDate() : LocalDateTime.now().plusDays(30));
        invoice.setStatus("DRAFT");
        invoice.setNotes(invoiceDTO.getNotes());
        invoice.setTax(invoiceDTO.getTax() != null ? invoiceDTO.getTax() : salesOrder.getTax());
        invoice.setDiscount(invoiceDTO.getDiscount() != null ? invoiceDTO.getDiscount() : salesOrder.getDiscount());
        invoice.setPaidAmount(0.0);
        
        double subtotal = 0.0;
        
        // Create invoice items from sales order items
        if (salesOrder.getItems() != null && !salesOrder.getItems().isEmpty()) {
            for (lk.nibm.kd.hdse252.pdsa_cw.entities.SalesOrderItem salesItem : salesOrder.getItems()) {
                InvoiceItem item = new InvoiceItem();
                item.setInvoice(invoice);
                item.setProduct(salesItem.getProduct());
                item.setQuantity(salesItem.getQuantity());
                item.setUnitPrice(salesItem.getUnitPrice());
                item.setTotalPrice(salesItem.getTotalPrice());
                
                invoice.getItems().add(item);
                subtotal += item.getTotalPrice();
            }
        }
        
        invoice.setSubtotal(subtotal);
        double totalAmount = subtotal + invoice.getTax() - invoice.getDiscount();
        invoice.setTotalAmount(totalAmount);
        invoice.setBalance(totalAmount);
        
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(savedInvoice);
    }
    
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        SalesOrder salesOrder = salesOrderRepository.findById(invoiceDTO.getSalesOrderId())
                .orElseThrow(() -> new RuntimeException("Sales order not found"));
        
        Customer customer = customerRepository.findById(invoiceDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setSalesOrder(salesOrder);
        invoice.setCustomer(customer);
        invoice.setInvoiceDate(invoiceDTO.getInvoiceDate() != null ? 
                invoiceDTO.getInvoiceDate() : LocalDateTime.now());
        invoice.setDueDate(invoiceDTO.getDueDate() != null ? 
                invoiceDTO.getDueDate() : LocalDateTime.now().plusDays(30));
        invoice.setStatus("DRAFT");
        invoice.setNotes(invoiceDTO.getNotes());
        invoice.setTax(invoiceDTO.getTax() != null ? invoiceDTO.getTax() : 0.0);
        invoice.setDiscount(invoiceDTO.getDiscount() != null ? invoiceDTO.getDiscount() : 0.0);
        invoice.setPaidAmount(0.0);
        
        double subtotal = 0.0;
        
        if (invoiceDTO.getItems() != null && !invoiceDTO.getItems().isEmpty()) {
            for (InvoiceItemDTO itemDTO : invoiceDTO.getItems()) {
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));
                
                InvoiceItem item = new InvoiceItem();
                item.setInvoice(invoice);
                item.setProduct(product);
                item.setQuantity(itemDTO.getQuantity());
                item.setUnitPrice(itemDTO.getUnitPrice());
                item.setTotalPrice(itemDTO.getQuantity() * itemDTO.getUnitPrice());
                
                invoice.getItems().add(item);
                subtotal += item.getTotalPrice();
            }
        }
        
        invoice.setSubtotal(subtotal);
        double totalAmount = subtotal + invoice.getTax() - invoice.getDiscount();
        invoice.setTotalAmount(totalAmount);
        invoice.setBalance(totalAmount);
        
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(savedInvoice);
    }
    
    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        invoice.setDueDate(invoiceDTO.getDueDate());
        invoice.setStatus(invoiceDTO.getStatus());
        invoice.setNotes(invoiceDTO.getNotes());
        
        if (invoiceDTO.getTax() != null) {
            invoice.setTax(invoiceDTO.getTax());
        }
        if (invoiceDTO.getDiscount() != null) {
            invoice.setDiscount(invoiceDTO.getDiscount());
        }
        if (invoiceDTO.getPaidAmount() != null) {
            invoice.setPaidAmount(invoiceDTO.getPaidAmount());
            invoice.setBalance(invoice.getTotalAmount() - invoice.getPaidAmount());
        }
        
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(updatedInvoice);
    }
    
    public InvoiceDTO recordPayment(Long id, Double paymentAmount) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        if ("PAID".equals(invoice.getStatus())) {
            throw new RuntimeException("Invoice is already paid");
        }
        
        invoice.setPaidAmount(invoice.getPaidAmount() + paymentAmount);
        invoice.setBalance(invoice.getTotalAmount() - invoice.getPaidAmount());
        
        if (invoice.getBalance() <= 0) {
            invoice.setStatus("PAID");
        } else {
            invoice.setStatus("ISSUED");
        }
        
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(updatedInvoice);
    }
    
    public InvoiceDTO issueInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        invoice.setStatus("ISSUED");
        Invoice issuedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(issuedInvoice);
    }
    
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
    
    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        return convertToDTO(invoice);
    }
    
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<InvoiceDTO> getInvoicesByCustomer(Long customerId) {
        return invoiceRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<InvoiceDTO> getInvoicesBySalesOrder(Long salesOrderId) {
        return invoiceRepository.findBySalesOrderId(salesOrderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private InvoiceDTO convertToDTO(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setSalesOrderId(invoice.getSalesOrder().getId());
        dto.setSalesOrder(modelMapper.map(invoice.getSalesOrder(), lk.nibm.kd.hdse252.pdsa_cw.dto.SalesOrderDTO.class));
        dto.setCustomerId(invoice.getCustomer().getId());
        dto.setCustomer(modelMapper.map(invoice.getCustomer(), lk.nibm.kd.hdse252.pdsa_cw.dto.CustomerDTO.class));
        dto.setInvoiceDate(invoice.getInvoiceDate());
        dto.setDueDate(invoice.getDueDate());
        dto.setStatus(invoice.getStatus());
        dto.setSubtotal(invoice.getSubtotal());
        dto.setTax(invoice.getTax());
        dto.setDiscount(invoice.getDiscount());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setPaidAmount(invoice.getPaidAmount());
        dto.setBalance(invoice.getBalance());
        dto.setNotes(invoice.getNotes());
        dto.setCreatedAt(invoice.getCreatedAt());
        dto.setUpdatedAt(invoice.getUpdatedAt());
        
        if (invoice.getItems() != null) {
            dto.setItems(invoice.getItems().stream()
                    .map(item -> {
                        InvoiceItemDTO itemDTO = new InvoiceItemDTO();
                        itemDTO.setId(item.getId());
                        itemDTO.setInvoiceId(item.getInvoice().getId());
                        itemDTO.setProductId(item.getProduct().getId());
                        itemDTO.setQuantity(item.getQuantity());
                        itemDTO.setUnitPrice(item.getUnitPrice());
                        itemDTO.setTotalPrice(item.getTotalPrice());
                        return itemDTO;
                    })
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
}

