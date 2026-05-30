package tests.ui;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import base.AuthenticatedBaseTest;
import utils.ExcelUtils;

public class CreateNoteTest extends AuthenticatedBaseTest {
    @DataProvider(name = "notesData")
    public Object[][] notesData() {
        return ExcelUtils.getSheetData("Notes");
    }

    @Test(dataProvider = "notesData")
    public void createNote(String title, String description, String category, String result, String expectedMessage) {

        notesPage.clickAddNote();
        notesPage.enterTitle(title);
        notesPage.enterDescription(description);
        notesPage.selectCategory(category);
        notesPage.clickCreateButton();

        String pageText = notesPage.getPageText();

        if (result.equalsIgnoreCase("pass")) {
            Assert.assertTrue(pageText.contains(expectedMessage));
        }
        else {
            Assert.assertTrue(pageText.contains(expectedMessage));
        }
    }
}