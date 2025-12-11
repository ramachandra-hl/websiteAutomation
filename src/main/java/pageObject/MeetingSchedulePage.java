package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static configurator.BaseClass.envName;

public class MeetingSchedulePage {

    private static final Logger logger = LoggerFactory.getLogger(MeetingSchedulePage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators using @FindBy
    @FindBy(xpath = "//div[@class='BFC_flowSteps_selectOption_section__32fyK' and contains(., 'Select Experience Centre')]")
    WebElement experienceCentreDropdownPreProd;
    //HomeLane Adityapur

    @FindBy(xpath = "//div[@class='BFC_flowSteps_selectOption_section__32fyK' and contains(., 'Adityapur')]")
    WebElement experienceCentreDropdownProd;

//    @FindBy(xpath = "//ul[@class='BFC_flowSteps_dropdown_lists__1W_Cc']//li[@class='BFC_flowSteps_dropdown_options__3h23k ' and contains(text(), 'Test Showroom Bangalore - Electronic City')]")
//    WebElement experienceCentreOption;

    @FindBy(xpath = "//div[@class='BFC_flowSteps_selectMeeting_dateAndTime__3-mtW ']//p[@class = 'BFC_flowSteps_selectType_text__1GfhV' and contains(text(), 'Select date')] ")
    WebElement selectDateDropdown;

    @FindBy(xpath = "//div[@class='BFC_flowSteps_internalTabs_cards__1KbHv step3']//div[@id='time_dropdown']")
    WebElement timeSlotDropdown;

    @FindBy(xpath = "//ul[@class='BFC_flowSteps_dropdown_lists__1W_Cc BFC_flowSteps_timeSlot_dropdown__3q2Cm']//li[@class = 'BFC_flowSteps_dropdown_options__3h23k'][2]")
    WebElement timeSlotOption;

    @FindBy(xpath = "//div[@id='nextAndBack_optionStep3']//button[contains(text(), 'BOOK FREE DESIGN SESSION')]")
    WebElement bookSessionButton;

    // Constructor
    public MeetingSchedulePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("MeetingSchedulePage initialized");
    }

    // Helper method to wait for loaders to disappear
    private void waitForLoadersToDisappear() {
        try {
            // Wait for the time loader to disappear if it exists
            By loaderLocator = By.xpath("//div[contains(@class, 'BFC_flowSteps_timeLoader')]");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));
            logger.info("Loader disappeared");
        } catch (Exception e) {
            // Loader might not be present, which is fine
            logger.debug("No loader found or already disappeared");
        }
    }

    // Page Actions
    public void selectExperienceCentre(String showroomName) {
        try {
            logger.info("Selecting experience centre");
            if (envName.equalsIgnoreCase("prod")){
                wait.until(ExpectedConditions.elementToBeClickable(experienceCentreDropdownProd)).click();
            }else{
                wait.until(ExpectedConditions.elementToBeClickable(experienceCentreDropdownPreProd)).click();
            }
            logger.info("Experience centre dropdown clicked");
           String experienceCentreOption = "//ul[@class='BFC_flowSteps_dropdown_lists__1W_Cc']//li[@class='BFC_flowSteps_dropdown_options__3h23k ' and contains(text(), '"+showroomName+"')]";
            wait.until(ExpectedConditions.elementToBeClickable( driver.findElement(By.xpath(experienceCentreOption)))).click();
            logger.info("Experience centre option selected successfully");
        } catch (Exception e) {
            logger.error("Failed to select experience centre: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Selects a date from the React datepicker
     * 
     * @param dateString Date in format "d-M-yyyy" (e.g., "1-1-2026" or
     *                   "30-11-2025")
     */
    public void selectDate(String dateString) {
        try {
            logger.info("Selecting date: {}", dateString);

            // Wait for any loaders to disappear before clicking
            waitForLoadersToDisappear();

            wait.until(ExpectedConditions.elementToBeClickable(selectDateDropdown)).click();
            logger.info("Date dropdown clicked");

            // Wait for datepicker calendar to fully load
            Thread.sleep(2000);
            logger.info("Waiting for datepicker calendar to load");

            // Parse the input date string (format: d-M-yyyy)
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d-M-yyyy");
            LocalDate date = LocalDate.parse(dateString, inputFormatter);
            int day = date.getDayOfMonth();

            logger.info("Looking for date: {}", day);

            // Try multiple XPath strategies to find the date element
            WebElement dateButton = null;

            // Strategy 1: Try with aria-label (original approach)
            try {
                String dayOfWeek = date.format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH));
                String month = date.format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH));
                String dayWithSuffix = getDayWithSuffix(day);
                int year = date.getYear();
                String ariaLabel = String.format("Choose %s, %s %s, %d", dayOfWeek, month, dayWithSuffix, year);

                String dateXPath = String.format("//div[@class='react-datepicker__day' and @aria-label='%s']",
                        ariaLabel);
                logger.info("Strategy 1: Looking for aria-label: {}", ariaLabel);
                dateButton = driver.findElement(By.xpath(dateXPath));
                logger.info("Found date using Strategy 1 (aria-label)");
            } catch (Exception e1) {
                logger.warn("Strategy 1 failed: {}", e1.getMessage());

                // Strategy 2: Try clicking on visible date number directly
                try {
                    String simpleDateXPath = String.format(
                            "//div[contains(@class, 'react-datepicker__day') and text()='%d' and not(contains(@class, 'disabled'))]",
                            day);
                    logger.info("Strategy 2: Looking for date with XPath: {}", simpleDateXPath);
                    dateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(simpleDateXPath)));
                    logger.info("Found date using Strategy 2 (simple text match)");
                } catch (Exception e2) {
                    logger.warn("Strategy 2 failed: {}", e2.getMessage());

                    // Strategy 3: Try any clickable element with the date number
                    try {
                        String anyDateXPath = String.format("//*[text()='%d' and not(contains(@class, 'disabled'))]",
                                day);
                        logger.info("Strategy 3: Looking for any element with text: {}", day);
                        dateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(anyDateXPath)));
                        logger.info("Found date using Strategy 3 (any clickable element)");
                    } catch (Exception e3) {
                        logger.error("All strategies failed to find the date");
                        throw new RuntimeException("Could not find date " + day + " in the calendar", e3);
                    }
                }
            }

            if (dateButton != null) {
                dateButton.click();
                logger.info("Date selected successfully: {}", dateString);
            }
        } catch (InterruptedException e) {
            logger.error("Thread interrupted during wait: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Date selection interrupted", e);
        } catch (Exception e) {
            logger.error("Failed to select date: {}", e.getMessage(), e);
            throw e;
        }

        // Wait for time slots to load after date selection
        try {
            Thread.sleep(1500);
            logger.info("Waiting for time slots to load after date selection");
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Helper method to add ordinal suffix to day (1st, 2nd, 3rd, 4th, etc.)
     */
    private String getDayWithSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return day + "th";
        }
        switch (day % 10) {
            case 1:
                return day + "st";
            case 2:
                return day + "nd";
            case 3:
                return day + "rd";
            default:
                return day + "th";
        }
    }

    public void selectTimeSlot() {
        try {
            logger.info("Selecting time slot");

            // Wait for any loaders to disappear before clicking
            waitForLoadersToDisappear();

            wait.until(ExpectedConditions.elementToBeClickable(timeSlotDropdown)).click();
            logger.info("Time slot dropdown clicked");

            wait.until(ExpectedConditions.visibilityOf(timeSlotOption));
            wait.until(ExpectedConditions.elementToBeClickable(timeSlotOption)).click();
            logger.info("Time slot selected successfully");
        } catch (Exception e) {
            logger.error("Failed to select time slot: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void clickBookFreeDesignSession() {
        try {
            logger.info("Clicking 'Book Free Design Session' button");
            wait.until(ExpectedConditions.elementToBeClickable(bookSessionButton)).click();
            logger.info("'Book Free Design Session' button clicked successfully");

            // Wait for final submission
            logger.info("Waiting for final submission (5 seconds)");
            Thread.sleep(5000);
            logger.info("Final submission wait completed");
        } catch (InterruptedException e) {
            logger.error("Thread interrupted during wait: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Failed to click book session button: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Complete flow method
    public void scheduleMeeting(String date, String showroomName) {
        logger.info("Starting meeting scheduling process");
        selectExperienceCentre(showroomName);
        selectDate(date);
        selectTimeSlot();
        clickBookFreeDesignSession();
        logger.info("Meeting scheduling process completed");
    }
}
