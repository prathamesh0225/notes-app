package base;
import org.testng.annotations.BeforeClass;
import io.restassured.RestAssured;
import utils.ConfigReader;

public class BaseApiTest {
    public static String token;
    public static String noteId;
    @BeforeClass
    public static void setupApi() {
        RestAssured.baseURI = ConfigReader.getProperty("apiBaseUrl");
    }
}