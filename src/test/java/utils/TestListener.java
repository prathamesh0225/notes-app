package utils;

import drivers.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;

import java.io.ByteArrayInputStream;

public class TestListener implements ITestListener {
    @Override
    public void onTestStart(ITestResult result) {
        Allure.step("STARTED: " + result.getMethod().getMethodName());
    }
    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step("PASSED: " + result.getMethod().getMethodName());
    }
    @Override
    public void onTestFailure(ITestResult result) {

        try {
            Allure.step("FAILED: " + result.getMethod().getMethodName());
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Failure Screenshot",new ByteArrayInputStream(screenshot));

            if (result.getThrowable() != null) {
                Allure.addAttachment("Error Stacktrace",result.getThrowable().toString());
            }
        } catch (Exception e) {
            Allure.addAttachment("Listener Error", e.toString());
        }
    }
    @Override
    public void onFinish(ITestContext context) {
        HealingReport.getInstance().printSummary();
    }
}