package com.krce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class FakeAPITest {

    @BeforeClass
    public void setup(){
        RestAssured.baseURI="https://api.escuelajs.co/api/v1";
    }

    @Test
    public void testGetProducts(){
        RestAssured.given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0));
    }

    @Test
    public void testFilterProductsByPrice(){
        RestAssured.given()
                .queryParam("price",100)
                .when()
                .get("/products/")
                .then()
                .statusCode(200)
                .body("[0].price",Matchers.equalTo(100));
    }

    @Test
    public void testFilterProductsByPriceRange(){
        RestAssured.given()
                .queryParam("price_min",80)
                .queryParam("price_max",90)
                .when()
                .get("/products/")
                .then()
                .statusCode(200)
                .body("[0].price",Matchers.greaterThanOrEqualTo(80))
                .body("[0].price",Matchers.lessThanOrEqualTo(90));
    }
    @Test
    public void testFilterProductsByTitle() {
        RestAssured.given()
                .queryParam("title", "Classic Blue Baseball Cap")
                .when()
                .get("/products/")
                .then()
                .statusCode(200)
                .body("[0].title", Matchers.equalTo("Classic Blue Baseball Cap"));
    }
    @Test
    public void testGetCategories(){
        RestAssured.given()
                .when()
                .get("/categories")
                .then()
                .statusCode(200)
                .body("$",Matchers.instanceOf(List.class));
    }
    @Test
    public void testGetCategoriesById(){
        RestAssured.given()
                .pathParam("id",1)
                .when()
                .get("/categories/{id}")
                .then()
                .statusCode(200)
                .body("id",Matchers.equalTo(1));
    }
    @Test
    public void testCreateCategories(){
        String body = """
                {
                    "name": "Ovizs",
                    "image": "https://placeimg.com/640/480/any"
                }
                """;
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/categories")
                .then()
                .log().all()
                .statusCode(201)
                .body("name",Matchers.equalTo(
                        "Ovizs"))
                .body("image",Matchers.equalTo("https://placeimg.com/640/480/any"));
    }
}
