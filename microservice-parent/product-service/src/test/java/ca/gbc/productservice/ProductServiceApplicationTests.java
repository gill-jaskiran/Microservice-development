package ca.gbc.productservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

// Tells Springboot to look for a main configuration class(@SpringBootApplication)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    //This annotation is used in combination with text containers to automatically configure th connection to
    // the Test MongoDbContainer
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");


    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mongoDBContainer.start();
    }



    @Test
    void createProductTest(){

        String requestBody = """
            {
                 "name" : "Samsung TV",
                 "description" : "Samsung TV - Model 2024",
                 "price" : 2000
            }
        """;

        // BBD - Behavioural Driven Development (Given, When, Then)
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("Samsung TV"))
                .body("description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("price", Matchers.equalTo(2000));
    }

    @Test
    void getAllProductsTest(){

        String requestBody = """
            {
                 "name" : "Samsung TV",
                 "description" : "Samsung TV - Model 2024",
                 "price" : 2000
            }
        """;

        // BBD - Behavioural Driven Development (Given, When, Then)
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("Samsung TV"))
                .body("description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("price", Matchers.equalTo(2000) );


        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/product")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].name", Matchers.equalTo("Samsung TV"))
                .body("[0].description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("[0].price", Matchers.equalTo(2000));

    }

    @Test
    void updateProductTest() {
        String requestBodyCreate = """
        {
             "name" : "Samsung TV",
             "description" : "Samsung TV - Model 2024",
             "price" : 2000
        }
    """;

        String productId = RestAssured.given()
                .contentType("application/json")
                .body(requestBodyCreate)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .path("id");  // Extract the product ID for the update

        // Updating the product
        String requestBodyUpdate = """
        {
             "name" : "Samsung TV - Updated",
             "description" : "Updated description for Samsung TV",
             "price" : 2200
        }
    """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBodyUpdate)
                .when()
                .put("/api/product/" + productId)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", Matchers.equalTo(productId))
                .body("name", Matchers.equalTo("Samsung TV - Updated"))
                .body("description", Matchers.equalTo("Updated description for Samsung TV"))
                .body("price", Matchers.equalTo(2200));
    }

    @Test
    void deleteProductTest() {
        String requestBodyCreate = """
        {
             "name" : "Samsung TV",
             "description" : "Samsung TV - Model 2024",
             "price" : 2000
        }
    """;

        String productId = RestAssured.given()
                .contentType("application/json")
                .body(requestBodyCreate)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .path("id");

        // Deleting the product
        RestAssured.given()
                .when()
                .delete("/api/product/" + productId)
                .then()
                .log().all()
                .statusCode(204);


    }


}

