package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import  lib.ApiCoreRequests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


@Epic("User data get cases")
@Feature("User data get")
public class UserGetTests extends BaseTestCase {


    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Description("This test get username only if user unauthorized")
    @DisplayName("Test get only username")
    @Test
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get(baseUrl + "/user/2")
                .andReturn();
        Assertions.assertJsonHasKey(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "LastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }


    @Description("This test get full userdata if user authorized")
    @DisplayName("Test get full userdata")
    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post(baseUrl + "/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");


        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get(baseUrl + "/user/2")
                .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);

    }



    @Description("This test get username only if user authorized and get another user data")
    @DisplayName("Test get another user data")
    @Owner("My homework test")
    @Test
    public void testGetUserDetailsAuthAsAnotherUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(baseUrl + "/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");


        Response responseUserData = apiCoreRequests
                .getUserWithId3Data(baseUrl + "/user/3", header, cookie);


        Assertions.assertJsonHasKey(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "LastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }












}
