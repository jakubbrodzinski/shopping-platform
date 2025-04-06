# Shopping Platform
## Considerations/Comments
### Auth
For authentication, **JWT** (JSON Web Token) would be a strong choice, particularly because it is lightweight and stateless. It allows for secure, token-based authentication that doesn't require server-side session storage. The client can authenticate once and include the JWT in the Authorization header for subsequent requests. This would be ideal for the app, which could scale in the future and potentially expose sensitive data in the product or order endpoints.

JWT enables easy integration with Spring Security and scales well as the app grows. It is a widely accepted standard for securing REST APIs, providing both simplicity and flexibility. Additionally, it supports both short-lived access tokens and refresh tokens, ensuring secure communication in a distributed system. Furthermore, JWT could be part of a larger infrastructure component dedicated to authentication and authorization, adhering to the separation of concerns principle. This could also enable Single Sign-On (SSO), allowing users to authenticate across multiple applications with a unified identity management system.

### How can we make the service redunant?


## Future improvements
### API Evolution: HATEOAS Support
**Current State:** The application exposes a simple, RESTful API with standard endpoints for interacting with resources like orders and products.

**Consideration:** If the application grows in complexity—especially in terms of relationships between resources (e.g., order → customer → product)—it may become beneficial to adopt HATEOAS (Hypermedia as the Engine of Application State). HATEOAS can improve discoverability of the API and guide clients more dynamically through available actions, especially in more complex, stateful flows.

### Database Setup: From H2 to PostgreSQL
**Current State:** The app uses an in-memory H2 database with data.sql for simplicity, allowing it to be run and tested easily in any environment without external dependencies.

**Improvement:**  Migrating to PostgreSQL would be a natural next step for production-readiness. PostgreSQL provides:
- More robust indexing and query optimization
- Support for advanced data types (e.g., JSONB, arrays)
- Better scalability and concurrency handling

**Improvement:** It's rather a question of when, not if, but once PostgreSQL (or any other RDBMS) would be introduced in the app, `TestContainers` should be considered as a fast way to provide an alternative to some mocks during tests. 

This switch would also allow the use of database features that are not supported or simulated in H2.
### Architecture: From Transaction Script to Domain-Oriented Design
**Current State:** The application currently follows a basic layered architecture, and favors the [Transaction Script](https://martinfowler.com/eaaCatalog/transactionScript.html) pattern for implementing business logic. This approach was chosen due to the app’s simplicity and the lack of substantial domain logic.

**Future Direction:**
As business requirements grow and logic becomes more complex, transitioning to a more structured approach like [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) (inspired by Martin Fowler and Robert C. Martin) would be a good move. Benefits include:
- Clear separation of concerns
- Better testability
- Flexibility to evolve the domain model independently of infrastructure and delivery mechanisms

- This would also likely include restructuring the current package layout to follow domain-centric boundaries (e.g., orders, products, catalog, etc.).
## Decisions
### Internal and external identifiers
I've decided to work with two separate columns as identifiers of the enitites (`Order` and `Product`). It can be interpreted as overengineering taking into consideration the size and complexity of the app, but I've considered it as a good practice that's rarely talked about and/or used. Below I am adding few upsides of this approach.
- **Security** - UUIDs in APIs prevent ID guessing attacks while `Long` IDs power efficient database operations.
- **Performance** - Database uses fast `Long` indexes internally, avoiding UUID indexing slowdowns.
- **Distributed Ready** - UUIDs guarantee global uniqueness for cross-system data merging.
- **Debug Friendly** - Developers use `Long` for queries/logs while hiding them from external interfaces.
## Local Run
App can be run and tested by simplify running `./gradlew bootRun` in Terminal. It uses H2 DB with `data.sql`, so no need to define any extra test data to test it.
## Docs
Application exposes API Documentation in OpenAPI format. There are two ways to get to it.
- Once the application is in running state, you can go to `http://localhost:8080/swagger-ui/index.html` to browser through the docs.
- Running `./gradlew clean generateOpenApiDocs` in terminal will generate the docs and put it into `build/openapi.json` file.