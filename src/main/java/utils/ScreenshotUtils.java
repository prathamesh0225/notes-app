package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {

    public static String captureScreenshot(WebDriver driver,String testName) {

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        String path = "target/screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";

        try {

            File dest = new File(path);
            dest.getParentFile().mkdirs();
            Files.copy(src.toPath(),dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static byte[] captureScreenshotBytes(WebDriver driver) {
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
}
}