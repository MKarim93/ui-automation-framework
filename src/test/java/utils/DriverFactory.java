package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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

        return new ChromeDriver(options);
    }
}
