# Testing Guide - Wood Carving Inventory Management System

## Prerequisites
1. Ensure MySQL is running
2. Start the Spring Boot application: `mvn spring-boot:run`
3. Application will run on `http://localhost:8080`

---

## Method 1: Using Postman (Recommended)

### Setup Postman
1. Download and install Postman from https://www.postman.com/downloads/
2. Create a new Collection named "Wood Carving Inventory API"

### Test Endpoints

#### 1. Create a User
- **Method:** POST
- **URL:** `http://localhost:8080/api/users`
- **Headers:** 
  - `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "username": "admin",
  "password": "admin123",
  "email": "admin@woodcarving.com",
  "fullName": "Administrator",
  "role": "ADMIN",
  "permissions": "CREATE,READ,UPDATE,DELETE",
  "isActive": true
}
```

#### 2. Create a Supplier
- **Method:** POST
- **URL:** `http://localhost:8080/api/suppliers`
- **Body:**
```json
{
  "supplierCode": "SUP001",
  "name": "Wood Suppliers Ltd",
  "contactPerson": "John Doe",
  "email": "contact@woodsuppliers.com",
  "phone": "+94 11 2345678",
  "address": "123 Main Street",
  "city": "Colombo",
  "country": "Sri Lanka",
  "status": "ACTIVE"
}
```

#### 3. Create a Customer
- **Method:** POST
- **URL:** `http://localhost:8080/api/customers`
- **Body:**
```json
{
  "customerCode": "CUST001",
  "name": "ABC Furniture Store",
  "email": "contact@abcfurniture.com",
  "phone": "+94 11 8765432",
  "address": "456 Market Road",
  "city": "Colombo",
  "country": "Sri Lanka",
  "customerType": "BUSINESS",
  "status": "ACTIVE"
}
```

#### 4. Create a Product
- **Method:** POST
- **URL:** `http://localhost:8080/api/products`
- **Body:**
```json
{
  "productCode": "WC001",
  "name": "Hand Carved Buddha Statue",
  "description": "Beautiful hand-carved wooden Buddha statue",
  "category": "Statues",
  "itemType": "Statue",
  "woodType": "Teak",
  "finishedType": "Polished",
  "isCarved": true,
  "cost": 10000.00,
  "sellingPrice": 15000.00,
  "stock": 25,
  "minStockLevel": 10,
  "dimensions": "12x8x20 inches",
  "weight": 2.5
}
```

#### 5. Get All Products
- **Method:** GET
- **URL:** `http://localhost:8080/api/products`

#### 6. Search Product by Code (BST)
- **Method:** GET
- **URL:** `http://localhost:8080/api/products/search/code/WC001`

#### 7. Get Sorted Products (BST)
- **Method:** GET
- **URL:** `http://localhost:8080/api/products/sorted`

#### 8. Get Recent Items (Linked List)
- **Method:** GET
- **URL:** `http://localhost:8080/api/products/recent`

#### 9. Create Purchase Order
- **Method:** POST
- **URL:** `http://localhost:8080/api/purchase-orders`
- **Body:**
```json
{
  "supplierId": 1,
  "orderDate": "2025-01-17T10:00:00",
  "expectedDeliveryDate": "2025-01-24T10:00:00",
  "notes": "Urgent order",
  "items": [
    {
      "productId": 1,
      "quantity": 50,
      "unitPrice": 10000.00
    }
  ]
}
```

#### 10. Create GRN
- **Method:** POST
- **URL:** `http://localhost:8080/api/grns`
- **Body:**
```json
{
  "purchaseOrderId": 1,
  "receivedDate": "2025-01-17T10:00:00",
  "notes": "All items received",
  "items": [
    {
      "productId": 1,
      "orderedQuantity": 50,
      "receivedQuantity": 50,
      "unitPrice": 10000.00
    }
  ]
}
```

#### 11. Complete GRN (Updates Stock)
- **Method:** POST
- **URL:** `http://localhost:8080/api/grns/1/complete`

#### 12. Create Sales Order
- **Method:** POST
- **URL:** `http://localhost:8080/api/sales-orders`
- **Body:**
```json
{
  "customerId": 1,
  "orderDate": "2025-01-17T10:00:00",
  "expectedDeliveryDate": "2025-01-20T10:00:00",
  "tax": 500.00,
  "discount": 200.00,
  "notes": "Customer order",
  "items": [
    {
      "productId": 1,
      "quantity": 5,
      "unitPrice": 15000.00
    }
  ]
}
```

#### 13. Confirm Sales Order (Deducts Stock)
- **Method:** POST
- **URL:** `http://localhost:8080/api/sales-orders/1/confirm`

#### 14. Create Invoice from Sales Order
- **Method:** POST
- **URL:** `http://localhost:8080/api/invoices/from-sales-order/1`
- **Body:**
```json
{
  "invoiceDate": "2025-01-17T10:00:00",
  "dueDate": "2025-02-17T10:00:00",
  "notes": "Payment terms: 30 days"
}
```

#### 15. Record Payment
- **Method:** POST
- **URL:** `http://localhost:8080/api/invoices/1/payment?paymentAmount=5000.00`

#### 16. Stock Prediction
- **Method:** GET
- **URL:** `http://localhost:8080/api/products/predict-stock/WC001?days=30`

#### 17. Product Recommendations
- **Method:** GET
- **URL:** `http://localhost:8080/api/products/recommend/WC001?maxRecommendations=5`

---

## Method 2: Using cURL (Command Line)

### Windows (PowerShell)
```powershell
# Create a Product
curl -X POST http://localhost:8080/api/products `
  -H "Content-Type: application/json" `
  -d '{\"productCode\":\"WC001\",\"name\":\"Hand Carved Buddha Statue\",\"category\":\"Statues\",\"itemType\":\"Statue\",\"woodType\":\"Teak\",\"finishedType\":\"Polished\",\"isCarved\":true,\"cost\":10000.00,\"sellingPrice\":15000.00,\"stock\":25,\"minStockLevel\":10,\"dimensions\":\"12x8x20 inches\",\"weight\":2.5}'

# Get All Products
curl -X GET http://localhost:8080/api/products

# Get Product by ID
curl -X GET http://localhost:8080/api/products/1

# Search Product by Code
curl -X GET http://localhost:8080/api/products/search/code/WC001

# Get Sorted Products
curl -X GET http://localhost:8080/api/products/sorted

# Get Recent Items
curl -X GET http://localhost:8080/api/products/recent

# Get Low Stock Products
curl -X GET http://localhost:8080/api/products/low-stock

# Get Restock Requests
curl -X GET http://localhost:8080/api/products/restock-requests

# Stock Prediction
curl -X GET "http://localhost:8080/api/products/predict-stock/WC001?days=30"

# Product Recommendations
curl -X GET "http://localhost:8080/api/products/recommend/WC001?maxRecommendations=5"
```

### Linux/Mac (Terminal)
```bash
# Create a Product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "WC001",
    "name": "Hand Carved Buddha Statue",
    "category": "Statues",
    "itemType": "Statue",
    "woodType": "Teak",
    "finishedType": "Polished",
    "isCarved": true,
    "cost": 10000.00,
    "sellingPrice": 15000.00,
    "stock": 25,
    "minStockLevel": 10,
    "dimensions": "12x8x20 inches",
    "weight": 2.5
  }'

# Get All Products
curl -X GET http://localhost:8080/api/products

# Get Product by ID
curl -X GET http://localhost:8080/api/products/1

# Search Product by Code
curl -X GET http://localhost:8080/api/products/search/code/WC001

# Get Sorted Products
curl -X GET http://localhost:8080/api/products/sorted

# Get Recent Items
curl -X GET http://localhost:8080/api/products/recent
```

---

## Method 3: Using Browser (GET Requests Only)

You can test GET endpoints directly in your browser:

1. **Get All Products:**
   ```
   http://localhost:8080/api/products
   ```

2. **Get Product by ID:**
   ```
   http://localhost:8080/api/products/1
   ```

3. **Search Product by Code:**
   ```
   http://localhost:8080/api/products/search/code/WC001
   ```

4. **Get Sorted Products:**
   ```
   http://localhost:8080/api/products/sorted
   ```

5. **Get Recent Items:**
   ```
   http://localhost:8080/api/products/recent
   ```

6. **Get All Suppliers:**
   ```
   http://localhost:8080/api/suppliers
   ```

7. **Get All Customers:**
   ```
   http://localhost:8080/api/customers
   ```

8. **Get All Purchase Orders:**
   ```
   http://localhost:8080/api/purchase-orders
   ```

9. **Get All Sales Orders:**
   ```
   http://localhost:8080/api/sales-orders
   ```

10. **Get All Invoices:**
    ```
    http://localhost:8080/api/invoices
    ```

---

## Method 4: Using HTTPie (Alternative to cURL)

Install HTTPie: `pip install httpie` or download from https://httpie.io/

```bash
# Create a Product
http POST http://localhost:8080/api/products \
  productCode=WC001 \
  name="Hand Carved Buddha Statue" \
  category=Statues \
  itemType=Statue \
  woodType=Teak \
  finishedType=Polished \
  isCarved:=true \
  cost:=10000.00 \
  sellingPrice:=15000.00 \
  stock:=25 \
  minStockLevel:=10 \
  dimensions="12x8x20 inches" \
  weight:=2.5

# Get All Products
http GET http://localhost:8080/api/products
```

---

## Method 5: Using Spring Boot Test (Unit Testing)

Create test files in `src/test/java`:

```java
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testCreateProduct() throws Exception {
        String productJson = """
            {
              "productCode": "WC001",
              "name": "Test Product",
              "category": "Statues",
              "itemType": "Statue",
              "woodType": "Teak",
              "finishedType": "Polished",
              "isCarved": true,
              "cost": 10000.00,
              "sellingPrice": 15000.00,
              "stock": 25,
              "minStockLevel": 10,
              "dimensions": "12x8x20",
              "weight": 2.5
            }
            """;
        
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isCreated());
    }
}
```

---

## Quick Test Sequence

### Step 1: Start the Application
```bash
mvn spring-boot:run
```

### Step 2: Verify Application is Running
Open browser: `http://localhost:8080/api/products`
- Should return empty array `[]` if no products exist
- Should return 200 OK status

### Step 3: Create Test Data (Using Postman or cURL)

1. **Create User**
2. **Create Supplier**
3. **Create Customer**
4. **Create Product**
5. **Get Product** - Verify it was created
6. **Search Product** - Test BST search
7. **Get Sorted Products** - Test BST sorting
8. **Get Recent Items** - Test Linked List
9. **Create Purchase Order**
10. **Create GRN**
11. **Complete GRN** - Verify stock increased
12. **Create Sales Order**
13. **Confirm Sales Order** - Verify stock decreased
14. **Create Invoice**
15. **Record Payment**

### Step 4: Verify Data Structures

- **Array:** Check `/api/products` - all products stored
- **BST:** Check `/api/products/sorted` - sorted by product code
- **Linked List:** Check `/api/products/recent` - last 10 items
- **Queue:** Check `/api/products/restock-requests` - restock requests

---

## Common Issues & Solutions

### Issue 1: Connection Refused
**Solution:** Ensure Spring Boot application is running on port 8080

### Issue 2: 404 Not Found
**Solution:** Check the endpoint URL is correct (case-sensitive)

### Issue 3: 400 Bad Request
**Solution:** 
- Check JSON format is valid
- Ensure all required fields are provided
- Check data types match (e.g., Boolean vs String)

### Issue 4: Database Connection Error
**Solution:**
- Verify MySQL is running
- Check `application.properties` credentials
- Ensure database `wood_carving` exists or can be created

### Issue 5: CORS Error (if testing from browser console)
**Solution:** CORS is already enabled in controllers with `@CrossOrigin(origins = "*")`

---

## Testing Checklist

- [ ] Application starts without errors
- [ ] Database connection successful
- [ ] GET endpoints return data (or empty arrays)
- [ ] POST endpoints create resources
- [ ] PUT endpoints update resources
- [ ] DELETE endpoints remove resources
- [ ] BST search works correctly
- [ ] BST sorting works correctly
- [ ] Linked List (recent items) works correctly
- [ ] Queue (restock requests) works correctly
- [ ] Stock updates when GRN is completed
- [ ] Stock decreases when sales order is confirmed
- [ ] Stock validation prevents overselling
- [ ] Auto-generated numbers work (PO, SO, GRN, INV)

---

## Recommended Testing Order

1. **Basic CRUD:** User → Supplier → Customer → Product
2. **Data Structures:** Test BST, Linked List, Queue with products
3. **Purchase Flow:** Purchase Order → GRN → Stock Update
4. **Sales Flow:** Sales Order → Confirm → Stock Deduction
5. **Invoice Flow:** Create Invoice → Record Payment
6. **Advanced Features:** Stock Prediction, Recommendations

---

## Useful Browser Extensions

1. **REST Client** (VS Code Extension)
2. **Thunder Client** (VS Code Extension)
3. **ModHeader** (Chrome Extension) - For adding headers
4. **JSON Formatter** (Chrome Extension) - For better JSON viewing

---

## Monitoring

Check application logs in the console for:
- SQL queries (if `spring.jpa.show-sql=true`)
- Request/response details
- Error messages
- Data structure operations

---

## Next Steps

Once basic testing is complete:
1. Test edge cases (invalid data, missing fields)
2. Test error handling
3. Test concurrent requests
4. Test data structure performance
5. Verify all business logic works correctly


