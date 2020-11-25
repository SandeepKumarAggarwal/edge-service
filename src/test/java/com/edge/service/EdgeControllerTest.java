package com.example.sample.test;

import org.junit.Test;

import org.hamcrest.Matchers;
import io.restassured.RestAssured;

public class EdgeControllerTest {


    /**
     * User 123452 - First API - Ending with error once no of requests shoots more than 5 in 2 minutes window
     *
     */
    @Test
    public void whenRequestExceedsCapacity_thenReturnTooManyRequestExceptionForUser() {

        for(int hitCount=0; hitCount < 5; hitCount++) {
            RestAssured.given().header("clientID", "123452")
                .when().get("http://localhost:8081/firstapi/summary")
                .then().statusCode(200);
        }

        RestAssured.given().header("clientID", "123452")
            .when().get("http://localhost:8081/firstapi/summary")
            .then().statusCode(429);
    }

    /**
     * User 123452 - Second API - Ending with error once no of requests shoots more than default limits (2 in a minute)
     *
     */
    @Test
    public void whenRequestExceedsCapacity_thenReturnTooManyRequestExceptionForUserWithDefaultLimits() {

        for(int hitCount=0; hitCount < 2; hitCount++) {
            RestAssured.given().header("clientID", "123452")
                .when().get("http://localhost:8081/secondapi/summary")
                .then().statusCode(200);
        }

        RestAssured.given().header("clientID", "123452")
            .when().get("http://localhost:8081/secondapi/summary")
            .then().statusCode(429);
    }

    /**
     * Default Settings - user abc - First API - Success Flow
     *
     */
    @Test
    public void whenRequestNotExceedingCapacity_thenReturnOkResponseForDefaultRateLimit() {
        RestAssured.given().header("clientID", "abc")
            .when().get("http://localhost:8081/firstapi/summary")
            .then().statusCode(200);
    }

    /**
     * Default Settings - user def - First API - Ending with error once no of requests shoots more than 2 in a minute window
     *
     */
    @Test
    public void whenRequestExceedsCapacity_thenReturnTooManyRequestExceptionForDefaultRateLimit() {

        for(int hitCount=0; hitCount < 2; hitCount++) {
            RestAssured.given().header("clientID", "def")
                .when().get("http://localhost:8081/firstapi/summary")
                .then().statusCode(200);
        }

        RestAssured.given().header("clientID", "def")
            .when().get("http://localhost:8081/firstapi/summary")
            .then().statusCode(429);
    }

}
