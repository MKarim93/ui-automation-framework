package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    public static WebDriverWait wait(WebDriver driver) {
        int timeout = ConfigReader.getInt("timeout.seconds", 10);
        return new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }
}
