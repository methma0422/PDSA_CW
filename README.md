# Wood Carving Inventory Management System


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



