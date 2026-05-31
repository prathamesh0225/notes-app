package base;

import org.testng.annotations.*;
import drivers.DriverManager;
import utils.*;

public class BaseTest {
    protected SelfHealingDriver driver;
    @BeforeMethod
    public void setUp() {
        driver = new SelfHealingDriver(DriverManager.getDriver());
        driver.get(ConfigReader.get("BASE_URL"));
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}