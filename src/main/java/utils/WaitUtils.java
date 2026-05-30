package utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {

    WebDriver driver;
    public WaitUtils(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement waitForElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    public void waitForInvisibility(By locator) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
}

public WebElement waitForElementIfPresent(By locator) {
    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

    } catch (Exception e) {
        return null;
    }
}
public void scrollAndClickUsingJS(WebElement element) {

    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].scrollIntoView({block:'center'});",element);
    js.executeScript("arguments[0].click();",element);
}
}