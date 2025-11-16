package lk.nibm.kd.hdse252.pdsa_cw.services;

import lk.nibm.kd.hdse252.pdsa_cw.dto.ProductDTO;
import lk.nibm.kd.hdse252.pdsa_cw.entities.Product;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for advanced analytics and algorithms
 * Enhanced Functionality 1: Stock Prediction using Moving Average Algorithm
 * Enhanced Functionality 2: Product Recommendation using Similarity Algorithm
 */
@Service
public class ProductAnalyticsService {
    
    @Autowired
    private ProductRepository productRepository;
    
    // Store historical stock data for prediction
    private final Map<String, List<StockHistory>> stockHistoryMap = new HashMap<>();
    
    /**
     * Enhanced Functionality 1: Stock Prediction using Moving Average Algorithm
     * Predicts future stock requirements based on historical consumption patterns
     */
    public Map<String, Object> predictStockRequirements(String productCode, int days) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        List<StockHistory> history = stockHistoryMap.getOrDefault(productCode, new ArrayList<>());
        
        // If no history, use current stock and min level for prediction
        if (history.isEmpty()) {
            return createBasicPrediction(product, days);
        }
        
        // Calculate moving average of stock consumption
        double movingAverage = calculateMovingAverage(history, Math.min(7, history.size()));
        
        // Predict future stock requirements
        double predictedConsumption = movingAverage * days;
        int currentStock = product.getStock();
        int predictedStock = (int) (currentStock - predictedConsumption);
        int recommendedOrder = predictedStock < product.getMinStockLevel() 
                ? (int) (predictedConsumption + product.getMinStockLevel() - predictedStock)
                : 0;
        
        Map<String, Object> prediction = new HashMap<>();
        prediction.put("productCode", productCode);
        prediction.put("productName", product.getName());
        prediction.put("currentStock", currentStock);
        prediction.put("predictedStockAfter" + days + "Days", Math.max(0, predictedStock));
        prediction.put("predictedConsumption", String.format("%.2f", predictedConsumption));
        prediction.put("recommendedOrderQuantity", recommendedOrder);
        prediction.put("daysPredicted", days);
        prediction.put("predictionDate", LocalDateTime.now());
        prediction.put("algorithm", "Moving Average");
        
        return prediction;
    }
    
    /**
     * Calculate moving average from stock history
     */
    private double calculateMovingAverage(List<StockHistory> history, int period) {
        if (history.isEmpty()) {
            return 0.0;
        }
        
        int size = Math.min(period, history.size());
        double sum = 0.0;
        
        for (int i = history.size() - size; i < history.size(); i++) {
            sum += history.get(i).getConsumption();
        }
        
        return sum / size;
    }
    
    /**
     * Create basic prediction when no history is available
     */
    private Map<String, Object> createBasicPrediction(Product product, int days) {
        Map<String, Object> prediction = new HashMap<>();
        prediction.put("productCode", product.getProductCode());
        prediction.put("productName", product.getName());
        prediction.put("currentStock", product.getStock());
        prediction.put("predictedStockAfter" + days + "Days", 
                Math.max(0, product.getStock() - (product.getMinStockLevel() / 30 * days)));
        prediction.put("predictedConsumption", String.format("%.2f", (double) product.getMinStockLevel() / 30 * days));
        prediction.put("recommendedOrderQuantity", 
                product.getStock() <= product.getMinStockLevel() ? product.getMinStockLevel() * 2 : 0);
        prediction.put("daysPredicted", days);
        prediction.put("predictionDate", LocalDateTime.now());
        prediction.put("algorithm", "Basic Estimation (No History)");
        prediction.put("note", "Insufficient historical data. Using basic estimation.");
        
        return prediction;
    }
    
    /**
     * Record stock consumption for prediction algorithm
     */
    public void recordStockConsumption(String productCode, int quantityConsumed) {
        StockHistory history = new StockHistory(LocalDateTime.now(), quantityConsumed);
        stockHistoryMap.computeIfAbsent(productCode, k -> new ArrayList<>()).add(history);
        
        // Keep only last 30 days of history
        List<StockHistory> historyList = stockHistoryMap.get(productCode);
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        historyList.removeIf(h -> h.getDate().isBefore(cutoffDate));
    }
    
    /**
     * Enhanced Functionality 2: Product Recommendation using Similarity Algorithm
     * Recommends similar products based on category, wood type, and price range
     */
    public List<ProductDTO> recommendSimilarProducts(String productCode, int maxRecommendations) {
        Product targetProduct = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        List<Product> allProducts = productRepository.findAll();
        
        // Calculate similarity score for each product
        List<ProductSimilarity> similarities = new ArrayList<>();
        
        for (Product product : allProducts) {
            if (product.getId().equals(targetProduct.getId())) {
                continue; // Skip the product itself
            }
            
            double similarityScore = calculateSimilarity(targetProduct, product);
            similarities.add(new ProductSimilarity(product, similarityScore));
        }
        
        // Sort by similarity score (descending) using Quick Sort algorithm
        quickSort(similarities, 0, similarities.size() - 1);
        
        // Return top recommendations
        return similarities.stream()
                .limit(maxRecommendations)
                .map(ps -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(ps.getProduct().getId());
                    dto.setProductCode(ps.getProduct().getProductCode());
                    dto.setName(ps.getProduct().getName());
                    dto.setDescription(ps.getProduct().getDescription());
                    dto.setCategory(ps.getProduct().getCategory());
                    dto.setSellingPrice(ps.getProduct().getSellingPrice());
                    dto.setPrice(ps.getProduct().getSellingPrice());
                    dto.setStock(ps.getProduct().getStock());
                    dto.setStockQuantity(ps.getProduct().getStock());
                    dto.setItemType(ps.getProduct().getItemType());
                    dto.setFinishedType(ps.getProduct().getFinishedType());
                    dto.setIsCarved(ps.getProduct().getIsCarved());
                    dto.setCost(ps.getProduct().getCost());
                    dto.setWoodType(ps.getProduct().getWoodType());
                    dto.setDimensions(ps.getProduct().getDimensions());
                    dto.setWeight(ps.getProduct().getWeight());
                    dto.setSimilarityScore(ps.getSimilarityScore());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate similarity score between two products
     * Uses weighted scoring: category (40%), wood type (30%), price range (30%)
     */
    private double calculateSimilarity(Product product1, Product product2) {
        double score = 0.0;
        
        // Category match (40% weight)
        if (product1.getCategory().equalsIgnoreCase(product2.getCategory())) {
            score += 0.4;
        }
        
        // Wood type match (30% weight)
        if (product1.getWoodType().equalsIgnoreCase(product2.getWoodType())) {
            score += 0.3;
        }
        
        // Price similarity (30% weight) - within 20% price range
        double priceDiff = Math.abs(product1.getSellingPrice() - product2.getSellingPrice());
        double avgPrice = (product1.getSellingPrice() + product2.getSellingPrice()) / 2;
        if (avgPrice > 0) {
            double priceSimilarity = 1.0 - Math.min(1.0, priceDiff / (avgPrice * 0.2));
            score += priceSimilarity * 0.3;
        }
        
        return score;
    }
    
    /**
     * Quick Sort algorithm for sorting products by similarity
     */
    private void quickSort(List<ProductSimilarity> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }
    
    private int partition(List<ProductSimilarity> list, int low, int high) {
        double pivot = list.get(high).getSimilarityScore();
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (list.get(j).getSimilarityScore() >= pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
    
    /**
     * Get stock prediction for all products
     */
    public List<Map<String, Object>> predictAllProductsStock(int days) {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> predictStockRequirements(product.getProductCode(), days))
                .collect(Collectors.toList());
    }
    
    /**
     * Inner class for stock history
     */
    private static class StockHistory {
        private LocalDateTime date;
        private int consumption;
        
        public StockHistory(LocalDateTime date, int consumption) {
            this.date = date;
            this.consumption = consumption;
        }
        
        public LocalDateTime getDate() {
            return date;
        }
        
        public int getConsumption() {
            return consumption;
        }
    }
    
    /**
     * Inner class for product similarity
     */
    private static class ProductSimilarity {
        private Product product;
        private double similarityScore;
        
        public ProductSimilarity(Product product, double similarityScore) {
            this.product = product;
            this.similarityScore = similarityScore;
        }
        
        public Product getProduct() {
            return product;
        }
        
        public double getSimilarityScore() {
            return similarityScore;
        }
    }
}

