package tests;

import configurator.BaseClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.BannerFormPage;
import pageObject.MeetingSchedulePage;
import pageObject.PlanningOptionsPage;
import pageObject.PropertyDetailsPage;

public class BannerSignInTest extends BaseClass {

    private static final Logger logger = LoggerFactory.getLogger(BannerSignInTest.class);

    private BannerFormPage bannerFormPage;
    private PropertyDetailsPage propertyDetailsPage;
    private PlanningOptionsPage planningOptionsPage;
    private MeetingSchedulePage meetingSchedulePage;

    @BeforeMethod
    public void setup() {
        logger.info("========== Starting Banner Sign In Test ==========");
        logger.info("Initializing WebDriver and navigating to application");

        try {
            initializeDriver();
            logger.info("WebDriver initialized successfully");
            logger.info("Current URL: {}", driver.getCurrentUrl());

            // Verify page title
            String pageTitle = driver.getTitle();
            logger.info("Page title: {}", pageTitle);

            Assert.assertTrue(pageTitle.contains("HomeLane"),
                    "Page title should contain 'HomeLane' but found: " + pageTitle);
            logger.info("Page title verification passed");

            // Initialize page objects
            logger.info("Initializing page objects");
            bannerFormPage = new BannerFormPage(driver);
            propertyDetailsPage = new PropertyDetailsPage(driver);
            planningOptionsPage = new PlanningOptionsPage(driver);
            meetingSchedulePage = new MeetingSchedulePage(driver);
            logger.info("All page objects initialized successfully");

        } catch (Exception e) {
            logger.error("Error during test setup: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Test
    public void bannerPageSignInTest() throws InterruptedException {

        logger.info("---------- Test Execution Started ----------");

        // Generate dynamic phone number
        String phoneNumber = utilis.Utilities.generateDynamicPhoneNumber();
        logger.info("Using phone number: {}", phoneNumber);

        String customerName = dotenv.get("CUSTOMER_NAME");
        String otp = dotenv.get("OTP");
        if (env.toLowerCase().equals("preprod")) {
            otp = dotenv.get("PREPROD_OTP");
        } else if (env.toLowerCase().equals("prod")) {
            otp = dotenv.get("PROD_OTP");
        }

        // Step 1: Fill banner form with user details
        logger.info("Step 1: Filling banner form with user details");
        bannerFormPage.fillBannerForm(customerName, phoneNumber, otp);
        logger.info("Step 1: Banner form filled successfully");

        // Step 2: Fill property details
        logger.info("Step 2: Filling property details");
        propertyDetailsPage
                .fillPropertyDetails("TestYantra Software Solutions, Somajiguda, Hyderabad, Telangana, India");
        logger.info("Step 2: Property details filled successfully");

        // Step 3: Fill planning options
        logger.info("Step 3: Filling planning options");
        planningOptionsPage.fillPlanningOptions();
        logger.info("Step 3: Planning options filled successfully");

        // Step 4: Schedule meeting with tomorrow's date
        logger.info("Step 4: Scheduling meeting");
        String tomorrowDate = utilis.Utilities.getTomorrowDate();
        logger.info("Using tomorrow's date: {}", tomorrowDate);
        meetingSchedulePage.scheduleMeeting(tomorrowDate);
        logger.info("Step 4: Meeting scheduled successfully");

        logger.info("✅ Banner Sign In Test completed successfully!");
        logger.info("---------- Test Execution Completed ----------");

    }

    @AfterMethod
    public void tearDown() {
        logger.info("Cleaning up and closing browser");
        try {
            closeDriver();
            logger.info("Browser closed successfully");
        } catch (Exception e) {
            logger.error("Error during teardown: {}", e.getMessage(), e);
        }
        logger.info("========== Test Execution Ended ==========\n");
    }
}
