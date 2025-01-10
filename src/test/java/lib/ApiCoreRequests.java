package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }


    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }


    @Step("Make a GET-request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }



    @Step("Make a POST-request")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }



    @Step("Create user POST-request")
    public Response createUserPostRequest(String url, Map<String, String> userData) {
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }



    @Step("User GET-request")
    public Response getUserWithId3Data(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();

    }



    @Step("JSON Create user POST-request")
    public JsonPath createUserPostRequestJson(String url, Map<String, String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .jsonPath();
    }


    @Step("Edit user PUT-request")
    public Response editUserPutRequest(String url, String header, String cookie, Map<String, String> editData){
        return given()
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url)
                .andReturn();
    }



    @Step("Make User DELETE-request")
    public Response userDeleteRequest(String url, String header, String cookie){
        return  given()
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .delete(url)
                .andReturn();
    }


}
