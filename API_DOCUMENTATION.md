# Wood Carving Inventory Management System - API Documentation

## Overview
This is a Spring Boot REST API backend for a Wood Carving Inventory Management System. The system implements various data structures and algorithms as required for the PDSA coursework.

## Data Structures Implemented

### 1. **Array** (`List<Product>`)
- Used to store all products in memory
- Provides O(1) access time for indexed operations
- Located in: `DataStructureService.productArray`

### 2. **Linked List** (`LinkedListNode`)
- Used to manage recently added items (FIFO with size limit of 10)
- Provides O(1) insertion at head
- Located in: `DataStructureService.recentItemsHead`

### 3. **Binary Search Tree** (`BSTNode`)
- Used for fast searching and sorting of products by product code
- Provides O(log n) average case for search and insertion
- Located in: `DataStructureService.bstRoot`

### 4. **Queue** (`QueueNode`)
- Used to manage restock requests (FIFO)
- Provides O(1) enqueue and dequeue operations
- Located in: `DataStructureService.restockQueueFront/Rear`

## Core Functionalities

### 1. Add Product
- **Endpoint**: `POST /api/products`
- **Description**: Adds a new product to the inventory
- **Data Structures Used**: Array, BST, Linked List
- **Request Body**: ProductDTO

### 2. Update Product
- **Endpoint**: `PUT /api/products/{id}`
- **Description**: Updates an existing product
- **Data Structures Used**: Array, BST

### 3. Delete Product
- **Endpoint**: `DELETE /api/products/{id}`
- **Description**: Deletes a product from inventory
- **Data Structures Used**: Array, BST

### 4. Search Product
- **Endpoint**: `GET /api/products/search/code/{productCode}`
- **Description**: Fast search using BST
- **Algorithm**: Binary Search Tree traversal
- **Time Complexity**: O(log n) average case

### 5. Advanced Search
- **Endpoint**: `POST /api/products/search`
- **Description**: Multi-criteria search with filters
- **Request Body**: ProductSearchDTO

### 6. Sort Products
- **Endpoint**: `GET /api/products/sorted`
- **Description**: Get products sorted by product code
- **Algorithm**: In-order BST traversal
- **Time Complexity**: O(n)

### 7. Get Recent Items
- **Endpoint**: `GET /api/products/recent`
- **Description**: Get recently added items (last 10)
- **Data Structure**: Linked List
- **Algorithm**: Linked List traversal

### 8. Restock Management
- **Get All Requests**: `GET /api/products/restock-requests`
- **Process Request**: `POST /api/products/restock-requests/process`
- **Data Structure**: Queue (FIFO)
- **Algorithm**: Queue enqueue/dequeue

## Enhanced Functionalities (New Algorithms)

### Enhanced Functionality 1: Stock Prediction using Moving Average Algorithm
- **Endpoint**: `GET /api/products/predict-stock/{productCode}?days=30`
- **Description**: Predicts future stock requirements based on historical consumption
- **Algorithm**: Moving Average
- **Time Complexity**: O(n) where n is the number of historical records
- **Features**:
  - Calculates moving average of stock consumption
  - Predicts stock levels for specified days
  - Recommends order quantities
  - Handles cases with insufficient historical data

**Additional Endpoints**:
- `GET /api/products/predict-stock/all?days=30` - Predict for all products
- `POST /api/products/record-consumption/{productCode}?quantityConsumed=10` - Record consumption

### Enhanced Functionality 2: Product Recommendation using Similarity Algorithm
- **Endpoint**: `GET /api/products/recommend/{productCode}?maxRecommendations=5`
- **Description**: Recommends similar products based on multiple criteria
- **Algorithm**: Similarity Scoring with Quick Sort
- **Time Complexity**: O(n log n) for sorting
- **Similarity Factors**:
  - Category match (40% weight)
  - Wood type match (30% weight)
  - Price similarity (30% weight)
- **Sorting Algorithm**: Quick Sort for efficient sorting by similarity score

## API Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/products` | Add new product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products` | Get all products |
| GET | `/api/products/search/code/{productCode}` | Search by product code (BST) |
| POST | `/api/products/search` | Advanced search |
| GET | `/api/products/sorted` | Get sorted products (BST) |
| GET | `/api/products/recent` | Get recent items (Linked List) |
| GET | `/api/products/low-stock` | Get low stock products |
| GET | `/api/products/restock-requests` | Get all restock requests (Queue) |
| POST | `/api/products/restock-requests/process` | Process next restock request |
| GET | `/api/products/predict-stock/{productCode}` | Predict stock requirements |
| GET | `/api/products/predict-stock/all` | Predict stock for all products |
| POST | `/api/products/record-consumption/{productCode}` | Record stock consumption |
| GET | `/api/products/recommend/{productCode}` | Get product recommendations |

## Request/Response Examples

### Add Product
```json
POST /api/products
{
  "productCode": "WC001",
  "name": "Hand Carved Buddha Statue",
  "description": "Beautiful hand-carved wooden Buddha statue",
  "category": "Statues",
  "price": 15000.00,
  "stockQuantity": 25,
  "minStockLevel": 10,
  "woodType": "Teak",
  "dimensions": "12x8x20 inches",
  "weight": 2.5
}
```

### Search Products
```json
POST /api/products/search
{
  "searchTerm": "Buddha",
  "category": "Statues",
  "woodType": "Teak",
  "minPrice": 10000.00,
  "maxPrice": 20000.00,
  "minStock": 5
}
```

### Stock Prediction Response
```json
{
  "productCode": "WC001",
  "productName": "Hand Carved Buddha Statue",
  "currentStock": 25,
  "predictedStockAfter30Days": 15,
  "predictedConsumption": "10.00",
  "recommendedOrderQuantity": 0,
  "daysPredicted": 30,
  "predictionDate": "2025-01-17T10:30:00",
  "algorithm": "Moving Average"
}
```

### Product Recommendation Response
```json
[
  {
    "id": 2,
    "productCode": "WC002",
    "name": "Carved Elephant Statue",
    "category": "Statues",
    "price": 14500.00,
    "woodType": "Teak",
    "similarityScore": 0.95
  }
]
```

## Database Schema

### Product Table
- `id` (Long, Primary Key)
- `product_code` (String, Unique)
- `name` (String)
- `description` (String)
- `category` (String)
- `price` (Double)
- `stock_quantity` (Integer)
- `min_stock_level` (Integer)
- `wood_type` (String)
- `dimensions` (String)
- `weight` (Double)
- `created_at` (LocalDateTime)
- `updated_at` (LocalDateTime)

## Algorithm Complexity Analysis

### Binary Search Tree Operations
- **Search**: O(log n) average, O(n) worst case
- **Insert**: O(log n) average, O(n) worst case
- **Delete**: O(log n) average, O(n) worst case
- **In-order Traversal (Sort)**: O(n)

### Linked List Operations
- **Insert at Head**: O(1)
- **Traversal**: O(n)

### Queue Operations
- **Enqueue**: O(1)
- **Dequeue**: O(1)
- **Peek**: O(1)

### Moving Average Algorithm
- **Time Complexity**: O(n) where n is the number of historical records
- **Space Complexity**: O(n) for storing history

### Quick Sort (Recommendation)
- **Time Complexity**: O(n log n) average, O(n²) worst case
- **Space Complexity**: O(log n) average

## Technologies Used
- Spring Boot 3.5.7
- Spring Data JPA
- MySQL Database
- Lombok
- ModelMapper
- Java 17

## Setup Instructions

1. Ensure MySQL is running
2. Update `application.properties` with your database credentials
3. Run the Spring Boot application
4. The database will be created automatically
5. Use the API endpoints to interact with the system

## Notes
- All timestamps are automatically managed by JPA
- Restock requests are automatically created when stock falls below minimum level
- The system maintains data structures in memory for fast operations
- Historical data for predictions is stored in memory (last 30 days)

