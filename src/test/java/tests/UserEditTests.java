package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;



import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;




@Epic("User edit cases")
@Feature("User edit")
public class UserEditTests extends BaseTestCase {


    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    @Description("This test try edit user")
    @DisplayName("Positive test")
    @Test
    public void testEditJustCreatedTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post(baseUrl + "/user")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post(baseUrl + "/user/login")
                .andReturn();

        //EDIT
        String newName = "Changed Name";
        Map <String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put(baseUrl + "/user/" + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get( baseUrl + "/user/" + userId)
                .andReturn();

        Assertions.assertJsonByName(responseUserData, "firstName", newName);

    }


    @Description("This test try edit not authorization user")
    @DisplayName("Negative test with not authorization user")
    @Test
    public void EditUnAuthUserTest(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.createUserPostRequestJson(baseUrl + "/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.editUserPutRequest(baseUrl + "/user/" + userId,
                "null",
                "null",
                editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "Auth token not supplied");
    }





    @Description("This test try edit another user")
    @DisplayName("Negative test with another user")
    @Test
    public void EditAsOtherUserTest() {
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

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.editUserPutRequest(baseUrl + "/user/" + editUserId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "This user can only edit their own data.");

    }



    @Description("This test try edit wrong email")
    @DisplayName("Negative test with wrong email")
    @Test
    public void EditWrongEmailTest(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.createUserPostRequestJson(baseUrl + "/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", authData);

        //EDIT
        String newEmail = "WrongEmail.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests.editUserPutRequest(baseUrl + "/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "Invalid email format");
    }




    @Description("This test try edit short firstName")
    @DisplayName("Negative test with short firstName")
    @Test
    public void EditShortFirstNameTest(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.createUserPostRequestJson(baseUrl + "/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", authData);

        //EDIT
        String firstName = "D";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", firstName);

        Response responseEditUser = apiCoreRequests.editUserPutRequest(baseUrl + "/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "The value for field `firstName` is too short");
    }



}
