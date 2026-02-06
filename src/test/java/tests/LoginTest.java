package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginShouldSucceed() {
        new LoginPage(driver).login("standard_user", "secret_sauce");

        Assert.assertTrue(
                driver.getCurrentUrl().contains("inventory"),
                "User should land on inventory page after login"
        );
    }

    @Test
    public void invalidLoginShouldShowError() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("bad_user", "bad_pass");

        Assert.assertTrue(
                loginPage.getErrorMessage().toLowerCase().contains("do not match"),
                "Proper error message should be shown"
        );
    }
}
