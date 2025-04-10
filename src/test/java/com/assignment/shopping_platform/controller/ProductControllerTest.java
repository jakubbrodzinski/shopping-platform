package com.assignment.shopping_platform.controller;

import com.assignment.shopping_platform.utils.TestMotherObject;
import io.restassured.RestAssured;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.assignment.shopping_platform.TestFixtures.EUR;
import static com.assignment.shopping_platform.TestFixtures.USD;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {
    @Autowired
    private TestMotherObject testMotherObject;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown(){
        testMotherObject.clear();
    }

    @Test
    void shouldReturnProductsWithDefaultPageRequest() throws JSONException {
        var p1 = testMotherObject.createProduct("product 1", EUR("1.33"));
        var p2 = testMotherObject.createProduct("product 2", USD("1.99"));

        var actualResponse = given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .extract().asString();

        assertEquals("""
                [{
                  "id": "%s",
                  "name": "product 1",
                  "description": "some description for product 1",
                  "price": {
                    "currency": "EUR",
                    "amount": "1.33"
                  }
                },
                  {
                    "id": "%s",
                    "name": "product 2",
                    "description": "some description for product 2",
                    "price": {
                      "currency": "USD",
                      "amount": "1.99"
                    }
                  }]""".formatted(p1.getExternalId().toString(), p2.getExternalId().toString()), actualResponse, true);
    }

    @Test
    void shouldCreateProduct() {
        given()
                .contentType("application/json")
                .body("""
                        {
                            "name": "Test Product",
                            "price": {
                                "currency": "USD",
                                "amount": "150.01"
                            },
                            "description": "Test description"
                        }""")
                .when()
                .post("/products")
                .then()
                .statusCode(200)
                .body("id", matchesRegex("[0-9a-zA-Z\\-]+"))
                .body("name", equalTo("Test Product"))
                .body("price.currency", equalTo("USD"))
                .body("price.amount", equalTo("150.01"))
                .body("description", equalTo("Test description"));
    }

    @Test
    void shouldUpdateProduct() throws JSONException {
        var p1 = testMotherObject.createProduct("product 1", EUR("1.33"));

        var response = given()
                .contentType("application/json")
                .body("""
                        {
                            "name": "Updated Product",
                             "price": {
                                "currency": "USD",
                                "amount": "1.99"
                            },
                            "description": "Updated description"
                        }""")
                .when()
                .put("/products/{productId}", p1.getExternalId().toString())
                .then()
                .statusCode(200)
                .extract().asString();

        assertEquals(response, """
                {
                      "id": "%s",
                      "name": "Updated Product",
                      "description": "Updated description",
                      "price": {
                        "currency": "USD",
                        "amount": "1.99"
                      }
                }""".formatted(p1.getExternalId().toString()), true);
    }

    @Test
    void shouldResultInErrorWhenProductNotExisitng() {
        given()
                .contentType("application/json")
                .body("""
                        {
                            "name": "Updated Product",
                             "price": {
                                "currency": "USD",
                                "amount": "1.99"
                            },
                            "description": "Updated description"
                        }""")
                .when()
                .put("/products/{productId}", "25a483b6-ad09-4134-befa-44a9116544ca")
                .then()
                .statusCode(404);
    }
}
