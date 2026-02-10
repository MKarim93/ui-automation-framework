package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import utils.WaitUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ProductsPage {

    private final WebDriver driver;

    public ProductsPage(WebDriver driver){
        this.driver = driver;
        WaitUtils.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
        WaitUtils.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(inventoryItems));
    }

    // ---------- Page-level locators ----------
    private final By pageTitle = By.cssSelector(".title");
    private final By inventoryItems = By.cssSelector(".inventory_item");
    private final By shoppingCartLink = By.className("shopping_cart_link");
    private final By shoppingCartBadge = By.cssSelector(".shopping_cart_badge");
    private final By productSortDropdown = By.className("product_sort_container");

    // ---------- Item-level locators (inside each card) ----------
    private final By itemName = By.cssSelector(".inventory_item_name");
    private final By itemPrice = By.cssSelector(".inventory_item_price");
    private final By addRemoveButton = By.cssSelector("button.btn_inventory");

    // ---------- Menu locators ----------
    private final By burgerButton = By.id("react-burger-menu-btn");
    private final By closeButton = By.id("react-burger-cross-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By resetAppStateLink = By.id("reset_sidebar_link");

    // ---------- Page assertions / state ----------
    public boolean isOnProductPage(){
        return driver.getCurrentUrl().contains("inventory")
                && driver.findElement(pageTitle).getText().trim().equalsIgnoreCase("Products");
    }

    public int getInventoryCount() {
        return driver.findElements(inventoryItems).size();
    }

    public List<WebElement> getAllItemCards() {
        return driver.findElements(inventoryItems);
    }

    public List<String> getAllProductNames() {
        return getAllItemCards().stream()
                .map(card -> card.findElement(itemName).getText().trim())
                .collect(Collectors.toList());
    }

    public List<Double> getAllProductPrices() {
        return getAllItemCards().stream()
                .map(card -> card.findElement(itemPrice).getText().trim())
                .map(this::parsePrice)
                .collect(Collectors.toList());
    }

    // ---------- Core helper: find a product card by name ----------
    private WebElement findCardByProductName(String productName) {
        for (WebElement card : getAllItemCards()) {
            String nameText = card.findElement(itemName).getText().trim();
            if (nameText.equalsIgnoreCase(productName.trim())) {
                return card;
            }
        }
        throw new NoSuchElementException("Product not found: " + productName);
    }

    // ---------- Actions ----------
    public void openProductByName(String productName) {
        WebElement card = findCardByProductName(productName);
        card.findElement(itemName).click();
    }

    public void addToCartByName(String productName) {
        WebElement card = findCardByProductName(productName);
        card.findElement(addRemoveButton).click();
    }

    public void removeFromCartByName(String productName) {
        WebElement card = findCardByProductName(productName);
        card.findElement(addRemoveButton).click();
    }

    public String getAddRemoveButtonText(String productName) {
        WebElement card = findCardByProductName(productName);
        return card.findElement(addRemoveButton).getText().trim();
    }

    public boolean isItemInCartState(String productName) {
        return getAddRemoveButtonText(productName).equalsIgnoreCase("Remove");
    }

    public void addFirstNItems(int n) {
        List<WebElement> cards = getAllItemCards();
        int limit = Math.min(n, cards.size());
        for (int i = 0; i < limit; i++) {
            cards.get(i).findElement(addRemoveButton).click();
        }
    }

    // ---------- Cart ----------
    public void goToCart() {
        driver.findElement(shoppingCartLink).click();
    }

    /** Returns 0 if the badge is not visible/present. */
    public int getCartBadgeCount() {
        List<WebElement> badge = driver.findElements(shoppingCartBadge);
        if (badge.isEmpty()) return 0;
        String txt = badge.get(0).getText().trim();
        return txt.isEmpty() ? 0 : Integer.parseInt(txt);
    }

    // ---------- Sorting ----------
    public void sortByVisibleText(String optionText) {
        Select select = new Select(driver.findElement(productSortDropdown));
        select.selectByVisibleText(optionText);
        // optional small wait: inventory refreshed
        WaitUtils.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(inventoryItems));
    }

    public String getSelectedSortOption() {
        Select select = new Select(driver.findElement(productSortDropdown));
        return select.getFirstSelectedOption().getText().trim();
    }

    // ---------- Menu ----------
    public void openMenu() {
        driver.findElement(burgerButton).click();
        WaitUtils.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(closeButton));
    }

    public void closeMenu() {
        driver.findElement(closeButton).click();
        WaitUtils.wait(driver).until(ExpectedConditions.invisibilityOfElementLocated(closeButton));
    }

    public void resetAppState() {
        openMenu();
        driver.findElement(resetAppStateLink).click();
        // closeMenu(); // optional: keep UI clean
    }

    public void logout() {
        openMenu();
        driver.findElement(logoutLink).click();
    }

    // ---------- Utils ----------
    private double parsePrice(String priceText) {
        // "$29.99" -> 29.99
        return Double.parseDouble(priceText.replace("$", "").trim());
    }

}
