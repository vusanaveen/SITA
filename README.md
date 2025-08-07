# Microservices Project - UserService & OrderService

This project demonstrates a microservices architecture using Spring Boot 3+ and Java 21 with two independent services: UserService and OrderService.

## Project Structure

```
microservices/
├── userservice/          # User management service
│   ├── src/
│   ├── pom.xml
│   └── application.yml
├── orderservice/         # Order management service
│   ├── src/
│   ├── pom.xml
│   └── application.yml
└── README.md
```

## Technology Stack

- **Java 21**
- **Spring Boot 3.2.x**
- **Spring Web** - REST APIs
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database
- **Lombok** - Boilerplate reduction
- **JUnit 5 + Mockito** - Testing framework
- **Maven** - Build tool
- **Jacoco** - Code coverage
- **Swagger/OpenAPI 3** - API documentation and testing interface

## Services

### UserService (Port: 8081)
- CRUD operations for User entity
- Endpoints: POST, GET, PUT, DELETE /users/{id}
- **API Documentation**: http://localhost:8081/swagger-ui.html

### OrderService (Port: 8082)
- CRUD operations for Order entity
- Validates user existence via UserService before creating orders
- Endpoints: POST, GET, PUT, DELETE /orders/{id}
- **API Documentation**: http://localhost:8082/swagger-ui.html

## Prerequisites

- Java 21
- Maven 3.8+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## Build Instructions

### Build All Services
```bash
# Build UserService
cd userservice
mvn clean install

# Build OrderService
cd ../orderservice
mvn clean install
```

### Run Services

#### UserService
```bash
cd userservice
mvn spring-boot:run
```
UserService will be available at: http://localhost:8081

#### OrderService
```bash
cd orderservice
mvn spring-boot:run
```
OrderService will be available at: http://localhost:8082

## Testing

### Run Tests with Coverage
```bash
# UserService tests
cd userservice
mvn test jacoco:report

# OrderService tests
cd ../orderservice
mvn test jacoco:report
```

### Coverage Reports
- UserService: `userservice/target/site/jacoco/index.html`
- OrderService: `orderservice/target/site/jacoco/index.html`

## API Documentation

Both services include comprehensive Swagger/OpenAPI documentation:

- **UserService API Docs**: http://localhost:8081/swagger-ui.html
- **OrderService API Docs**: http://localhost:8082/swagger-ui.html

The documentation includes:
- Interactive API testing interface
- Request/response schemas
- Example payloads
- HTTP status codes
- Parameter descriptions

## API Endpoints

### UserService (http://localhost:8081)

#### Create User
```bash
POST /users
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123",
  "email": "john@example.com"
}
```

#### Get User
```bash
GET /users/{id}
```

#### Update User
```bash
PUT /users/{id}
Content-Type: application/json

{
  "username": "john_doe_updated",
  "password": "newpassword123",
  "email": "john.updated@example.com"
}
```

#### Delete User
```bash
DELETE /users/{id}
```

### OrderService (http://localhost:8082)

#### Create Order
```bash
POST /orders
Content-Type: application/json

{
  "userId": 1,
  "product": "Laptop",
  "quantity": 2,
  "price": 999.99
}
```

#### Get Order
```bash
GET /orders/{id}
```

#### Update Order
```bash
PUT /orders/{id}
Content-Type: application/json

{
  "userId": 1,
  "product": "Laptop Pro",
  "quantity": 1,
  "price": 1299.99
}
```

#### Delete Order
```bash
DELETE /orders/{id}
```

## Sample Data

Both services include sample data that is automatically loaded on startup:

### UserService Sample Data
- User ID: 1, Username: "john_doe", Email: "john@example.com"
- User ID: 2, Username: "jane_smith", Email: "jane@example.com"

### OrderService Sample Data
- Order ID: 1, User ID: 1, Product: "Laptop", Quantity: 1, Price: 999.99
- Order ID: 2, User ID: 2, Product: "Mouse", Quantity: 2, Price: 29.99

## Exception Handling

The services include centralized exception handling with meaningful HTTP status codes:

- `ResourceNotFoundException` - 404 Not Found
- `InvalidUserException` - 400 Bad Request
- `ValidationException` - 400 Bad Request
- `InternalServerException` - 500 Internal Server Error

## Service Communication

OrderService communicates with UserService to validate user existence before creating orders:

1. OrderService receives order creation request
2. Validates user exists by calling UserService GET /users/{userId}
3. If user not found, throws `InvalidUserException`
4. If user exists, creates the order

## Assumptions

- Duplicate user validation not required (allowed)
- Passwords stored in plain text (no encryption)
- No pagination or filtering required
- No API gateway or service discovery
- Static ports (UserService: 8081, OrderService: 8082)
- Hardcoded service URLs for simplicity

## Code Coverage

Both services achieve 100% code coverage through comprehensive unit tests:

- Controller layer tests using `@WebMvcTest`
- Service layer tests with mocked dependencies
- Repository layer tests using `@DataJpaTest`
- Exception handling tests
- Integration tests for service-to-service communication

## Development Guidelines

- Follow TDD (Test-Driven Development) approach
- Use meaningful exception messages
- Implement proper logging
- Follow REST API best practices
- Maintain clean, modular code structure
- Use DTOs for request/response handling
- Implement proper validation

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   - Change ports in `application.yml` files
   - Kill existing processes using the ports

2. **Database Connection Issues**
   - Ensure H2 database is properly configured
   - Check `data.sql` files for syntax errors

3. **Service Communication Issues**
   - Verify both services are running
   - Check network connectivity between services
   - Validate service URLs in configuration

### Logs

Check application logs for detailed error information:
```bash
# UserService logs
tail -f userservice/logs/application.log

# OrderService logs
tail -f orderservice/logs/application.log
```
