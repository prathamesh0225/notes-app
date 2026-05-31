package tests.ui;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import base.AuthenticatedBaseTest;
import utils.ExcelUtils;

public class DeleteNoteTest extends AuthenticatedBaseTest {
    @DataProvider(name = "deleteNoteData")
    public Object[][] deleteNoteData() {

        Object[][] data = ExcelUtils.getSheetData("Notes");
        return new Object[][]{
                data[0]
        };
    }

    @Test(dataProvider = "deleteNoteData",dependsOnMethods = "tests.ui.CreateNoteTest.createNote")
    public void noteDeletion(String title,String description,String category,String result,String expectedMessage)throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(notesPage.isNotePresent(title),"Note not found before delete");

        notesPage.clickDeleteButton(title);
        notesPage.confirmDelete();
        
        Thread.sleep(2000);
        driver.navigate().refresh();
        Thread.sleep(2000);
        Assert.assertFalse(notesPage.isNotePresent(title),"Note still found after delete");
    }
}