package tests.e2e;

import base.AuthenticatedBaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import api.AuthApi;
import api.NotesApi;
import utils.ExcelUtils;
import java.util.List;
import java.util.Map;

public class UiToApiTest extends AuthenticatedBaseTest {
    @DataProvider(name = "notesData")
    public Object[][] notesData() {
        Object[][] data = ExcelUtils.getSheetData("e2e");
        return new Object[][]{
                data[0]
        };
    }

    @Test(dataProvider = "notesData")
    public void verifyNoteCreatedInUiAppearsInApi(String title,String description,String category) {

        notesPage.clickAddNote();
        notesPage.enterTitle(title);
        notesPage.enterDescription(description);
        notesPage.selectCategory(category);
        notesPage.clickCreateButton();

        Assert.assertTrue(notesPage.getPageText().contains(title),"Note creation failed in UI");

        String token = AuthApi.getAuthToken();
        Response response = NotesApi.getNotes(token);

        response.then()
                .statusCode(200);

        // find note
        List<Map<String, Object>> notes = response.jsonPath().getList("data");

        Map<String, Object> apiNote = notes.stream()
                                .filter(n -> n.get("title")
                                .toString()
                                .equals(title))
                                .findFirst()
                                .orElse(null);

        Assert.assertNotNull(apiNote,"Note not found in API");
        // field validation
        Assert.assertEquals(apiNote.get("title"),title);
        Assert.assertEquals(apiNote.get("description"),description);
        Assert.assertEquals(apiNote.get("category"),category);
    }
}