package lk.nibm.kd.hdse252.pdsa_cw.services;

import lk.nibm.kd.hdse252.pdsa_cw.dto.SalesOrderDTO;
import lk.nibm.kd.hdse252.pdsa_cw.dto.SalesOrderItemDTO;
import lk.nibm.kd.hdse252.pdsa_cw.entities.SalesOrder;
import lk.nibm.kd.hdse252.pdsa_cw.entities.SalesOrderItem;
import lk.nibm.kd.hdse252.pdsa_cw.entities.Product;
import lk.nibm.kd.hdse252.pdsa_cw.entities.Customer;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.SalesOrderRepository;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.ProductRepository;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.CustomerRepository;
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
public class SalesOrderService {
    
    @Autowired
    private SalesOrderRepository salesOrderRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    private String generateOrderNumber() {
        String prefix = "SO";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + timestamp;
    }
    
    public SalesOrderDTO createSalesOrder(SalesOrderDTO salesOrderDTO) {
        Customer customer = customerRepository.findById(salesOrderDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderNumber(generateOrderNumber());
        salesOrder.setCustomer(customer);
        salesOrder.setOrderDate(salesOrderDTO.getOrderDate() != null ? 
                salesOrderDTO.getOrderDate() : LocalDateTime.now());
        salesOrder.setExpectedDeliveryDate(salesOrderDTO.getExpectedDeliveryDate());
        salesOrder.setStatus("PENDING");
        salesOrder.setNotes(salesOrderDTO.getNotes());
        salesOrder.setTax(salesOrderDTO.getTax() != null ? salesOrderDTO.getTax() : 0.0);
        salesOrder.setDiscount(salesOrderDTO.getDiscount() != null ? salesOrderDTO.getDiscount() : 0.0);
        
        double subtotal = 0.0;
        
        if (salesOrderDTO.getItems() != null && !salesOrderDTO.getItems().isEmpty()) {
            for (SalesOrderItemDTO itemDTO : salesOrderDTO.getItems()) {
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));
                
                // Check stock availability
                if (product.getStock() < itemDTO.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getName() + 
                            ". Available: " + product.getStock() + ", Requested: " + itemDTO.getQuantity());
                }
                
                SalesOrderItem item = new SalesOrderItem();
                item.setSalesOrder(salesOrder);
                item.setProduct(product);
                item.setQuantity(itemDTO.getQuantity());
                item.setUnitPrice(itemDTO.getUnitPrice() != null ? 
                        itemDTO.getUnitPrice() : product.getSellingPrice());
                item.setTotalPrice(item.getQuantity() * item.getUnitPrice());
                
                salesOrder.getItems().add(item);
                subtotal += item.getTotalPrice();
            }
        }
        
        salesOrder.setSubtotal(subtotal);
        double totalAmount = subtotal + salesOrder.getTax() - salesOrder.getDiscount();
        salesOrder.setTotalAmount(totalAmount);
        
        SalesOrder savedOrder = salesOrderRepository.save(salesOrder);
        return convertToDTO(savedOrder);
    }
    
    public SalesOrderDTO updateSalesOrder(Long id, SalesOrderDTO salesOrderDTO) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales order not found"));
        
        salesOrder.setExpectedDeliveryDate(salesOrderDTO.getExpectedDeliveryDate());
        salesOrder.setStatus(salesOrderDTO.getStatus());
        salesOrder.setNotes(salesOrderDTO.getNotes());
        
        if (salesOrderDTO.getTax() != null) {
            salesOrder.setTax(salesOrderDTO.getTax());
        }
        if (salesOrderDTO.getDiscount() != null) {
            salesOrder.setDiscount(salesOrderDTO.getDiscount());
        }
        
        // Recalculate total
        double subtotal = salesOrder.getItems().stream()
                .mapToDouble(SalesOrderItem::getTotalPrice)
                .sum();
        salesOrder.setSubtotal(subtotal);
        salesOrder.setTotalAmount(subtotal + salesOrder.getTax() - salesOrder.getDiscount());
        
        SalesOrder updatedOrder = salesOrderRepository.save(salesOrder);
        return convertToDTO(updatedOrder);
    }
    
    public SalesOrderDTO confirmSalesOrder(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales order not found"));
        
        if ("CONFIRMED".equals(salesOrder.getStatus()) || "SHIPPED".equals(salesOrder.getStatus())) {
            throw new RuntimeException("Sales order is already confirmed or shipped");
        }
        
        // Deduct stock for each item
        for (SalesOrderItem item : salesOrder.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
        
        salesOrder.setStatus("CONFIRMED");
        SalesOrder confirmedOrder = salesOrderRepository.save(salesOrder);
        
        return convertToDTO(confirmedOrder);
    }
    
    public void deleteSalesOrder(Long id) {
        salesOrderRepository.deleteById(id);
    }
    
    public SalesOrderDTO getSalesOrderById(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales order not found"));
        return convertToDTO(salesOrder);
    }
    
    public List<SalesOrderDTO> getAllSalesOrders() {
        return salesOrderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<SalesOrderDTO> getSalesOrdersByCustomer(Long customerId) {
        return salesOrderRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private SalesOrderDTO convertToDTO(SalesOrder salesOrder) {
        SalesOrderDTO dto = new SalesOrderDTO();
        dto.setId(salesOrder.getId());
        dto.setOrderNumber(salesOrder.getOrderNumber());
        dto.setCustomerId(salesOrder.getCustomer().getId());
        dto.setCustomer(modelMapper.map(salesOrder.getCustomer(), lk.nibm.kd.hdse252.pdsa_cw.dto.CustomerDTO.class));
        dto.setOrderDate(salesOrder.getOrderDate());
        dto.setExpectedDeliveryDate(salesOrder.getExpectedDeliveryDate());
        dto.setStatus(salesOrder.getStatus());
        dto.setSubtotal(salesOrder.getSubtotal());
        dto.setTax(salesOrder.getTax());
        dto.setDiscount(salesOrder.getDiscount());
        dto.setTotalAmount(salesOrder.getTotalAmount());
        dto.setNotes(salesOrder.getNotes());
        dto.setCreatedAt(salesOrder.getCreatedAt());
        dto.setUpdatedAt(salesOrder.getUpdatedAt());
        
        if (salesOrder.getItems() != null) {
            dto.setItems(salesOrder.getItems().stream()
                    .map(item -> {
                        SalesOrderItemDTO itemDTO = new SalesOrderItemDTO();
                        itemDTO.setId(item.getId());
                        itemDTO.setSalesOrderId(item.getSalesOrder().getId());
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


