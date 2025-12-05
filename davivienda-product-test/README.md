# Test Backend Java Daivienda - Módulo Productos

Proyecto Spring Boot que implementa un CRUD para el recurso **Productos**, usando H2, paginación, validaciones, DTOs, OpenAPI/Swagger y pruebas unitarias.

## Requisitos

- Java 17
- Gradle 8.x

## Cómo ejecutar

```bash
gradlew bootRun
```

URL de la API: `http://localhost:8080`

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:productsdb`)

## Endpoints principales

- `POST /api/products`
- `GET /api/products`
- `GET /api/products/{id}`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`
- `POST /auth/login`




