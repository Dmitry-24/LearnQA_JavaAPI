import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

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

}
