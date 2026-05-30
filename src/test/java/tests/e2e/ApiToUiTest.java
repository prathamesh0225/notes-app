package tests.e2e;

import api.AuthApi;
import base.AuthenticatedBaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelUtils;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiToUiTest extends AuthenticatedBaseTest {
    @DataProvider(name = "deleteNoteData")
    public Object[][] deleteNoteData() {

        Object[][] data = ExcelUtils.getSheetData("e2e");
        return new Object[][]{
                data[0]
        };
    }
    @Test(dataProvider = "deleteNoteData")
    public void verifyUiAfterApiDeletion(String title,String description,String category) throws InterruptedException {

        Assert.assertNotNull(title,"No note visible in UI");
        String token = AuthApi.getAuthToken();
        Response getResponse =
                        given()
                        .header("x-auth-token",token)
                        .when()
                        .get("https://practice.expandtesting.com/notes/api/notes");
        getResponse.then()
                   .statusCode(200);

        // find note id
        List<Map<String, Object>> notes = getResponse.jsonPath().getList("data");

        Map<String, Object> note = notes.stream()
                                        .filter(n -> title.equals(n.get("title")))
                                        .findFirst()
                                        .orElse(null);

        Assert.assertNotNull(note,"Note not found in API");
        String noteId = note.get("id").toString();

        // delete note
        Response deleteResponse = given()
                        .header("x-auth-token",token)
                        .when()
                        .delete("https://practice.expandtesting.com/notes/api/notes/"+ noteId);

        deleteResponse.then()
                .statusCode(200);

        driver.navigate().refresh();
        Assert.assertFalse(notesPage.isNotePresent(title),"Deleted note still visible in UI");
    }
}