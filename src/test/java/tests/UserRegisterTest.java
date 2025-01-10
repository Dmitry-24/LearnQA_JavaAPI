package tests;

import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

import  lib.ApiCoreRequests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {


    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    @Description("Try create user with existing email")
    @DisplayName("Negative test with existing email")
    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";


        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);


        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post(baseUrl + "/user")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");

    }

    @Test
    public void testCreateUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();


        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasKey(responseCreateAuth, "id");

    }

    @Description("Try create user with wrong email")
    @DisplayName("Negative test with wrong email")
    @Owner("My homework test")
    @Test
    public void testCreateUserWithWrongEmail() {
        String email = "vinkotovexample.com";


        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.createUserPostRequest(baseUrl + "/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");

    }


    @Description("Try create user with short username")
    @DisplayName("Negative test with wrong username")
    @Owner("My homework test")
    @Test
    public void testCreateUserWithShortUsername() {
        String username = "a";

        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.createUserPostRequest(baseUrl + "/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
    }




    @Description("Try create user with long username")
    @DisplayName("Negative test with wrong username")
    @Owner("My homework test")
    @Test
    public void testCreateUserWithLongUsername() {
        String username = "Как принято считать, явные признаки победы институционализации набирают популярность среди определенных слоев населения, а значит, должны быть обнародованы. Предварительные выводы неутешительны: глубокий уровень погружения не даёт нам иного выбора, кроме определения вывода текущих активов.";

        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.createUserPostRequest(baseUrl + "/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
    }



    @Description("Try create user with empty field")
    @DisplayName("Negative test with empty field")
    @Owner("My homework test")
    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserWithEmptyField(String field) {

        Map<String, String> userData = new HashMap<>();
        userData.put(field, null);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.createUserPostRequest(baseUrl + "/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + field);
    }


}
