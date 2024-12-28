import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RestAssuredTest {

    @Test
    public void testRestAssured(){
        Map<String, String> params = new HashMap<>();
        params.put("name", "John");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String name = response.get("answer");
        if (name == null) {
            System.out.println("The key answer2 is absent");
        } else {
        System.out.println(name);
    }
        }


    @Test
    public void testGetText(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }


    @Test
    public void testCheckType() {
        Map<String, Object> body = new HashMap<>();
        body.put("param1", "value1");
        body.put("param2", "value2");

        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();
    }



    @Test
    public void testStatusCode(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }

    @Test
    public void testHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1", "value1");
        headers.put("myHeader2", "value2");

        Response response = RestAssured
                .given()
                .headers(headers)
                .when()
                .get("https://playground.learnqa.ru/api/show_all_headers")
                .andReturn();
        response.prettyPrint();

        //Headers responseHeaders = response.getHeaders();
        //System.out.println(responseHeaders);
    }



    @Test
    public void testCookie(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass1");

        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");

        Map<String, String> cookie = new HashMap<>();
        if (responseCookie != null){
            cookie.put("auth_cookie", responseCookie);
        }

        Response responseForCheck = RestAssured
                .given()
                .body(data)
                .cookies(cookie)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();
    }

    @Test
    public void testFor200 () {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        assertEquals(200, response.statusCode(), "Unexpected status code");
    }



    @Test
    public void testFor404() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map2")
                .andReturn();
        assertEquals(404, response.statusCode(), "Unexpected status code");
    }


}
