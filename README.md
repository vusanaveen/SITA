# Microservices Project - UserService & OrderService

This project demonstrates a microservices architecture using Spring Boot 3+ and Java 21 with two independent services: UserService and OrderService.

## Project Structure

```
SITA/
├── shared-common/      # Shared library for common config, DTOs, and exceptions
│   ├── src/
│   └── pom.xml
├── userservice/          # User management service
│   ├── src/
│   └── pom.xml
├── orderservice/         # Order management service
│   ├── src/
│   └── pom.xml
├── pom.xml               # Root parent POM (builds all modules)
└── README.md
```

### About `shared-common`

- Used by: Both `userservice` and `orderservice` (built first during root build).


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
- **springdoc-openapi** (OpenAPI 3) with Swagger UI (code-first generation)

## Services

### UserService (Port: 8081)
- CRUD operations for User entity
- Endpoints (as required):
  1. POST `/users` — Create a user
  2. GET `/users/{id}` — Retrieve a user by ID
  3. PUT `/users/{id}` — Update a user by ID
  4. DELETE `/users/{id}` — Delete a user by ID
- Extra endpoints (for demo/testing convenience):
  - GET `/users` — List all users
  - GET `/users/{id}/exists` — Lightweight existence check used by OrderService
 - **Swagger UI**: http://localhost:8081/swagger-ui/index.html

### OrderService (Port: 8082)
- CRUD operations for Order entity
- Validates user existence via UserService before creating/updating orders
- Endpoints (as required):
  1. POST `/orders` — Create an order
  2. GET `/orders/{id}` — Retrieve an order by ID
  3. PUT `/orders/{id}` — Update an order by ID
  4. DELETE `/orders/{id}` — Delete an order by ID
- Extra endpoints (for demo/testing convenience):
  - GET `/orders` — List all orders
  - GET `/orders/user/{userId}` — List all orders for a user
 - **Swagger UI**: http://localhost:8082/swagger-ui/index.html


## Prerequisites

- Java 21
- Maven 3.8+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## Build Instructions

### Build from Root (recommended)
```bash
mvn clean install
```
This builds all modules in the correct dependency order: `shared-common` → `userservice` → `orderservice`.

### Build a Single Module
```bash
# Only userservice//orderservice cd 
mvn clean install

```

### Run Services

#### UserService
```bash
cd userservice
mvn spring-boot:run
```
UserService will be available at: http://localhost:8081
Swagger UI: `http://localhost:8081/swagger-ui/index.html`

#### OrderService
```bash
cd orderservice
mvn spring-boot:run
```
OrderService will be available at: http://localhost:8082
Swagger UI: `http://localhost:8082/swagger-ui/index.html`

## Testing

### Run Tests with Coverage
```bash
# UserService tests
cd userservice
mvn test jacoco:report



# OrderService tests
cd ../orderservice
mvn test jacoco:report



### Coverage Reports
- UserService: `userservice/target/site/jacoco/index.html`
- OrderService: `orderservice/target/site/jacoco/index.html`

## API Documentation

We use `springdoc-openapi` to generate OpenAPI 3 specs and serve Swagger UI.

- UserService: `http://localhost:8081/swagger-ui/index.html`
- OrderService: `http://localhost:8082/swagger-ui/index.html`

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

Sample data is auto-seeded on application startup. No profiles or extra configuration needed.

Just start the services and use Swagger UI to try the endpoints.

## Exception Handling

The services include centralized exception handling with meaningful HTTP status codes:

- `ResourceNotFoundException` - 404 Not Found
- `InvalidUserException` - 400 Bad Request (for invalid user references)
- `ValidationException` - 400 Bad Request
- `InternalServerException` - 500 Internal Server Error

## Service Communication

OrderService communicates with UserService to validate user existence before creating orders:

1. OrderService receives order creation request
2. Validates user exists by calling UserService GET /users/{userId}/exists
3. If user not found, throws `InvalidUserException` (400 Bad Request)
4. If user exists, creates the order

## Assumptions

- Duplicate user validation is not enforced (allowed)
- Passwords stored in plain text (no encryption)
- No pagination or filtering required
- No API gateway or service discovery
- Static ports (UserService: 8081, OrderService: 8082)
- Hardcoded service URLs for simplicity

## Code Coverage

Both services include comprehensive unit and slice tests:

- Controller layer tests using `@WebMvcTest`
- Service layer tests with mocked dependencies
- Repository layer tests using `@DataJpaTest`
- Exception handling tests

Generate coverage reports with Jacoco (see commands above) and open the HTML reports to review coverage.

## Acceptance Criteria Mapping (for reviewer)

- UserService
  - Should be able to add user — POST `/users` (visible as “1. Create user” in Swagger)
  - Should be able to retrieve users by Id — GET `/users/{id}` (“2. Get user by ID”)
  - Should be able to update user by Id — PUT `/users/{id}` (“3. Update user by ID”)
  - Should be able to delete user by Id — DELETE `/users/{id}` (“4. Delete user by ID”)
  - Handling of duplicate users should not be handled — no duplicate checks enforced in service
  - Exception Handling — centralized `@RestControllerAdvice`, custom exceptions and consistent status codes

- OrderService
  - Communicates with UserService to validate user existence before creating an order
  - Should be able to create order — POST `/orders` (“1. Create order”)
  - Should be able to retrieve order by Id — GET `/orders/{id}` (“2. Get order by ID”)
  - Should be able to update order by Id — PUT `/orders/{id}` (“3. Update order by ID”)
  - Should be able to delete order by Id — DELETE `/orders/{id}` (“4. Delete order by ID”)
  - Exception Handling — centralized `@RestControllerAdvice`, custom exceptions and consistent status codes

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

### H2 Console

- UserService H2 Console: `http://localhost:8081/h2-console`
- OrderService H2 Console: `http://localhost:8082/h2-console`

Use JDBC URLs from each service's `application.yml` (e.g., `jdbc:h2:mem:userdb`, `jdbc:h2:mem:orderdb`).
