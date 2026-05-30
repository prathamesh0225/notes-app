package tests.ui;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.LoginPage;
import utils.ExcelUtils;

public class LoginTest extends BaseTest {
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return ExcelUtils.getSheetData("Login");
    }

@Test(dataProvider = "loginData")
public void loginTest(String email,String password,String result,String expectedMessage) {

    LoginPage loginPage = new LoginPage(driver);

    loginPage.openLoginPage();
    loginPage.enterEmail(email);
    loginPage.enterPassword(password);
    loginPage.clickLogin();

    if (result.equalsIgnoreCase("pass")) {
        Assert.assertTrue(loginPage.getPageText().contains(expectedMessage));
    }
    else {
        Assert.assertTrue(loginPage.getPageText().contains(expectedMessage));
    }
}
}