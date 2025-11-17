package lk.nibm.kd.hdse252.pdsa_cw.services;

import lk.nibm.kd.hdse252.pdsa_cw.dto.ProductDTO;
import lk.nibm.kd.hdse252.pdsa_cw.dto.ProductSearchDTO;
import lk.nibm.kd.hdse252.pdsa_cw.dto.RestockRequestDTO;
import lk.nibm.kd.hdse252.pdsa_cw.entities.Product;
import lk.nibm.kd.hdse252.pdsa_cw.entities.RestockRequest;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private DataStructureService dataStructureService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    /**
     * Add a new product
     */
    public ProductDTO addProduct(ProductDTO productDTO) {
        // Check if product code already exists
        if (productRepository.findByProductCode(productDTO.getProductCode()).isPresent()) {
            throw new RuntimeException("Product with code " + productDTO.getProductCode() + " already exists");
        }
        
        Product product = new Product();
        product.setProductCode(productDTO.getProductCode());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
        product.setItemType(productDTO.getItemType());
        product.setWoodType(productDTO.getWoodType());
        product.setFinishedType(productDTO.getFinishedType());
        product.setIsCarved(productDTO.getIsCarved());
        product.setCost(productDTO.getCost());
        
        if (productDTO.getSellingPrice() != null) {
            product.setSellingPrice(productDTO.getSellingPrice());
        } else if (productDTO.getPrice() != null) {
            product.setSellingPrice(productDTO.getPrice());
        }
        // keep legacy price column in sync
        product.setPrice(product.getSellingPrice());
        
        if (productDTO.getStock() != null) {
            product.setStock(productDTO.getStock());
        } else if (productDTO.getStockQuantity() != null) {
            product.setStock(productDTO.getStockQuantity());
        }
        // keep legacy stock_quantity column in sync
        product.setStockQuantityLegacy(product.getStock());
        
        product.setMinStockLevel(productDTO.getMinStockLevel());
        product.setDimensions(productDTO.getDimensions());
        product.setWeight(productDTO.getWeight());
        
        Product savedProduct = productRepository.save(product);
        
        // Add to data structures
        dataStructureService.addToArray(savedProduct);
        dataStructureService.insertIntoBST(savedProduct);
        dataStructureService.addToRecentItems(savedProduct);
        
        // Check if restock is needed
        if (savedProduct.getStock() <= savedProduct.getMinStockLevel()) {
            createRestockRequest(savedProduct);
        }
        
        return convertToDTO(savedProduct);
    }
    
    /**
     * Convert Product entity to ProductDTO
     */
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setProductCode(product.getProductCode());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setItemType(product.getItemType());
        dto.setWoodType(product.getWoodType());
        dto.setFinishedType(product.getFinishedType());
        dto.setIsCarved(product.getIsCarved());
        dto.setCost(product.getCost());
        dto.setSellingPrice(product.getSellingPrice());
        dto.setPrice(product.getSellingPrice()); // For backward compatibility
        dto.setStock(product.getStock());
        dto.setStockQuantity(product.getStock()); // For backward compatibility
        dto.setMinStockLevel(product.getMinStockLevel());
        dto.setDimensions(product.getDimensions());
        dto.setWeight(product.getWeight());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
    
    /**
     * Update an existing product
     */
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Update fields
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setItemType(productDTO.getItemType());
        existingProduct.setWoodType(productDTO.getWoodType());
        existingProduct.setFinishedType(productDTO.getFinishedType());
        existingProduct.setIsCarved(productDTO.getIsCarved());
        existingProduct.setCost(productDTO.getCost());
        
        if (productDTO.getSellingPrice() != null) {
            existingProduct.setSellingPrice(productDTO.getSellingPrice());
        } else if (productDTO.getPrice() != null) {
            existingProduct.setSellingPrice(productDTO.getPrice());
        }
        // keep legacy price column in sync
        existingProduct.setPrice(existingProduct.getSellingPrice());
        
        if (productDTO.getStock() != null) {
            existingProduct.setStock(productDTO.getStock());
        } else if (productDTO.getStockQuantity() != null) {
            existingProduct.setStock(productDTO.getStockQuantity());
        }
        // keep legacy stock_quantity column in sync
        existingProduct.setStockQuantityLegacy(existingProduct.getStock());
        
        existingProduct.setMinStockLevel(productDTO.getMinStockLevel());
        existingProduct.setDimensions(productDTO.getDimensions());
        existingProduct.setWeight(productDTO.getWeight());
        
        Product updatedProduct = productRepository.save(existingProduct);
        
        // Update in data structures
        dataStructureService.updateInArray(updatedProduct);
        dataStructureService.removeFromBST(existingProduct.getProductCode());
        dataStructureService.insertIntoBST(updatedProduct);
        
        // Check if restock is needed
        if (updatedProduct.getStock() <= updatedProduct.getMinStockLevel()) {
            createRestockRequest(updatedProduct);
        }
        
        return convertToDTO(updatedProduct);
    }
    
    /**
     * Delete a product
     */
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        productRepository.deleteById(id);
        
        // Remove from data structures
        dataStructureService.removeFromArray(id);
        dataStructureService.removeFromBST(product.getProductCode());
    }
    
    /**
     * Get product by ID
     */
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToDTO(product);
    }
    
    /**
     * Get all products
     */
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        
        // Sync with data structures
        dataStructureService.clearAll();
        for (Product product : products) {
            dataStructureService.addToArray(product);
            dataStructureService.insertIntoBST(product);
        }
        
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Search products using BST (fast search by product code)
     */
    public ProductDTO searchProductByCode(String productCode) {
        Product product = dataStructureService.searchInBST(productCode);
        if (product == null) {
            product = productRepository.findByProductCode(productCode)
                    .orElseThrow(() -> new RuntimeException("Product not found with code: " + productCode));
        }
        return convertToDTO(product);
    }
    
    /**
     * Advanced search with multiple criteria
     */
    public List<ProductDTO> searchProducts(ProductSearchDTO searchDTO) {
        List<Product> products;
        
        if (searchDTO.getSearchTerm() != null && !searchDTO.getSearchTerm().isEmpty()) {
            products = productRepository.searchProducts(searchDTO.getSearchTerm());
        } else {
            products = productRepository.findAll();
        }
        
        // Apply filters
        if (searchDTO.getCategory() != null && !searchDTO.getCategory().isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getCategory().equalsIgnoreCase(searchDTO.getCategory()))
                    .collect(Collectors.toList());
        }
        
        if (searchDTO.getWoodType() != null && !searchDTO.getWoodType().isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getWoodType().equalsIgnoreCase(searchDTO.getWoodType()))
                    .collect(Collectors.toList());
        }
        
        if (searchDTO.getMinPrice() != null && searchDTO.getMaxPrice() != null) {
            products = products.stream()
                    .filter(p -> p.getSellingPrice() >= searchDTO.getMinPrice() && p.getSellingPrice() <= searchDTO.getMaxPrice())
                    .collect(Collectors.toList());
        }
        
        if (searchDTO.getMinStock() != null) {
            products = products.stream()
                    .filter(p -> p.getStock() >= searchDTO.getMinStock())
                    .collect(Collectors.toList());
        }
        
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Sort products using BST (sorted by product code)
     */
    public List<ProductDTO> getSortedProducts() {
        List<Product> sortedProducts = dataStructureService.getSortedFromBST();
        return sortedProducts.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }
    
    /**
     * Get recently added items from Linked List
     * If linked list is empty, returns most recent products from database
     */
    public List<ProductDTO> getRecentItems() {
        List<Product> recentProducts = dataStructureService.getRecentItems();
        
        // If linked list is empty, get most recent products from database
        if (recentProducts.isEmpty()) {
            recentProducts = productRepository.findAll().stream()
                    .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                    .limit(10)
                    .collect(Collectors.toList());
        }
        
        return recentProducts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Create restock request and add to queue
     */
    private void createRestockRequest(Product product) {
        String priority = determinePriority(product);
        int requestedQuantity = calculateRestockQuantity(product);
        
        RestockRequest request = new RestockRequest(
                product.getProductCode(),
                product.getName(),
                requestedQuantity,
                product.getStock(),
                priority
        );
        
        dataStructureService.enqueueRestockRequest(request);
    }
    
    /**
     * Determine priority based on stock level
     */
    private String determinePriority(Product product) {
        double stockPercentage = (double) product.getStock() / product.getMinStockLevel();
        if (stockPercentage <= 0.5) {
            return "HIGH";
        } else if (stockPercentage <= 0.75) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    /**
     * Calculate restock quantity (2x min stock level)
     */
    private int calculateRestockQuantity(Product product) {
        return product.getMinStockLevel() * 2;
    }
    
    /**
     * Get all restock requests from queue
     */
    public List<RestockRequestDTO> getAllRestockRequests() {
        List<RestockRequest> requests = dataStructureService.getAllRestockRequests();
        return requests.stream()
                .map(request -> {
                    RestockRequestDTO dto = new RestockRequestDTO();
                    dto.setProductCode(request.getProductCode());
                    dto.setProductName(request.getProductName());
                    dto.setRequestedQuantity(request.getRequestedQuantity());
                    dto.setCurrentStock(request.getCurrentStock());
                    dto.setPriority(request.getPriority());
                    dto.setRequestedAt(request.getRequestedAt());
                    dto.setStatus(request.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Process next restock request from queue
     */
    public RestockRequestDTO processNextRestockRequest() {
        RestockRequest request = dataStructureService.dequeueRestockRequest();
        if (request == null) {
            throw new RuntimeException("No restock requests in queue");
        }
        
        request.setStatus("PROCESSED");
        
        // Update product stock
        Product product = productRepository.findByProductCode(request.getProductCode())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStock(product.getStock() + request.getRequestedQuantity());
        productRepository.save(product);
        
        RestockRequestDTO dto = new RestockRequestDTO();
        dto.setProductCode(request.getProductCode());
        dto.setProductName(request.getProductName());
        dto.setRequestedQuantity(request.getRequestedQuantity());
        dto.setCurrentStock(request.getCurrentStock());
        dto.setPriority(request.getPriority());
        dto.setRequestedAt(request.getRequestedAt());
        dto.setStatus(request.getStatus());
        
        return dto;
    }
    
    /**
     * Get low stock products
     */
    public List<ProductDTO> getLowStockProducts() {
        List<Product> lowStockProducts = productRepository.findLowStockProducts();
        return lowStockProducts.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }
}

