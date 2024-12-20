import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TestHomework {

    @Test
    public void testHelloFrom(){
        System.out.println("Hello from Dmitry!");
    }


    @Test
    public void homeworkEx5(){

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        Object answer = response.get("messages.message[1]");
        System.out.println(answer);

    }


    @Test
    public void homeworkEx6(){

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String location = response.getHeader("Location");
        System.out.println(location);

    }


    @Test
    public void homeworkEx7(){

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        int statusCode = response.getStatusCode();
        String location = response.getHeader("Location");
        System.out.println(statusCode);
        System.out.println(location);

        while (statusCode != 200) {
                     response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(location)
                    .andReturn();
            statusCode = response.getStatusCode();
            location = response.getHeader("Location");
            System.out.println(statusCode);
            System.out.println(location);

        }
    }


}










