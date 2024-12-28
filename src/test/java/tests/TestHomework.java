package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

public class TestHomework extends BaseTestCase {

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



    @Test
    public void homeworkEx9() {
        List<String> passwords = new ArrayList<>();
        passwords.add("123456");
        passwords.add("123456789");
        passwords.add("qwerty");
        passwords.add("password");
        passwords.add("1234567");
        passwords.add("12345678");
        passwords.add("12345");
        passwords.add("iloveyou");
        passwords.add("111111");
        passwords.add("123123");
        passwords.add("abc123");
        passwords.add("qwerty123");
        passwords.add("1q2w3e4r");
        passwords.add("qwertyuiop");
        passwords.add("555555");
        passwords.add("lovely");
        passwords.add("7777777");
        passwords.add("welcome");
        passwords.add("888888");
        passwords.add("princess");
        passwords.add("dragon");
        passwords.add("password1");
        passwords.add("123qwe");


        Map<String, String> authParams = new HashMap<>();

        for (String pass : passwords) {
            authParams.put("login", "super_admin");
            authParams.put("password", pass);
            Response authResponse = RestAssured
                    .given()
                    .body(authParams)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String authCookie = authResponse.getCookie("auth_cookie");


            Map<String, String> cookie = new HashMap<>();
            cookie.put("auth_cookie", authCookie);

            Response checkAuthCookie = RestAssured
                    .given()
                    .body(authParams)
                    .cookies(cookie)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            String checkResponse = checkAuthCookie.body().asString();
            if (checkResponse.equals("You are authorized")) {
                System.out.println(checkResponse);
                System.out.println(pass);
                break;
            }


        }

    }


    @Test
    public void homeworkEx10() {

        String text = "Java it's cool!";
        int lengthText = text.length();
        assertEquals(15,lengthText,"Text more than 15 characters ");
    }


    @Test
    public void homeworkEx11() {

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        String cookie = response.getCookie("HomeWork");
        assertEquals("hw_value", cookie, "Wrong cookie value");

    }




    @Test
    public void homeworkEx12() {

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        String header = response.getHeader("x-secret-homework-header");
        assertEquals("Some secret value", header, "Wrong header value");

    }



}













