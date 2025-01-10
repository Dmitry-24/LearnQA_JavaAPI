package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


@Epic("User delete cases")
@Feature("User delete")


public class UserDeleteTests extends BaseTestCase {


    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Description("This test try delete user with ID 2")
    @DisplayName("Negative test with delete user ID 2")
    @Test
    public void deleteUserWithId2() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", authData);

        String token = responseGetAuth.getHeader("x-csrf-token");
        String cookie = responseGetAuth.getCookie("auth_sid");

        Response responseDeleteUser = apiCoreRequests.userDeleteRequest(baseUrl + "/user/" + 2, token, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertJsonByName(responseDeleteUser, "error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    }


    @Test
    @Description("This test try delete just created user")
    @DisplayName("Positive test delete just created user")
    public void UserDeleteTest() {
        //CREATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.createUserPostRequestJson(baseUrl + "/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //LOGIN USER
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", authData);

        String token = responseGetAuth.getHeader("x-csrf-token");
        String cookie = responseGetAuth.getCookie("auth_sid");

        //DELETE
        Response responseDeleteUser = apiCoreRequests.userDeleteRequest(baseUrl + "/user/" + userId, token, cookie);

        Response responseUserGet = apiCoreRequests.makeGetRequest(baseUrl + "/user/" + userId, token, cookie);

        Assertions.assertResponseCodeEquals(responseUserGet, 404);
        Assertions.assertResponseTextEquals(responseUserGet, "User not found");
    }



    @Description("This test try delete another user")
    @DisplayName("Negative test delete another user")
    @Test
    public void DeleteAsOtherUserTest() {
        //GENERATE AUTH USER
        Map<String, String> userAuth = DataGenerator.getRegistrationData();
        JsonPath responseCreateUserAuth = apiCoreRequests.createUserPostRequestJson(baseUrl + "/user/", userAuth);
        String authUserId = responseCreateUserAuth.getString("id");
        System.out.println(authUserId);

        //GENERATE EDIT USER
        Map<String, String> userEdit = DataGenerator.getRegistrationData();
        JsonPath responseCreateUserEdit = apiCoreRequests.createUserPostRequestJson(baseUrl + "/user/", userEdit);
        String editUserId = responseCreateUserEdit.getString("id");
        System.out.println(editUserId);

        //LOGIN AUTH USER
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userAuth.get("email"));
        authData.put("password", userAuth.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", authData);

        String token = responseGetAuth.getHeader("x-csrf-token");
        String cookie = responseGetAuth.getCookie("auth_sid");

        //DELETE

        Response responseDeleteUser = apiCoreRequests.userDeleteRequest(baseUrl + "/user/" + editUserId, token, cookie);


        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertJsonByName(responseDeleteUser, "error", "This user can only delete their own account.");

    }






}
