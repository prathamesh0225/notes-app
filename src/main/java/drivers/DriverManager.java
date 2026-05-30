package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {

        if (driver.get() == null) {

            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            options.addArguments("--incognito");
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-infobars");

            driver.set(new ChromeDriver(options));
        }

        return driver.get();
    }

    public static void quitDriver() {

        if (driver.get() != null) {

            driver.get().quit();
            driver.remove();
        }
    }
}