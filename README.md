# Wood Carving Inventory Management System - Backend

## Project Overview
This is a Spring Boot REST API backend for a Wood Carving Inventory Management System developed as part of the Programming Data Structures and Algorithms (PDSA) coursework. The system demonstrates practical implementation of various data structures and algorithms in a real-world business context.

## Features

### Core Functionalities
1. **Product Management**
   - Add, Update, Delete products
   - Search products by code (using BST)
   - Advanced multi-criteria search
   - Sort products (using BST in-order traversal)

2. **Stock Management**
   - Track stock quantities
   - Monitor low stock levels
   - Automatic restock request generation

3. **Data Structure Implementations**
   - **Array**: Store all products
   - **Linked List**: Manage recently added items (last 10)
   - **Binary Search Tree**: Fast searching and sorting by product code
   - **Queue**: Manage restock requests (FIFO)

### Enhanced Functionalities (New Algorithms)

#### 1. Stock Prediction using Moving Average Algorithm
- Predicts future stock requirements based on historical consumption patterns
- Calculates moving average of stock consumption
- Recommends order quantities
- Handles cases with insufficient historical data

#### 2. Product Recommendation using Similarity Algorithm
- Recommends similar products based on:
  - Category match (40% weight)
  - Wood type match (30% weight)
  - Price similarity (30% weight)
- Uses Quick Sort algorithm for efficient sorting by similarity score

## Technology Stack
- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **Libraries**: Lombok, ModelMapper

## Project Structure

```
src/main/java/lk/nibm/kd/hdse252/pdsa_cw/
├── config/
│   └── ModelMapperConfig.java          # ModelMapper bean configuration
├── controllers/
│   └── ProductController.java          # REST API endpoints
├── dto/
│   ├── ProductDTO.java                 # Product data transfer object
│   ├── ProductSearchDTO.java           # Search criteria DTO
│   └── RestockRequestDTO.java          # Restock request DTO
├── entities/
│   ├── Product.java                    # Product entity (JPA)
│   ├── BSTNode.java                    # Binary Search Tree node
│   ├── LinkedListNode.java             # Linked List node
│   ├── QueueNode.java                  # Queue node
│   └── RestockRequest.java             # Restock request entity
├── repositories/
│   └── ProductRepository.java          # JPA repository
├── services/
│   ├── DataStructureService.java       # Custom data structures management
│   ├── ProductService.java             # Business logic for products
│   └── ProductAnalyticsService.java    # Analytics and algorithms
└── PdsaCwApplication.java              # Main application class
```

## Data Structures Implementation

### 1. Array (ArrayList)
- **Purpose**: Store all products in memory
- **Operations**: Add, Remove, Update, Get All
- **Time Complexity**: O(1) for indexed access, O(n) for search

### 2. Binary Search Tree (BST)
- **Purpose**: Fast searching and sorting by product code
- **Operations**: Insert, Search, Delete, In-order Traversal
- **Time Complexity**: 
  - Search: O(log n) average, O(n) worst case
  - Insert: O(log n) average, O(n) worst case
  - Sort (in-order): O(n)

### 3. Linked List
- **Purpose**: Manage recently added items (FIFO with size limit)
- **Operations**: Insert at head, Traverse
- **Time Complexity**: O(1) for insertion, O(n) for traversal
- **Size Limit**: 10 most recent items

### 4. Queue
- **Purpose**: Manage restock requests (FIFO)
- **Operations**: Enqueue, Dequeue, Peek
- **Time Complexity**: O(1) for all operations

## Algorithms Implemented

### 1. Binary Search Tree Operations
- **Search Algorithm**: Recursive binary search
- **Insert Algorithm**: Recursive insertion maintaining BST property
- **Delete Algorithm**: Handles three cases (no children, one child, two children)
- **Sort Algorithm**: In-order traversal

### 2. Moving Average Algorithm (Stock Prediction)
- **Purpose**: Predict future stock requirements
- **Method**: Calculate moving average of historical consumption
- **Time Complexity**: O(n) where n is historical records
- **Space Complexity**: O(n) for storing history

### 3. Similarity Algorithm (Product Recommendation)
- **Purpose**: Find similar products
- **Method**: Weighted scoring based on multiple criteria
- **Sorting**: Quick Sort algorithm
- **Time Complexity**: O(n log n) for sorting

### 4. Quick Sort Algorithm
- **Purpose**: Sort products by similarity score
- **Method**: Divide and conquer with pivot selection
- **Time Complexity**: O(n log n) average, O(n²) worst case

## API Endpoints

### Product Management
- `POST /api/products` - Add new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products` - Get all products

### Search & Sort
- `GET /api/products/search/code/{productCode}` - Search by code (BST)
- `POST /api/products/search` - Advanced search
- `GET /api/products/sorted` - Get sorted products (BST)

### Additional Features
- `GET /api/products/recent` - Get recent items (Linked List)
- `GET /api/products/low-stock` - Get low stock products
- `GET /api/products/restock-requests` - Get restock requests (Queue)
- `POST /api/products/restock-requests/process` - Process restock request

### Enhanced Functionalities
- `GET /api/products/predict-stock/{productCode}?days=30` - Stock prediction
- `GET /api/products/predict-stock/all?days=30` - Predict for all products
- `POST /api/products/record-consumption/{productCode}?quantityConsumed=10` - Record consumption
- `GET /api/products/recommend/{productCode}?maxRecommendations=5` - Get recommendations

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Configuration
1. Update `src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wood_carving?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application
1. Clone the repository
2. Navigate to project directory
3. Run: `mvn spring-boot:run`
4. The application will start on `http://localhost:8080`
5. Database tables will be created automatically

## Database Schema

### Product Table
- `id` - Primary Key (Auto-generated)
- `product_code` - Unique product identifier
- `name` - Product name
- `description` - Product description
- `category` - Product category
- `price` - Product price
- `stock_quantity` - Current stock quantity
- `min_stock_level` - Minimum stock level threshold
- `wood_type` - Type of wood used
- `dimensions` - Product dimensions
- `weight` - Product weight
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

## Testing the API

### Using cURL or Postman

**Add a Product:**
```bash
POST http://localhost:8080/api/products
Content-Type: application/json

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

**Search by Code (BST):**
```bash
GET http://localhost:8080/api/products/search/code/WC001
```

**Get Sorted Products (BST):**
```bash
GET http://localhost:8080/api/products/sorted
```

**Get Recommendations:**
```bash
GET http://localhost:8080/api/products/recommend/WC001?maxRecommendations=5
```

**Predict Stock:**
```bash
GET http://localhost:8080/api/products/predict-stock/WC001?days=30
```

## Algorithm Complexity Analysis

| Operation | Data Structure | Time Complexity | Space Complexity |
|-----------|---------------|-----------------|------------------|
| Search | BST | O(log n) avg, O(n) worst | O(log n) |
| Insert | BST | O(log n) avg, O(n) worst | O(log n) |
| Sort | BST (in-order) | O(n) | O(n) |
| Insert | Linked List | O(1) | O(1) |
| Enqueue | Queue | O(1) | O(1) |
| Dequeue | Queue | O(1) | O(1) |
| Moving Average | Array | O(n) | O(n) |
| Quick Sort | Array | O(n log n) avg | O(log n) |

## Key Features for Presentation

1. **Data Structure Demonstrations**
   - Show how BST provides fast search (O(log n))
   - Demonstrate Linked List for recent items
   - Show Queue for restock request management

2. **Algorithm Demonstrations**
   - Moving Average for stock prediction
   - Similarity algorithm with Quick Sort for recommendations

3. **Real-world Application**
   - Business context: Inventory management
   - Practical use cases for each data structure
   - Performance benefits of chosen algorithms

## Future Enhancements
- Graph structure for product relationships
- Heap for priority-based restock requests
- Hash table for faster product code lookups
- Advanced analytics and reporting

## Author
Developed for HDSE Programming Data Structures and Algorithms - I Coursework

## License
This project is developed for educational purposes.

