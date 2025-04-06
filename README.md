# Shopping Platform
## Considerations/Comments

## Local Run
App can be run and tested by simplify running `./gradlew bootRun` in Terminal. It uses H2 DB with `data.sql`, so no need to define any extra test data to test it.
## Docs
Application exposes API Documentation in OpenAPI format. There are two ways to get to it.
- Once the application is in running state, you can go to `http://localhost:8080/swagger-ui/index.html` to browser through the docs.
- Running `./gradlew clean generateOpenApiDocs` in terminal will generate the docs and put it into `build/openapi.json` file.