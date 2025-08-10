# Microservices Project - UserService & OrderService

This project demonstrates a microservices architecture using Spring Boot 3+ and Java 21 with two independent services: UserService and OrderService.
In addition to the stated requirements, the project also includes full Swagger/OpenAPI documentation with detailed example responses for various HTTP statuses, making the APIs easier to explore, test, and maintain.
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
 - Contains base exception handler, error DTO, and base OpenAPI config reused by both services.


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
cd userservice
mvn clean install

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
```

```bash
# OrderService tests
cd ../orderservice
mvn test jacoco:report
```

### Coverage Reports
- UserService: `userservice/target/site/jacoco/index.html`
- OrderService: `orderservice/target/site/jacoco/index.html`

## API Documentation

We use `springdoc-openapi` with Swagger UI. Use Swagger for endpoint details and try-outs:

- UserService Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OrderService Swagger UI: `http://localhost:8082/swagger-ui/index.html`


## API Endpoints

Refer to Swagger UI for the full, up-to-date endpoint list and request/response models.

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
- Static service URLs via application.yml

## Code Coverage

Both services include comprehensive unit and slice tests:

- Controller layer tests using `@WebMvcTest`
- Service layer tests with mocked dependencies
- Repository layer tests using `@DataJpaTest`
- Exception handling tests

Generate coverage reports with Jacoco (see commands above) and open the HTML reports to review coverage.

## Services

### UserService
- CRUD for User entity
- Handling of duplicate users should not be handled — no duplicate checks enforced in service
 - Exception handling — centralized `@RestControllerAdvice`, custom exceptions and consistent status codes

### OrderService 
- CRUD for Order entity
- Validates user existence via UserService before creating/updating orders
 - Exception handling — centralized `@RestControllerAdvice`, custom exceptions and consistent status codes

## Troubleshooting

1. Ports already in use
   - Change ports in each service `application.yml`
2. Service communication
   - Ensure both services are running and URLs match configuration.

