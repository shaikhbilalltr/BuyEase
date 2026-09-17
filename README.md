# BuyEase - E-Commerce CRUD API

A comprehensive REST API for managing products in an e-commerce system built with Spring Boot.

## Features

- ✅ **Complete CRUD Operations** - Create, Read, Update, Delete products
- ✅ **REST API** - RESTful endpoints for product management
- ✅ **Database Integration** - JPA/Hibernate with H2 database (configurable)
- ✅ **Input Validation** - Bean validation with comprehensive error handling
- ✅ **Exception Handling** - Global exception handler for consistent error responses
- ✅ **Category Management** - Filter products by category
- ✅ **Stock Management** - Track and manage product quantities
- ✅ **Soft Delete** - Deactivate products without permanent deletion
- ✅ **Unit Tests** - Comprehensive test suite with Mockito

## Technology Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **Hibernate**
- **H2 Database**
- **Maven**
- **Lombok**
- **JUnit 5 & Mockito**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

## Installation

1. Clone the repository:
```bash
git clone https://github.com/shaikhbilalltr/BuyEase.git
cd BuyEase
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Base URL
```
http://localhost:8080/api/v1/products
```

### 1. Create Product
**POST** `/api/v1/products`

**Request Body:**
```json
{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "quantity": 10,
  "category": "Electronics"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "quantity": 10,
  "category": "Electronics",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### 2. Get All Products
**GET** `/api/v1/products`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "quantity": 10,
    "category": "Electronics",
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

### 3. Get All Active Products
**GET** `/api/v1/products/active`

Returns only products with `isActive = true`

### 4. Get Product by ID
**GET** `/api/v1/products/{id}`

**Example:** `GET /api/v1/products/1`

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "quantity": 10,
  "category": "Electronics",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### 5. Get Products by Category
**GET** `/api/v1/products/category/{category}`

**Example:** `GET /api/v1/products/category/Electronics`

Returns all active products in the specified category.

### 6. Update Product
**PUT** `/api/v1/products/{id}`

**Request Body (all fields optional):**
```json
{
  "name": "Gaming Laptop",
  "description": "High-performance gaming laptop",
  "price": 1299.99,
  "quantity": 5,
  "category": "Electronics",
  "isActive": true
}
```

**Response (200 OK):**
Updated product object

### 7. Delete Product (Permanent)
**DELETE** `/api/v1/products/{id}`

**Response (200 OK):**
```json
{
  "message": "Product deleted successfully"
}
```

### 8. Deactivate Product (Soft Delete)
**PATCH** `/api/v1/products/{id}/deactivate`

Marks product as inactive without permanent deletion.

**Response (200 OK):**
Product object with `isActive: false`

### 9. Get Low Stock Products
**GET** `/api/v1/products/low-stock/{threshold}`

**Example:** `GET /api/v1/products/low-stock/5`

Returns all active products with quantity below the threshold.

### 10. Get Product Statistics
**GET** `/api/v1/products/stats/count`

**Response (200 OK):**
```json
{
  "totalProducts": 10,
  "activeProducts": 8
}
```

### 11. Check Product Existence
**HEAD** `/api/v1/products/{id}`

Returns 200 if product exists, 404 if not found.

## Error Handling

The API returns consistent error responses for all error scenarios:

### 404 - Product Not Found
```json
{
  "status": 404,
  "message": "Product not found with id: 1",
  "error": "Product Not Found",
  "timestamp": "2024-01-15T10:35:00",
  "path": "/api/v1/products/1"
}
```

### 409 - Duplicate Product
```json
{
  "status": 409,
  "message": "Product with name 'Laptop' already exists",
  "error": "Duplicate Product",
  "timestamp": "2024-01-15T10:35:00",
  "path": "/api/v1/products"
}
```

### 400 - Validation Error
```json
{
  "status": 400,
  "message": "Validation failed",
  "error": "Bad Request",
  "timestamp": "2024-01-15T10:35:00",
  "path": "/api/v1/products",
  "validationErrors": [
    "name: Product name is required",
    "price: Price must be greater than zero"
  ]
}
```

## Project Structure

```
src/
├── main/
│   ├── java/com/buyease/
│   │   ├── BuyEaseApplication.java       # Main application class
│   │   ├── entity/
│   │   │   └── Product.java              # Product entity
│   │   ├── dto/
│   │   │   ├── CreateProductRequest.java # DTO for creating products
│   │   │   ├── UpdateProductRequest.java # DTO for updating products
│   │   │   └── ProductResponse.java      # DTO for product responses
│   │   ├── repository/
│   │   │   └── ProductRepository.java    # JPA repository
│   │   ├── service/
│   │   │   └── ProductService.java       # Business logic
│   │   ├── controller/
│   │   │   └── ProductController.java    # REST endpoints
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── ProductNotFoundException.java
│   │   │   ├── DuplicateProductException.java
│   │   │   └── ApiError.java
│   │   └── config/
│   └── resources/
│       └── application.properties         # Application configuration
└── test/
    └── java/com/buyease/
        └── service/
            └── ProductServiceTest.java    # Unit tests
```

## Running Tests

```bash
mvn test
```

## Database

### H2 Console

Access the H2 database console at: `http://localhost:8080/h2-console`

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:buyeasedb`
- User Name: `sa`
- Password: (leave empty)

### Switching Databases

To use MySQL or PostgreSQL, update `application.properties`:

**MySQL:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/buyease
spring.datasource.username=root
spring.datasource.******
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

**PostgreSQL:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/buyease
spring.datasource.username=postgres
spring.datasource.******
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## Example Requests

### Using cURL

Create a product:
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "quantity": 10,
    "category": "Electronics"
  }'
```

Get all products:
```bash
curl http://localhost:8080/api/v1/products
```

Get product by ID:
```bash
curl http://localhost:8080/api/v1/products/1
```

Update a product:
```bash
curl -X PUT http://localhost:8080/api/v1/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "price": 1299.99,
    "quantity": 5
  }'
```

Delete a product:
```bash
curl -X DELETE http://localhost:8080/api/v1/products/1
```

## Validation Rules

- **Product Name:** Required, must be unique (case-insensitive)
- **Price:** Required, must be positive number
- **Quantity:** Required, must be non-negative
- **Description & Category:** Optional

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

Bilal Khan - [GitHub](https://github.com/shaikhbilalltr)