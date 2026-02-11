package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.WaitUtils;

public class LoginPage {

    private final WebDriver driver;

    // Locators
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By pageTitle = By.cssSelector(".login_logo");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        // Wait until login page is ready
        WaitUtils.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
    }

    // ---------- Actions ----------
    public void enterUsername(String username) {
        driver.findElement(usernameInput).clear();
        driver.findElement(usernameInput).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // ---------- Assertions / State ----------
    public boolean isErrorDisplayed() {
        return !driver.findElements(errorMessage).isEmpty();
    }

    public String getErrorMessageText() {
        return WaitUtils.wait(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(errorMessage))
                .getText()
                .trim();
    }
}
