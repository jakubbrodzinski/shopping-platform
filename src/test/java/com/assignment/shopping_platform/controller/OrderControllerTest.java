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
import org.springframework.core.io.ResourceLoader;

import java.nio.file.Files;

import static com.assignment.shopping_platform.TestFixtures.EUR;
import static com.assignment.shopping_platform.TestFixtures.USD;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.matchesRegex;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    @Autowired
    private TestMotherObject testMotherObject;
    @Autowired
    private ResourceLoader resourceLoader;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        testMotherObject.clear();
    }

    @Test
    void shouldCreateOrder() throws JSONException {
        var p1 = testMotherObject.createProduct("p one", EUR("1.33"));
        var p2 = testMotherObject.createProduct("p two", USD("100.01"));

        var actualResponse = given()
                .contentType("application/json")
                .body("""
                        {
                            "email": "newcustomer@example.com",
                            "items": [
                               { "productId": "%s", "quantity": 1 },
                               { "productId": "%s", "quantity": 2 }
                            ]
                        }""".formatted(p1.getExternalId(), p2.getExternalId()))
                .when()
                .post("/orders")
                .then()
                .statusCode(200)
                .body("id", matchesRegex("[0-9a-zA-Z\\-]+"))
                .body("createdAt", matchesRegex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+Z"))
                .extract().asString();

        assertEquals(
                extractStub("orders/expectedResponse.json").formatted(),
                actualResponse,
                false);
    }

    @Test
    void shouldReturnErrorWhenCreatingEmptyOrder() {
        given()
                .contentType("application/json")
                .body("""
                        {
                            "email": "newcustomer@example.com",
                            "items": []
                        }""")
                .when()
                .post("/orders")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturnErrorIfProductDoesNotExist() {
        given()
                .contentType("application/json")
                .body("""
                        {
                            "email": "newcustomer@example.com",
                            "items": [{ "productId": "25a483b6-ad09-4134-befa-44a9116544ca", "quantity": 1 }]
                        }""")
                .when()
                .post("/orders")
                .then()
                .statusCode(400);
    }

    private String extractStub(String path) {
        try {
            return new String(Files.readAllBytes(
                    resourceLoader.getResource("classpath:%s".formatted(path))
                            .getFile()
                            .toPath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
