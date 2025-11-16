package lk.nibm.kd.hdse252.pdsa_cw.controllers;

import lk.nibm.kd.hdse252.pdsa_cw.dto.ProductDTO;
import lk.nibm.kd.hdse252.pdsa_cw.dto.ProductSearchDTO;
import lk.nibm.kd.hdse252.pdsa_cw.dto.RestockRequestDTO;
import lk.nibm.kd.hdse252.pdsa_cw.services.ProductAnalyticsService;
import lk.nibm.kd.hdse252.pdsa_cw.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductAnalyticsService analyticsService;
    
    /**
     * Add a new product
     */
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        try {
            ProductDTO createdProduct = productService.addProduct(productDTO);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Update an existing product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Delete a product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        try {
            ProductDTO product = productService.getProductById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Get all products
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    /**
     * Search product by product code (using BST)
     */
    @GetMapping("/search/code/{productCode}")
    public ResponseEntity<ProductDTO> searchProductByCode(@PathVariable String productCode) {
        try {
            ProductDTO product = productService.searchProductByCode(productCode);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Advanced search with multiple criteria
     */
    @PostMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestBody ProductSearchDTO searchDTO) {
        List<ProductDTO> products = productService.searchProducts(searchDTO);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    /**
     * Get sorted products (using BST - sorted by product code)
     */
    @GetMapping("/sorted")
    public ResponseEntity<List<ProductDTO>> getSortedProducts() {
        List<ProductDTO> products = productService.getSortedProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    /**
     * Get recently added items (from Linked List)
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ProductDTO>> getRecentItems() {
        List<ProductDTO> recentProducts = productService.getRecentItems();
        return new ResponseEntity<>(recentProducts, HttpStatus.OK);
    }
    
    /**
     * Get low stock products
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDTO>> getLowStockProducts() {
        List<ProductDTO> lowStockProducts = productService.getLowStockProducts();
        return new ResponseEntity<>(lowStockProducts, HttpStatus.OK);
    }
    
    /**
     * Get all restock requests (from Queue)
     */
    @GetMapping("/restock-requests")
    public ResponseEntity<List<RestockRequestDTO>> getAllRestockRequests() {
        List<RestockRequestDTO> requests = productService.getAllRestockRequests();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }
    
    /**
     * Process next restock request (dequeue from Queue)
     */
    @PostMapping("/restock-requests/process")
    public ResponseEntity<RestockRequestDTO> processNextRestockRequest() {
        try {
            RestockRequestDTO processedRequest = productService.processNextRestockRequest();
            return new ResponseEntity<>(processedRequest, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Enhanced Functionality 1: Stock Prediction using Moving Average Algorithm
     */
    @GetMapping("/predict-stock/{productCode}")
    public ResponseEntity<Map<String, Object>> predictStockRequirements(
            @PathVariable String productCode,
            @RequestParam(defaultValue = "30") int days) {
        try {
            Map<String, Object> prediction = analyticsService.predictStockRequirements(productCode, days);
            return new ResponseEntity<>(prediction, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Predict stock for all products
     */
    @GetMapping("/predict-stock/all")
    public ResponseEntity<List<Map<String, Object>>> predictAllProductsStock(
            @RequestParam(defaultValue = "30") int days) {
        List<Map<String, Object>> predictions = analyticsService.predictAllProductsStock(days);
        return new ResponseEntity<>(predictions, HttpStatus.OK);
    }
    
    /**
     * Record stock consumption for prediction algorithm
     */
    @PostMapping("/record-consumption/{productCode}")
    public ResponseEntity<String> recordStockConsumption(
            @PathVariable String productCode,
            @RequestParam int quantityConsumed) {
        try {
            analyticsService.recordStockConsumption(productCode, quantityConsumed);
            return new ResponseEntity<>("Stock consumption recorded successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error recording consumption", HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Enhanced Functionality 2: Product Recommendation using Similarity Algorithm
     */
    @GetMapping("/recommend/{productCode}")
    public ResponseEntity<List<ProductDTO>> recommendSimilarProducts(
            @PathVariable String productCode,
            @RequestParam(defaultValue = "5") int maxRecommendations) {
        try {
            List<ProductDTO> recommendations = analyticsService.recommendSimilarProducts(productCode, maxRecommendations);
            return new ResponseEntity<>(recommendations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}

