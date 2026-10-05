package org.example;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class ApiTestcases {
    @BeforeClass
    public void setup() {
        RestAssured.config = RestAssuredConfig.config().httpClient(
                HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 5000)
                        .setParam("http.socket.timeout", 5000)
        );
    }

    @Test
    public void verifyGetUser() {
        RestAssured.baseURI = "https://reqres.in/api/users";
        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/2");
// Print response
        System.out.println(response.asPrettyString());
// Validate status code
        Assert.assertEquals(response.getStatusCode(), 200);
// Validate response body
        Assert.assertEquals(
                response.jsonPath().getString("data.first_name"),
                "Janet");
        Assert.assertEquals(
                response.jsonPath().getString("data.last_name"),
                "Weaver");
    }

    @Test
    public void createUser() {
        RestAssured.baseURI = "https://reqres.in";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Shubham");
        requestBody.put("job", "QA Engineer");
        Response response =
                        given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .log().all()
                        .when()
                        .post("/api/users")
                        .then()
                        .log().all()
                        .extract()
                        .response();
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.jsonPath().getString("name"), "Shubham");
        Assert.assertEquals(response.jsonPath().getString("job"), "QA Engineer");
        String userId = response.jsonPath().getString("id");
        Assert.assertNotNull(userId);
        System.out.println("Created User ID: " + userId);
    }
}
