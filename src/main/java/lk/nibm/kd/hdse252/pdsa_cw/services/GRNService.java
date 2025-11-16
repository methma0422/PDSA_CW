package lk.nibm.kd.hdse252.pdsa_cw.services;

import lk.nibm.kd.hdse252.pdsa_cw.dto.GRNDTO;
import lk.nibm.kd.hdse252.pdsa_cw.dto.GRNItemDTO;
import lk.nibm.kd.hdse252.pdsa_cw.entities.GRN;
import lk.nibm.kd.hdse252.pdsa_cw.entities.GRNItem;
import lk.nibm.kd.hdse252.pdsa_cw.entities.PurchaseOrder;
import lk.nibm.kd.hdse252.pdsa_cw.entities.Product;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.GRNRepository;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.PurchaseOrderRepository;
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
public class GRNService {
    
    @Autowired
    private GRNRepository grnRepository;
    
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    private String generateGRNNumber() {
        String prefix = "GRN";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + timestamp;
    }
    
    public GRNDTO createGRN(GRNDTO grnDTO) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(grnDTO.getPurchaseOrderId())
                .orElseThrow(() -> new RuntimeException("Purchase order not found"));
        
        GRN grn = new GRN();
        grn.setGrnNumber(generateGRNNumber());
        grn.setPurchaseOrder(purchaseOrder);
        grn.setReceivedDate(grnDTO.getReceivedDate() != null ? 
                grnDTO.getReceivedDate() : LocalDateTime.now());
        grn.setStatus("PENDING");
        grn.setNotes(grnDTO.getNotes());
        
        if (grnDTO.getItems() != null && !grnDTO.getItems().isEmpty()) {
            for (GRNItemDTO itemDTO : grnDTO.getItems()) {
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));
                
                GRNItem item = new GRNItem();
                item.setGrn(grn);
                item.setProduct(product);
                item.setOrderedQuantity(itemDTO.getOrderedQuantity());
                item.setReceivedQuantity(itemDTO.getReceivedQuantity());
                item.setUnitPrice(itemDTO.getUnitPrice());
                item.setTotalPrice(itemDTO.getReceivedQuantity() * itemDTO.getUnitPrice());
                
                grn.getItems().add(item);
            }
        }
        
        GRN savedGRN = grnRepository.save(grn);
        return convertToDTO(savedGRN);
    }
    
    public GRNDTO updateGRN(Long id, GRNDTO grnDTO) {
        GRN grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found"));
        
        grn.setReceivedDate(grnDTO.getReceivedDate());
        grn.setStatus(grnDTO.getStatus());
        grn.setNotes(grnDTO.getNotes());
        
        GRN updatedGRN = grnRepository.save(grn);
        return convertToDTO(updatedGRN);
    }
    
    public GRNDTO completeGRN(Long id) {
        GRN grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found"));
        
        if ("COMPLETED".equals(grn.getStatus())) {
            throw new RuntimeException("GRN is already completed");
        }
        
        // Update product stock for each item
        for (GRNItem item : grn.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getReceivedQuantity());
            productRepository.save(product);
        }
        
        grn.setStatus("COMPLETED");
        GRN completedGRN = grnRepository.save(grn);
        
        // Update purchase order status
        PurchaseOrder purchaseOrder = grn.getPurchaseOrder();
        purchaseOrder.setStatus("RECEIVED");
        purchaseOrderRepository.save(purchaseOrder);
        
        return convertToDTO(completedGRN);
    }
    
    public void deleteGRN(Long id) {
        grnRepository.deleteById(id);
    }
    
    public GRNDTO getGRNById(Long id) {
        GRN grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found"));
        return convertToDTO(grn);
    }
    
    public List<GRNDTO> getAllGRNs() {
        return grnRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<GRNDTO> getGRNsByPurchaseOrder(Long purchaseOrderId) {
        return grnRepository.findByPurchaseOrderId(purchaseOrderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private GRNDTO convertToDTO(GRN grn) {
        GRNDTO dto = new GRNDTO();
        dto.setId(grn.getId());
        dto.setGrnNumber(grn.getGrnNumber());
        dto.setPurchaseOrderId(grn.getPurchaseOrder().getId());
        dto.setPurchaseOrder(modelMapper.map(grn.getPurchaseOrder(), lk.nibm.kd.hdse252.pdsa_cw.dto.PurchaseOrderDTO.class));
        dto.setReceivedDate(grn.getReceivedDate());
        dto.setStatus(grn.getStatus());
        dto.setNotes(grn.getNotes());
        dto.setCreatedAt(grn.getCreatedAt());
        dto.setUpdatedAt(grn.getUpdatedAt());
        
        if (grn.getItems() != null) {
            dto.setItems(grn.getItems().stream()
                    .map(item -> {
                        GRNItemDTO itemDTO = new GRNItemDTO();
                        itemDTO.setId(item.getId());
                        itemDTO.setGrnId(item.getGrn().getId());
                        itemDTO.setProductId(item.getProduct().getId());
                        itemDTO.setOrderedQuantity(item.getOrderedQuantity());
                        itemDTO.setReceivedQuantity(item.getReceivedQuantity());
                        itemDTO.setUnitPrice(item.getUnitPrice());
                        itemDTO.setTotalPrice(item.getTotalPrice());
                        return itemDTO;
                    })
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
}

