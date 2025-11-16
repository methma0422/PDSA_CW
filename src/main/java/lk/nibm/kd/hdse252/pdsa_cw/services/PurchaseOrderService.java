package lk.nibm.kd.hdse252.pdsa_cw.services;

import lk.nibm.kd.hdse252.pdsa_cw.dto.PurchaseOrderDTO;
import lk.nibm.kd.hdse252.pdsa_cw.dto.PurchaseOrderItemDTO;
import lk.nibm.kd.hdse252.pdsa_cw.entities.PurchaseOrder;
import lk.nibm.kd.hdse252.pdsa_cw.entities.PurchaseOrderItem;
import lk.nibm.kd.hdse252.pdsa_cw.entities.Product;
import lk.nibm.kd.hdse252.pdsa_cw.entities.Supplier;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.PurchaseOrderRepository;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.ProductRepository;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.SupplierRepository;
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
public class PurchaseOrderService {
    
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    private String generateOrderNumber() {
        String prefix = "PO";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + timestamp;
    }
    
    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) {
        Supplier supplier = supplierRepository.findById(purchaseOrderDTO.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderNumber(generateOrderNumber());
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setOrderDate(purchaseOrderDTO.getOrderDate() != null ? 
                purchaseOrderDTO.getOrderDate() : LocalDateTime.now());
        purchaseOrder.setExpectedDeliveryDate(purchaseOrderDTO.getExpectedDeliveryDate());
        purchaseOrder.setStatus("PENDING");
        purchaseOrder.setNotes(purchaseOrderDTO.getNotes());
        
        double totalAmount = 0.0;
        
        if (purchaseOrderDTO.getItems() != null && !purchaseOrderDTO.getItems().isEmpty()) {
            for (PurchaseOrderItemDTO itemDTO : purchaseOrderDTO.getItems()) {
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));
                
                PurchaseOrderItem item = new PurchaseOrderItem();
                item.setPurchaseOrder(purchaseOrder);
                item.setProduct(product);
                item.setQuantity(itemDTO.getQuantity());
                item.setUnitPrice(itemDTO.getUnitPrice());
                item.setTotalPrice(itemDTO.getQuantity() * itemDTO.getUnitPrice());
                
                purchaseOrder.getItems().add(item);
                totalAmount += item.getTotalPrice();
            }
        }
        
        purchaseOrder.setTotalAmount(totalAmount);
        PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);
        return convertToDTO(savedOrder);
    }
    
    public PurchaseOrderDTO updatePurchaseOrder(Long id, PurchaseOrderDTO purchaseOrderDTO) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found"));
        
        purchaseOrder.setExpectedDeliveryDate(purchaseOrderDTO.getExpectedDeliveryDate());
        purchaseOrder.setStatus(purchaseOrderDTO.getStatus());
        purchaseOrder.setNotes(purchaseOrderDTO.getNotes());
        
        PurchaseOrder updatedOrder = purchaseOrderRepository.save(purchaseOrder);
        return convertToDTO(updatedOrder);
    }
    
    public void deletePurchaseOrder(Long id) {
        purchaseOrderRepository.deleteById(id);
    }
    
    public PurchaseOrderDTO getPurchaseOrderById(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found"));
        return convertToDTO(purchaseOrder);
    }
    
    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private PurchaseOrderDTO convertToDTO(PurchaseOrder purchaseOrder) {
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setId(purchaseOrder.getId());
        dto.setOrderNumber(purchaseOrder.getOrderNumber());
        dto.setSupplierId(purchaseOrder.getSupplier().getId());
        dto.setSupplier(modelMapper.map(purchaseOrder.getSupplier(), lk.nibm.kd.hdse252.pdsa_cw.dto.SupplierDTO.class));
        dto.setOrderDate(purchaseOrder.getOrderDate());
        dto.setExpectedDeliveryDate(purchaseOrder.getExpectedDeliveryDate());
        dto.setStatus(purchaseOrder.getStatus());
        dto.setTotalAmount(purchaseOrder.getTotalAmount());
        dto.setNotes(purchaseOrder.getNotes());
        dto.setCreatedAt(purchaseOrder.getCreatedAt());
        dto.setUpdatedAt(purchaseOrder.getUpdatedAt());
        
        if (purchaseOrder.getItems() != null) {
            dto.setItems(purchaseOrder.getItems().stream()
                    .map(item -> {
                        PurchaseOrderItemDTO itemDTO = new PurchaseOrderItemDTO();
                        itemDTO.setId(item.getId());
                        itemDTO.setPurchaseOrderId(item.getPurchaseOrder().getId());
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


