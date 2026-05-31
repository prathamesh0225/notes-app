package api;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayInputStream;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import utils.ConfigReader;
import base.BaseApiTest;

public class AuthApi extends BaseApiTest {
    public static String getAuthToken() {

        baseURI = ConfigReader.get("API_BASE_URL");
        Map<String, String> body = new HashMap<>();
        body.put("email", ConfigReader.get("NOTES_EMAIL"));
        body.put("password", ConfigReader.get("NOTES_PASSWORD"));

        Response response = given()
                        .header("Content-Type","application/json")
                        .body(body)
                .when()
                .post("/users/login");

        System.out.println(response.asPrettyString());

        Allure.addAttachment("Login Response","application/json",new ByteArrayInputStream(response.asPrettyString().getBytes()),".json");

        response.then()
                .statusCode(200);

        token = response.jsonPath()
                        .getString("data.token");
        return token;
    }
}