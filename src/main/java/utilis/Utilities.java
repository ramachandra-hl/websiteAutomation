package utilis;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class Utilities {

    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);
    private static final Dotenv dotenv = loadDotenv();

    private static Dotenv loadDotenv() {
        Dotenv base = Dotenv.configure().ignoreIfMissing().load();
        String envName = base.get("ENVIRONMENT", "preProd").toLowerCase();
        String filename = envName.equals("prod") ? ".env.prod" : ".env.preprod";

        try {
            if (Files.exists(Paths.get(filename))) {
                return Dotenv.configure().ignoreIfMissing().filename(filename).load();
            }
        } catch (Exception ignored) {}

        return base;
    }

    /**
     * Adds properties from a file to the test data map
     */
    public static Map<String, Object> addToTestDataFromProperties(String filePath, Map<String, Object> testData) {
        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            properties.load(inputStream);
            // Add all properties to the map
            for (String key : properties.stringPropertyNames()) {
                testData.put(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read properties file: " + filePath, e);
        }
        return testData;
    }

    /**
     * Generates a dynamic 10-digit phone number with first two digits from .env
     * Format: [PHONE_PREFIX from .env][8 random digits]
     * 
     * @return String - 10-digit phone number
     */
    public static String generateDynamicPhoneNumber() {
        try {
            // Get phone prefix from .env (default to "60" if not found)
            String phonePrefix = dotenv.get("PHONE_PREFIX", "60");

            // Validate prefix is 2 digits
            if (phonePrefix.length() != 2) {
                logger.warn("PHONE_PREFIX should be 2 digits, using default '60'");
                phonePrefix = "60";
            }

            // Generate 8 random digits
            Random random = new Random();
            StringBuilder phoneNumber = new StringBuilder(phonePrefix);

            for (int i = 0; i < 8; i++) {
                phoneNumber.append(random.nextInt(10));
            }

            String generatedNumber = phoneNumber.toString();
            logger.info("Generated dynamic phone number: {}", generatedNumber);

            return generatedNumber;

        } catch (Exception e) {
            logger.error("Error generating phone number: {}", e.getMessage(), e);
            // Fallback to a default number
            return "6000000000";
        }
    }

    /**
     * Generates a dynamic phone number with custom prefix
     * 
     * @param prefix - First two digits of the phone number
     * @return String - 10-digit phone number
     */
    public static String generateDynamicPhoneNumber(String prefix) {
        try {
            // Validate prefix is 2 digits
            if (prefix == null || prefix.length() != 2) {
                logger.warn("Prefix should be 2 digits, using .env value instead");
                return generateDynamicPhoneNumber();
            }

            // Generate 8 random digits
            Random random = new Random();
            StringBuilder phoneNumber = new StringBuilder(prefix);

            for (int i = 0; i < 8; i++) {
                phoneNumber.append(random.nextInt(10));
            }

            String generatedNumber = phoneNumber.toString();
            logger.info("Generated dynamic phone number with custom prefix: {}", generatedNumber);

            return generatedNumber;

        } catch (Exception e) {
            logger.error("Error generating phone number with custom prefix: {}", e.getMessage(), e);
            return generateDynamicPhoneNumber();
        }
    }

    /**
     * Generates a unique phone number based on timestamp
     * Format: [PHONE_PREFIX][last 8 digits of current timestamp]
     * 
     * @return String - 10-digit unique phone number
     */
    public static String generateUniquePhoneNumber() {
        try {
            String phonePrefix = dotenv.get("PHONE_PREFIX", "60");

            // Get last 8 digits of current timestamp
            long timestamp = System.currentTimeMillis();
            String timestampStr = String.valueOf(timestamp);
            String last8Digits = timestampStr.substring(timestampStr.length() - 8);

            String uniqueNumber = phonePrefix + last8Digits;
            logger.info("Generated unique phone number: {}", uniqueNumber);

            return uniqueNumber;

        } catch (Exception e) {
            logger.error("Error generating unique phone number: {}", e.getMessage(), e);
            return generateDynamicPhoneNumber();
        }
    }

    /**
     * Generates tomorrow's date in the format "d-M-yyyy" (e.g., "30-11-2025")
     * This format is required by the MeetingSchedulePage date picker
     * 
     * @return String - Tomorrow's date in "d-M-yyyy" format
     */
    public static String getTomorrowDate() {
        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
            String tomorrowDate = tomorrow.format(formatter);
            logger.info("Generated tomorrow's date: {}", tomorrowDate);
            return tomorrowDate;
        } catch (Exception e) {
            logger.error("Error generating tomorrow's date: {}", e.getMessage(), e);
            // Fallback to a future date
            return "1-1-2026";
        }
    }
}
