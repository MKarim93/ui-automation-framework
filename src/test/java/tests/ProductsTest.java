package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProductsTest extends BaseTest {

    @BeforeMethod
    public void login(){
        // BaseTest opens base.url (SauceDemo login page)
        new LoginPage(driver).login("standard_user", "secret_sauce");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        // Keep test independent: clear cart + state after each test
        // If a test fails mid-way, this still attempts cleanup.
        try {
            ProductsPage productsPage = new ProductsPage(driver);
            productsPage.resetAppState();
            ;
        } catch (Exception ignored) {
            // ignore cleanup errors (e.g., not a products page)
        }
    }

    @Test
    public void inventoryPageShouldLoadAndShowProducts(){
        ProductsPage productsPage = new ProductsPage(driver);

        Assert.assertTrue(productsPage.isOnProductPage(), "User should be on Products (inventory) page");
        Assert.assertTrue(productsPage.getInventoryCount() > 0, "Inventory should show at least one product");
        Assert.assertFalse(productsPage.getAllProductNames().isEmpty(), "Product names should be readable");
    }

    @Test
    public void addToCartShouldUpdateBadgeAndButtonState(){
        ProductsPage productsPage = new ProductsPage(driver);

        String itemName = "Sauce Labs Backpack";

        Assert.assertEquals(productsPage.getCartBadgeCount(), 0, "Cart badge should start at 0");
        Assert.assertFalse(productsPage.isItemInCartState(itemName), "Item should not be in cart initially");

        productsPage.addToCartByName(itemName);

        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart badge should be 1 after adding one item");
        Assert.assertTrue(productsPage.isItemInCartState(itemName), "Item should be in cart state (Remove)");
        Assert.assertEquals(productsPage.getAddRemoveButtonText(itemName), "Remove", "Button text should be Remove");
    }

    @Test
    public void removeFromCartShouldUpdateBadgeAndButtonState(){
        ProductsPage productsPage = new ProductsPage(driver);

        String itemName = "Sauce Labs Backpack";

        productsPage.addToCartByName(itemName);
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Badge should be 1 after adding");

        productsPage.removeFromCartByName(itemName);

        Assert.assertEquals(productsPage.getCartBadgeCount(), 0, "Badge should return to 0 after removing");
        Assert.assertFalse(productsPage.isItemInCartState(itemName), "Item should not be in cart state after removing");
        Assert.assertEquals(productsPage.getAddRemoveButtonText(itemName), "Add to cart", "Button should revert");
    }

    @Test
    public void addFirstNItemsShouldMatchBadgeCount(){
        ProductsPage productsPage = new ProductsPage(driver);

        int n = 3;
        productsPage.addFirstNItems(n);

        Assert.assertEquals(productsPage.getCartBadgeCount(), n, "Badge count should match number of added items");
    }

    @Test
    public void sortByPriceLowToHighShouldBeSortedNumerically(){
        ProductsPage productsPage = new ProductsPage(driver);

        productsPage.sortByVisibleText("Price (low to high)");
        Assert.assertEquals(productsPage.getSelectedSortOption(), "Price (low to high)", "Sort selection should match");

        List<Double> actualPrices = productsPage.getAllProductPrices();
        List<Double> expectedSorted = new ArrayList<>(actualPrices);
        Collections.sort(expectedSorted);

        Assert.assertEquals(actualPrices, expectedSorted, "Prices should be sorted low to high");
    }
}
