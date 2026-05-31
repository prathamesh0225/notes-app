package tests.api;

import static io.restassured.RestAssured.*;
import java.io.ByteArrayInputStream;
import base.BaseApiTest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthApiTest extends BaseApiTest {

    @Test(priority = 1)
    public void loginApiTest() {
        String payload = "{"
                                    + "\"email\":\""
                                    + ConfigReader.get("NOTES_EMAIL")
                                    + "\","
                                    + "\"password\":\""
                                    + ConfigReader.get("NOTES_PASSWORD")
                                    + "\""
                                    + "}";
        setupApi();
        Response response = given()
                        .header("Content-Type","application/json")
                        .body(payload)
                        .when()
                        .post("/users/login");
                response.then()
                        .statusCode(200);

        System.out.println("Login Response:");
        System.out.println(response.asPrettyString());
        token = response.jsonPath().getString("data.token");

        Allure.addAttachment("Login Response","application/json",new ByteArrayInputStream(response.asPrettyString().getBytes()),".json");
        Assert.assertNotNull(token);
    }
}