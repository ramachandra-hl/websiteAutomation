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

public class PlanningOptionsPage {

    private static final Logger logger = LoggerFactory.getLogger(PlanningOptionsPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators using @FindBy
    @FindBy(xpath = "//div[@class='BFC_flowSteps_planingOptions_section__3nolj']//button[contains(text(), 'Rent Out')]")
    WebElement rentOutButton;

    @FindBy(xpath = "//div[@class='BFC_flowSteps_interiorsType_section__2Odju']//button[contains(text(), 'End-to-end Interiors')]")
    WebElement endToEndInteriorsButton;

    @FindBy(xpath = "//div[@class='BFC_flowSteps_selectOption_section__32fyK']//div[@class=' ']//p[contains(text(), 'Select Budget')]")
    WebElement selectBudgetText;

    @FindBy(id = "budgetDropValueStep_2")
    WebElement budgetDropdownOption;

    @FindBy(xpath = "//div[@class='BFC_flowSteps_selectOption_section__32fyK']//div[@id = 'possessionMonth_section']")
    WebElement selectPossessionText;

    @FindBy(id = "possession_month_dropdown_1")
    WebElement possessionDropdownOption;

    @FindBy(xpath = "//div[@id='nextAndBack_optionStep2']//button[contains(text(), 'Next')]")
    WebElement nextButton;

    // Constructor
    public PlanningOptionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("PlanningOptionsPage initialized");
    }

    // Page Actions
    public void selectRentOut() {
        try {
            logger.info("Selecting 'Rent Out' option");
            wait.until(ExpectedConditions.visibilityOf(rentOutButton));
            wait.until(ExpectedConditions.elementToBeClickable(rentOutButton)).click();
            logger.info("'Rent Out' option selected successfully");
        } catch (Exception e) {
            logger.error("Failed to select 'Rent Out': {}", e.getMessage(), e);
            throw e;
        }
    }

    public void selectEndToEndInteriors() {
        try {
            logger.info("Selecting 'End-to-end Interiors' option");
            wait.until(ExpectedConditions.visibilityOf(endToEndInteriorsButton));
            wait.until(ExpectedConditions.elementToBeClickable(endToEndInteriorsButton)).click();
            logger.info("'End-to-end Interiors' option selected successfully");
        } catch (Exception e) {
            logger.error("Failed to select 'End-to-end Interiors': {}", e.getMessage(), e);
            throw e;
        }
    }

    public void selectBudget() {
        try {
            logger.info("Selecting budget option");
            wait.until(ExpectedConditions.elementToBeClickable(selectBudgetText)).click();
            logger.info("Budget dropdown clicked");

            wait.until(ExpectedConditions.visibilityOf(budgetDropdownOption));
            wait.until(ExpectedConditions.elementToBeClickable(budgetDropdownOption)).click();
            logger.info("Budget option selected successfully");
        } catch (Exception e) {
            logger.error("Failed to select budget: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void selectPossession() {
        try {
            logger.info("Selecting possession option");
            wait.until(ExpectedConditions.elementToBeClickable(selectPossessionText)).click();
            logger.info("Possession dropdown clicked");

            wait.until(ExpectedConditions.visibilityOf(possessionDropdownOption));
            wait.until(ExpectedConditions.elementToBeClickable(possessionDropdownOption)).click();
            logger.info("Possession option selected successfully");
        } catch (Exception e) {
            logger.error("Failed to select possession: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void clickNext() {
        try {
            logger.info("Clicking Next button");
            wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
            logger.info("Next button clicked successfully");
        } catch (Exception e) {
            logger.error("Failed to click next button: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Complete flow method
    public void fillPlanningOptions() {
        logger.info("Starting planning options fill process");
        selectRentOut();
        selectEndToEndInteriors();
        selectBudget();
        selectPossession();
        clickNext();
        logger.info("Planning options fill process completed");
    }
}
