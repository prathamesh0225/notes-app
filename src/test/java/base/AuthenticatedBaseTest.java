package base;

import org.testng.annotations.BeforeMethod;
import pages.LoginPage;
import pages.NotesPage;
import utils.ConfigReader;

public class AuthenticatedBaseTest extends BaseTest {
    protected NotesPage notesPage;
    @BeforeMethod
    public void loginToApplication() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginPage();
        loginPage.enterEmail(ConfigReader.get("NOTES_EMAIL"));
        loginPage.enterPassword(ConfigReader.get("NOTES_PASSWORD"));
        loginPage.clickLogin();
        notesPage = new NotesPage(driver);
    }
}