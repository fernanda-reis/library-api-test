package com.librarytest.LibraryTest.User;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;

public class UserDefinitions {

    private RequestSpecification request = RestAssured.given()
            .baseUri("http://localhost:8080")
            .contentType(ContentType.JSON);

    private Response response = null;
    private User user = new User();

    @Given("user is unknown")
    public void userIsUnknown(){
        user = new User();
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setEmail(user.getName() + "@email.com");
        user.setPassword(RandomStringUtils.randomNumeric(5));
    }

    @Given("user without valid email")
    public void userWithoutValidEmail(){
        user = new User();
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setEmail(user.getName() + "email.com");
        user.setPassword(RandomStringUtils.randomNumeric(5));
    }

    @Given("user is registered")
    public void userIsRegistered(){
        user = new User();
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setEmail(user.getName() + "@email.com");
        user.setPassword(RandomStringUtils.randomNumeric(5));
        response = request.body(user).when().post("/user");
        response.then().statusCode(201);
    }

    @When("user is registered with success")
    public void userIsRegisteredWithSuccess(){
        response = request.body(user).when().post("/user");
        response.then().statusCode(201);
    }

    @When("user fails to register")
    public void userFailsToRegister(){
        response = request.body(user).when().post("/user");
        response.then().statusCode(400);
    }

    @When("user logins with valid password")
    public void userLoginWithValidPassword(){
        response = request.body(user).when().post("/login");
        response.then().statusCode(200);
    }

    @When("user logins with invalid password")
    public void userLoginWithInvalidPassword(){
        user.setPassword(user.getPassword() + "1");
        response = request.body(user).when().post("/login");
    }

    @Then("user is known")
    public void userIsKnown(){
        response = request.when().get("/user/name/" + user.getName());
        response.then().statusCode(200);
        String name = response.jsonPath().get("[0].name");
        Assertions.assertEquals(user.getName(), name);
    }

    @Then("user is still unknown")
    public void userStillUnknown(){
        response = request.when().get("/user/name/" + user.getName());
        response.then().statusCode(404);
    }

    @Then("user gets an access token")
    public void userGetsAccessToken(){
        String token = response.jsonPath().get("token");
        Assertions.assertNotNull(token);
    }

    @Then("user fails to login")
    public void userFailsLogin(){
        response.then().statusCode(403);
    }

}
