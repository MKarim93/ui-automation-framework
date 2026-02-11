package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    public static WebDriver createDriver() {
        String browser = System.getProperty("browser",
                ConfigReader.get("browser") == null ? "chrome" : ConfigReader.get("browser"));

        boolean headless = Boolean.parseBoolean(
                System.getProperty("headless",
                        String.valueOf(ConfigReader.getBoolean("headless", false)))
        );

        if (!browser.equalsIgnoreCase("chrome")) {
            throw new IllegalArgumentException("Only chrome is supported for now. Got: " + browser);
        }

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");

// Disable Chrome password manager + breach/leak checks
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

// This one helps stop the "found in a data breach" style prompts in many cases
        prefs.put("profile.password_manager_leak_detection", false);

        options.setExperimentalOption("prefs", prefs);

// Optional: start with a fresh profile each run (stronger isolation)
        options.addArguments("--incognito");

        return new ChromeDriver(options);
    }
}
