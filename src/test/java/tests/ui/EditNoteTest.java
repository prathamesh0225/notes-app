package tests.ui;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import base.AuthenticatedBaseTest;
import utils.ExcelUtils;

public class EditNoteTest extends AuthenticatedBaseTest {
    @DataProvider(name = "editNoteData")
    public Object[][] editNoteData() {

        Object[][] data = ExcelUtils.getSheetData("e2e");
        return new Object[][]{
                data[1]
        };
    }

    @Test(dataProvider = "editNoteData")
    public void editNote(String title,String description,String category) throws InterruptedException {

        String updatedTitle = "Updated Note";
        String updatedDescription = "This is an updated description.";
        notesPage.clickAddNote();
        notesPage.enterTitle(title);
        notesPage.enterDescription(description);
        notesPage.selectCategory(category);
        notesPage.clickCreateButton();
        
        Assert.assertTrue(notesPage.isNotePresent(title));
        notesPage.clickEditButton(title);
        notesPage.clearTitleField();
        notesPage.enterTitle(updatedTitle);
        notesPage.clearDescriptionField();
        notesPage.enterDescription(updatedDescription);
        notesPage.clickSaveButton();
        Thread.sleep(2000);
        Assert.assertTrue(notesPage.isNotePresent(updatedTitle));
    }
}