package api;

import static io.restassured.RestAssured.*;
import java.io.ByteArrayInputStream;
import io.qameta.allure.Allure;
import io.restassured.response.Response;

public class NotesApi {
    public static Response createNote(String token,String title,String description,String category) {

        String payload = "{"
                        + "\"title\":\"" + title + "\","
                        + "\"description\":\"" + description + "\","
                        + "\"category\":\"" + category + "\""
                        + "}";

        Response response =given()
                        .header("x-auth-token",token)
                        .contentType("application/json")
                        .body(payload)
                        .when()
                        .post("/notes");

        Allure.addAttachment("Create Note API Response","application/json",new ByteArrayInputStream(response.asPrettyString().getBytes()),".json");
        return response;
    }

    public static Response getNotes(String token) {

        Response response = given()
                        .header("x-auth-token",token)
                        .when()
                        .get("https://practice.expandtesting.com/notes/api/notes");

        Allure.addAttachment("Get Notes API Response","application/json", new ByteArrayInputStream(response.asPrettyString().getBytes()),".json");
        return response;
    }

    public static Response deleteNote(String token, String noteId) {

        Response response =given()
                        .header("x-auth-token",token)
                        .when()
                        .delete("/notes/" + noteId);
        Allure.addAttachment("Delete Note API Response","application/json",
                new ByteArrayInputStream(response.asPrettyString().getBytes()),".json");

        return response;
    }
}