# Complete API Documentation - Wood Carving Inventory Management System

## Overview
This document provides a complete list of all REST API endpoints available in the system.

## Base URL
```
http://localhost:8080/api
```

---

## 1. Product Management (`/api/products`)

### Product CRUD Operations
- `POST /api/products` - Create a new product
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

### Product Search & Sort
- `GET /api/products/search/code/{productCode}` - Search product by code (using BST)
- `POST /api/products/search` - Advanced search with filters
- `GET /api/products/sorted` - Get sorted products (BST in-order traversal)

### Product Features
- `GET /api/products/recent` - Get recently added items (Linked List)
- `GET /api/products/low-stock` - Get low stock products
- `GET /api/products/restock-requests` - Get all restock requests (Queue)
- `POST /api/products/restock-requests/process` - Process next restock request

### Enhanced Functionalities
- `GET /api/products/predict-stock/{productCode}?days=30` - Stock prediction using Moving Average
- `GET /api/products/predict-stock/all?days=30` - Predict stock for all products
- `POST /api/products/record-consumption/{productCode}?quantityConsumed=10` - Record stock consumption
- `GET /api/products/recommend/{productCode}?maxRecommendations=5` - Get product recommendations

---

## 2. User Management (`/api/users`)

- `POST /api/users` - Create a new user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

**User Fields:**
- username, password, email, fullName, role, permissions, isActive

---

## 3. Supplier Management (`/api/suppliers`)

- `POST /api/suppliers` - Create a new supplier
- `GET /api/suppliers` - Get all suppliers
- `GET /api/suppliers/{id}` - Get supplier by ID
- `GET /api/suppliers/status/{status}` - Get suppliers by status (ACTIVE/INACTIVE)
- `PUT /api/suppliers/{id}` - Update supplier
- `DELETE /api/suppliers/{id}` - Delete supplier

**Supplier Fields:**
- supplierCode, name, contactPerson, email, phone, address, city, country, status

---

## 4. Customer Management (`/api/customers`)

- `POST /api/customers` - Create a new customer
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `GET /api/customers/status/{status}` - Get customers by status (ACTIVE/INACTIVE)
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

**Customer Fields:**
- customerCode, name, email, phone, address, city, country, customerType, status

---

## 5. Purchase Order Management (`/api/purchase-orders`)

- `POST /api/purchase-orders` - Create a new purchase order
- `GET /api/purchase-orders` - Get all purchase orders
- `GET /api/purchase-orders/{id}` - Get purchase order by ID
- `PUT /api/purchase-orders/{id}` - Update purchase order
- `DELETE /api/purchase-orders/{id}` - Delete purchase order

**Purchase Order Fields:**
- orderNumber (auto-generated), supplierId, orderDate, expectedDeliveryDate, status, totalAmount, notes, items[]

**Purchase Order Item Fields:**
- productId, quantity, unitPrice, totalPrice

**Status Values:** PENDING, APPROVED, RECEIVED, CANCELLED

---

## 6. GRN (Good Receive Note) Management (`/api/grns`)

- `POST /api/grns` - Create a new GRN
- `GET /api/grns` - Get all GRNs
- `GET /api/grns/{id}` - Get GRN by ID
- `GET /api/grns/purchase-order/{purchaseOrderId}` - Get GRNs by purchase order
- `PUT /api/grns/{id}` - Update GRN
- `POST /api/grns/{id}/complete` - Complete GRN (updates stock)
- `DELETE /api/grns/{id}` - Delete GRN

**GRN Fields:**
- grnNumber (auto-generated), purchaseOrderId, receivedDate, status, notes, items[]

**GRN Item Fields:**
- productId, orderedQuantity, receivedQuantity, unitPrice, totalPrice

**Status Values:** PENDING, COMPLETED, CANCELLED

**Note:** When GRN is completed, product stock is automatically updated.

---

## 7. Sales Order Management (`/api/sales-orders`)

- `POST /api/sales-orders` - Create a new sales order
- `GET /api/sales-orders` - Get all sales orders
- `GET /api/sales-orders/{id}` - Get sales order by ID
- `GET /api/sales-orders/customer/{customerId}` - Get sales orders by customer
- `PUT /api/sales-orders/{id}` - Update sales order
- `POST /api/sales-orders/{id}/confirm` - Confirm sales order (deducts stock)
- `DELETE /api/sales-orders/{id}` - Delete sales order

**Sales Order Fields:**
- orderNumber (auto-generated), customerId, orderDate, expectedDeliveryDate, status, subtotal, tax, discount, totalAmount, notes, items[]

**Sales Order Item Fields:**
- productId, quantity, unitPrice, totalPrice

**Status Values:** PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED

**Note:** When sales order is confirmed, product stock is automatically deducted. Stock availability is checked before confirmation.

---

## 8. Invoice Management (`/api/invoices`)

- `POST /api/invoices` - Create a new invoice
- `POST /api/invoices/from-sales-order/{salesOrderId}` - Create invoice from sales order
- `GET /api/invoices` - Get all invoices
- `GET /api/invoices/{id}` - Get invoice by ID
- `GET /api/invoices/customer/{customerId}` - Get invoices by customer
- `GET /api/invoices/sales-order/{salesOrderId}` - Get invoices by sales order
- `PUT /api/invoices/{id}` - Update invoice
- `POST /api/invoices/{id}/issue` - Issue invoice
- `POST /api/invoices/{id}/payment?paymentAmount={amount}` - Record payment
- `DELETE /api/invoices/{id}` - Delete invoice

**Invoice Fields:**
- invoiceNumber (auto-generated), salesOrderId, customerId, invoiceDate, dueDate, status, subtotal, tax, discount, totalAmount, paidAmount, balance, notes, items[]

**Invoice Item Fields:**
- productId, quantity, unitPrice, totalPrice

**Status Values:** DRAFT, ISSUED, PAID, OVERDUE, CANCELLED

**Note:** Balance is automatically calculated as totalAmount - paidAmount.

---

## Request/Response Examples

### Create Product
```json
POST /api/products
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

### Create Purchase Order
```json
POST /api/purchase-orders
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

### Create Sales Order
```json
POST /api/sales-orders
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

### Create GRN
```json
POST /api/grns
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

### Create Invoice from Sales Order
```json
POST /api/invoices/from-sales-order/1
{
  "invoiceDate": "2025-01-17T10:00:00",
  "dueDate": "2025-02-17T10:00:00",
  "notes": "Payment terms: 30 days"
}
```

### Record Payment
```
POST /api/invoices/1/payment?paymentAmount=5000.00
```

---

## Data Structures Used

1. **Array (ArrayList)** - Stores all products
2. **Binary Search Tree (BST)** - Fast searching and sorting by product code
3. **Linked List** - Manages recently added items (last 10)
4. **Queue** - Manages restock requests (FIFO)

---

## Status Codes

- `200 OK` - Request successful
- `201 CREATED` - Resource created successfully
- `204 NO CONTENT` - Resource deleted successfully
- `400 BAD REQUEST` - Invalid request data
- `404 NOT FOUND` - Resource not found
- `500 INTERNAL SERVER ERROR` - Server error

---

## Error Handling

All endpoints return appropriate HTTP status codes. Error responses typically include:
- Error message in the response body
- Appropriate HTTP status code

---

## Notes

1. All timestamps are in ISO 8601 format (LocalDateTime)
2. Order numbers, GRN numbers, and Invoice numbers are auto-generated
3. Stock is automatically updated when:
   - GRN is completed (stock increases)
   - Sales order is confirmed (stock decreases)
4. Stock availability is checked before confirming sales orders
5. All endpoints support CORS for frontend integration

---

## Testing

Use tools like Postman, cURL, or any REST client to test the endpoints. Ensure:
1. MySQL database is running
2. Application is started on port 8080
3. Database credentials are correctly configured in `application.properties`


