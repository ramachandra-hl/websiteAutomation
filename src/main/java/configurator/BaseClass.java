package configurator;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class BaseClass {

    public WebDriver driver;
    public String env;
    public String url;
    public static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    public void initializeDriver() {
        env = dotenv.get("ENVIRONMENT", "preProd");

        switch (env.toLowerCase()) {
            case "preprod":
                url = dotenv.get("PREPROD_URL");
                break;

            case "prod":
                url = dotenv.get("PROD_URL");
                break;

            default:
                throw new RuntimeException("❌ Invalid ENVIRONMENT: " + env);
        }

        if (url == null || url.isEmpty()) {
            throw new RuntimeException("❌ URL is missing in .env file for ENVIRONMENT = " + env);
        }

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get(url);
    }

    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
