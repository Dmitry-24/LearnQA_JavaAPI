import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TestHomework {

    @Test
    public void testHelloFrom() {
        System.out.println("Hello from Dmitry!");
    }


    @Test
    public void homeworkEx5() {

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        Object answer = response.get("messages.message[1]");
        System.out.println(answer);

    }


    @Test
    public void homeworkEx6() {

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
    public void homeworkEx7() {

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

    @Test
    public void homeworkEx8() throws InterruptedException {

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        int sec = response.get("seconds");
        int millsec = sec * 1000;
        String token = response.get("token");
        System.out.println("Времени до завершения задачи " + sec + " сек");
        System.out.println("Значение токена = " + token);

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        JsonPath responseStatus = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String status = responseStatus.get("status");
        String result = responseStatus.get("result");

        if (!Objects.equals(status, "Job is ready")) {
            System.out.println("Ожидание...");
            Thread.sleep(millsec);
            responseStatus = RestAssured
                    .given()
                    .queryParams(params)
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();
            result = responseStatus.get("result");
            status = responseStatus.get("status");
            System.out.println("Текущий статус задачи = " + status);
            System.out.println("Результат задачи = " + result);
        } else {
            System.out.println("Текущий статус задачи = " + status);
            System.out.println("Результат задачи = " +result);
        }

    }

}










