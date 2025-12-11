package configurator;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BaseClass {

    public WebDriver driver;
    public static String envName;
    public String url;
    public static final Dotenv dotenv = loadDotenv();

    private static Dotenv loadDotenv() {
        Dotenv base = Dotenv.configure().ignoreIfMissing().load();
        envName = "prod";
        String filename = envName.equals("prod") ? ".env.prod" : ".env.preprod";

        try {
            if (Files.exists(Paths.get(filename))) {
                return Dotenv.configure().ignoreIfMissing().filename(filename).load();
            }
        } catch (Exception ignored) {}

        return base;
    }

    public void initializeDriver() {

        url = dotenv.get("URL");

        if (url == null || url.isEmpty()) {
          url = dotenv.get("URL");
        }

        if (url == null || url.isEmpty()) {
            throw new RuntimeException("❌ URL is missing. Define 'URL' in the selected env file or legacy 'PREPROD_URL/PROD_URL'. ENVIRONMENT = " + envName);
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
