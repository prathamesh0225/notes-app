package tests.api;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.lessThan;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import api.AuthApi;
import api.NotesApi;
import base.BaseApiTest;
import io.restassured.response.Response;
import utils.ExcelUtils;

public class NotesApiTest extends BaseApiTest {
    @BeforeClass
    public void apiLogin() {
        token = AuthApi.getAuthToken();
    }

    @DataProvider(name = "notesData")
    public Object[][] notesData() {
        
        Object[][] data = ExcelUtils.getSheetData("Notes");

        return new Object[][]{
                {
                        data[0][0],
                        data[0][1],
                        data[0][2]
                }
        };
    }

    @Test(priority = 1, dataProvider = "notesData")
    public void createNoteTest(String title,String description,String category) {

        Response response = NotesApi.createNote(token, title, description, category);
        response.then()
                .statusCode(200)
                .time(lessThan(2000L));

        noteId = response.jsonPath().getString("data.id");

        System.out.println("Created Note ID: " + noteId);
    }

    @Test(priority = 2)
    public void seeNotesList() {

        Response response = NotesApi.getNotes(token);

        response.then()
                .statusCode(200)
                .time(lessThan(2000L));

        System.out.println("Notes list:");
        System.out.println(response.asPrettyString());
    }

    @Test(priority = 3)
    public void deleteNoteApiTest() {

        Response response = NotesApi.deleteNote(token, noteId);
        response.then()
                .statusCode(200)
                .time(lessThan(2000L));

        System.out.println("Deleted note ID: " + noteId);
    }

    @Test(priority = 4)
    public void verifyNotesAfterDelete() {

        Response response = NotesApi.getNotes(token);

        response.then()
                .statusCode(200)
                .time(lessThan(2000L));

        long responseTime = response.getTime();

        System.out.println("API Response Time: " + responseTime + " ms");
        System.out.println("Notes after delete:");
        System.out.println(response.asPrettyString());
        Assert.assertFalse(
                response.asPrettyString()
                        .contains(noteId),
                "Deleted note still present"
        );
    }

    @Test(priority = 5)
    public void verifyNoTokenAccessBlock() {

        Response response = given()
                        .when()
                        .get("/notes");

        System.out.println("Unauthenticated Response:");
        System.out.println(response.asPrettyString());

        response.then()
                .statusCode(401)
                .time(lessThan(2000L));

        Assert.assertEquals(response.getStatusCode(),401);
    }

    @Test(priority = 6)
    public void verifyInvalidNoteIdReturns404() {

        Response response = given()
                        .header("x-auth-token", token)
                        .when()
                        .delete("/notes/nonexistentid123");

        System.out.println("Invalid Note ID Response:");
        System.out.println(response.asPrettyString());

        response.then()
                .statusCode(400)
                .time(lessThan(2000L));

        Assert.assertEquals(response.getStatusCode(),400);
    }
}