package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.WaitUtils;

public class LoginPage {

    private final WebDriver driver;

    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String user, String pass) {
        WaitUtils.wait(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(username))
                .sendKeys(user);

        driver.findElement(password).sendKeys(pass);
        driver.findElement(loginButton).click();
    }

    public String getErrorMessage() {
        return WaitUtils.wait(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(errorMessage))
                .getText();
    }
}
