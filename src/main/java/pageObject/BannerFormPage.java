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

public class BannerFormPage {

    private static final Logger logger = LoggerFactory.getLogger(BannerFormPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators using @FindBy
    @FindBy(css = "#bfc-form-container input[placeholder='Enter your name']")
    WebElement nameInput;

    @FindBy(css = "#bfc-form-container input[placeholder='Enter your mobile number']")
    WebElement mobileInput;

    @FindBy(css = "div.bfcForm_dropdownBox__2Pagz p.bfcForm_unselectedValue__p2RLr")
    WebElement propertyLocationDropdown;

    @FindBy(id = "city_dropdown_0")
    WebElement cityOption;

    @FindBy(xpath = "//button[@id='bfc']") WebElement bookSessionButton;

    @FindBy(css = "input[placeholder='Enter OTP']")
    WebElement otpInput;

    @FindBy(xpath = "//button[contains(text(), 'SUBMIT')]")
    WebElement submitButton;

    // Constructor
    public BannerFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("BannerFormPage initialized");
    }

    // Page Actions
    public void enterName(String name) {
        try {
            logger.info("Entering name: {}", name);
            wait.until(ExpectedConditions.elementToBeClickable(nameInput));
            nameInput.click();
            nameInput.clear();
            nameInput.sendKeys(name);
            logger.info("Name entered successfully");
        } catch (Exception e) {
            logger.error("Failed to enter name: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void enterMobileNumber(String mobile) {
        try {
            logger.info("Entering mobile number: {}", mobile);
            wait.until(ExpectedConditions.elementToBeClickable(mobileInput));
            mobileInput.click();
            mobileInput.clear();
            mobileInput.sendKeys(mobile);
            logger.info("Mobile number entered successfully");
        } catch (Exception e) {
            logger.error("Failed to enter mobile number: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void selectPropertyLocation() {
        try {
            logger.info("Selecting property location");
            wait.until(ExpectedConditions.elementToBeClickable(propertyLocationDropdown)).click();
            logger.info("Property location dropdown clicked");
            wait.until(ExpectedConditions.elementToBeClickable(cityOption)).click();
            logger.info("City option selected successfully");
        } catch (Exception e) {
            logger.error("Failed to select property location: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void clickBookSession() {
        try {
            logger.info("Clicking Book 3D Design Session button");
            wait.until(ExpectedConditions.elementToBeClickable(bookSessionButton)).click();
            logger.info("Book session button clicked successfully");
        } catch (Exception e) {
            logger.error("Failed to click book session button: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void enterOTP(String otp) {
        try {
            logger.info("Entering OTP: {}", otp);
            wait.until(ExpectedConditions.elementToBeClickable(otpInput));
            otpInput.click();
            otpInput.clear();
            otpInput.sendKeys(otp);
            logger.info("OTP entered successfully");
        } catch (Exception e) {
            logger.error("Failed to enter OTP: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void clickSubmit() {
        try {
            logger.info("Clicking Submit button");
            wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
            logger.info("Submit button clicked successfully");
        } catch (Exception e) {
            logger.error("Failed to click submit button: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Complete flow method
    public void fillBannerForm(String name, String mobile, String otp) {
        logger.info("Starting banner form fill process");
        enterName(name);
        enterMobileNumber(mobile);
        selectPropertyLocation();
        clickBookSession();
        enterOTP(otp);
        clickSubmit();
        logger.info("Banner form fill process completed");
    }
}
