package pages;

import org.openqa.selenium.*;
import utils.*;

public class LoginPage {

    SelfHealingDriver driver;
    WaitUtils waitUtils;

    By loginMenuButton = By.xpath("//a[text()='Login']");
    By emailField = By.id("email21");
    By passwordField = By.id("password");
    By loginButton = By.xpath("//button[text()='Login']");
    By errorMessage = By.className("toast-body");
    By blankErrorMsg = By.className("invalid-feedback");

    public LoginPage(SelfHealingDriver driver) {
        this.driver = driver;
        waitUtils = new WaitUtils(driver.getWrappedDriver());
    }

    public void openLoginPage() {
        WebElement btn = driver.findElement(loginMenuButton);
        waitUtils.scrollAndClickUsingJS(btn);
    }

    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        WebElement btn = driver.findElement(loginButton);
        waitUtils.scrollAndClickUsingJS(btn);
    }

    public String getPageText() {
        waitUtils.waitForElementIfPresent(errorMessage);
        return driver.getPageSource();
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    public String getBlankErrorMsg() {
        return driver.findElement(blankErrorMsg).getText();
    }
}