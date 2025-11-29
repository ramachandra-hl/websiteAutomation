package pageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PropertyDetailsPage {

    private static final Logger logger = LoggerFactory.getLogger(PropertyDetailsPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators using @FindBy
    @FindBy(xpath = "//div[contains(@class,'BFC_flowSteps_listOf_house__3_f_E')]//p[normalize-space()='Apartment']/parent::div")
    WebElement apartmentOption;

    @FindBy(xpath = "//div[contains(@class,'BFC_flowSteps_listOf_house__3_f_E')]//p[normalize-space()='Villa']/parent::div")
    WebElement villaOption;

    @FindBy(xpath = "//div[contains(@class,'BFC_flowSteps_listOf_house__3_f_E')]//p[normalize-space()='Independent Home']/parent::div")
    WebElement independentHomeOption;

    @FindBy(xpath = "//div[contains(@class,'BFC_flowSteps_selectFloorType_option__2UXKX')]//button[contains(text(), '2BHK')]")
    WebElement bhk2Button;

    @FindBy(className = "BFC_flowSteps_selectProperty_area__1L1yy")
    WebElement propertyAreaSection;

    @FindBy(css = "input[placeholder='Search your property']")
    WebElement propertySearchInput;

    @FindBy(xpath = "//button[contains(text(), 'Next')]")
    WebElement nextButton;

    // Constructor
    public PropertyDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("PropertyDetailsPage initialized");
    }

    // Page Actions
    public void selectApartment() {
        try {
            logger.info("Selecting Apartment option");
            wait.until(ExpectedConditions.elementToBeClickable(apartmentOption)).click();
            logger.info("Apartment option selected successfully");
        } catch (Exception e) {
            logger.error("Failed to select apartment: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void selectVilla() {
        try {
            logger.info("Selecting Villa option");
            wait.until(ExpectedConditions.elementToBeClickable(villaOption)).click();
            logger.info("Villa option selected successfully");
        } catch (Exception e) {
            logger.error("Failed to select villa: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void selectIndependentHome() {
        try {
            logger.info("Selecting Independent Home option");
            wait.until(ExpectedConditions.elementToBeClickable(independentHomeOption)).click();
            logger.info("Independent Home option selected successfully");
        } catch (Exception e) {
            logger.error("Failed to select independent home: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void selectPropertyType(String propertyType) {
        logger.info("Selecting property type: {}", propertyType);
        switch (propertyType.toLowerCase()) {
            case "apartment":
                selectApartment();
                break;
            case "villa":
                selectVilla();
                break;
            case "independent home":
                selectIndependentHome();
                break;
            default:
                logger.error("Invalid property type: {}", propertyType);
                throw new IllegalArgumentException("Property type must be 'Apartment', 'Villa', or 'Independent Home'");
        }
    }

    public void select2BHK() {
        try {
            logger.info("Selecting 2BHK option");
            wait.until(ExpectedConditions.elementToBeClickable(bhk2Button)).click();
            logger.info("2BHK option selected successfully");
        } catch (Exception e) {
            logger.error("Failed to select 2BHK: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void searchAndSelectProperty(String propertyName) throws InterruptedException {
        try {
            logger.info("Searching for property: {}", propertyName);
            wait.until(ExpectedConditions.elementToBeClickable(propertyAreaSection)).click();
            logger.info("Property search area clicked");
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(propertySearchInput));
            propertySearchInput.clear();
            propertySearchInput.sendKeys(propertyName);
            logger.info("Property name entered in search box");
            Thread.sleep(2000);
            WebElement propertySearchResult = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            org.openqa.selenium.By
                                    .xpath("//div[@class='pac-item']//span[@class='pac-item-query']")));
            propertySearchResult.click();
            logger.info("Property selected from search results");
        } catch (Exception e) {
            logger.error("Failed to search and select property: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void clickNext() {
        try {
            logger.info("Clicking Next button");
            wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
            logger.info("Next button clicked successfully");

            // Wait for page transition
            logger.info("Waiting for page transition (5 seconds)");
            Thread.sleep(5000);
            logger.info("Page transition wait completed");
        } catch (InterruptedException e) {
            logger.error("Thread interrupted during wait: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Failed to click next button: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Complete flow method
    public void fillPropertyDetails(String propertyName) throws InterruptedException {
        logger.info("Starting property details fill process");
        selectApartment();
        select2BHK();
        searchAndSelectProperty(propertyName);
        clickNext();
        logger.info("Property details fill process completed");
    }
}
