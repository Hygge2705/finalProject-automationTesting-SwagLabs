package action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.InventoryPageUI;

import java.time.Duration;
import java.util.*;

import static utils.TestContext.selectedProducts;

public class InventoryPage {
    WebDriver driver;
    WebDriverWait wait;

    public InventoryPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public boolean verifyOnInventoryPage() {
        return Objects.equals(driver.getTitle(), "Swag Labs")
                && Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory")
                && driver.findElement(InventoryPageUI.PAGE_TITLE).getText().equals("Products");
    }

    //1. Check UI
    public boolean isLogoDisplayed(){
        return driver.findElement(InventoryPageUI.APP_LOGO).isDisplayed();
    }

    public boolean isMenuActive(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(InventoryPageUI.MENU));
        driver.findElement(InventoryPageUI.MENU).click();

        // Tạo list các item trong menu để chờ và kiểm tra
        List<By> menuOptions = Arrays.asList(
                InventoryPageUI.ALL_ITEMS_LINK,
                InventoryPageUI.ABOUT_LINK,
                InventoryPageUI.LOGOUT_LINK,
                InventoryPageUI.RESET_APP_STATE_LINK);

        // Chờ tất cả các item hiển thị
        for (By option : menuOptions) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(option));
                if (!driver.findElement(option).isDisplayed()) {
                    System.out.println("Item không hiển thị: " + option);
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Không tìm thấy hoặc không hiển thị: " + option);
                return false; // Nếu 1 item không hiển thị thì trả về false
            }
        }
        return true;
    }

    public void clickResetAppState(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(InventoryPageUI.MENU));
        driver.findElement(InventoryPageUI.MENU).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(InventoryPageUI.RESET_APP_STATE_LINK));
        driver.findElement(InventoryPageUI.RESET_APP_STATE_LINK).click();
    }

    public boolean isShoppingCartActive(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(InventoryPageUI.SHOPPING_CART));
        driver.findElement(InventoryPageUI.SHOPPING_CART).click();
        return Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart.html");
    }

    public boolean isFooterDisplayed(){
        return driver.findElement(InventoryPageUI.LOGO_FACEBOOK).isDisplayed()
                && driver.findElement(InventoryPageUI.LOGO_TWITTER).isDisplayed()
                && driver.findElement(InventoryPageUI.LOGO_LINKEDIN).isDisplayed()
                && driver.findElement(InventoryPageUI.FOOTER_TEXT).isDisplayed();
    }

    public int getInventoryCount(){
        return driver.findElements(InventoryPageUI.INVENTORY_ITEM).size();
    }

    public boolean isSortByActive(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(InventoryPageUI.SORt_BY));
        driver.findElement(InventoryPageUI.SORt_BY).click();
        return driver.findElement(InventoryPageUI.SORT_BY_NAME_AZ).isDisplayed()
                && driver.findElement(InventoryPageUI.SORT_BY_NAME_ZA).isDisplayed()
                && driver.findElement(InventoryPageUI.SORT_BY_PRICE_LOW).isDisplayed()
                && driver.findElement(InventoryPageUI.SORT_BY_PRICE_HIGH).isDisplayed();
    }

    //2. Check Sort By
    private List<String> getProductNames() {
        List<WebElement> elements = driver.findElements(InventoryPageUI.PRODUCT_NAME);
        List<String> names = new ArrayList<>();
        for (WebElement el : elements) {
            names.add(el.getText().trim());
        }
        return names;
    }

    public boolean isSortedByName(Comparator<String> comparator) {
        List<String> actual = getProductNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(comparator);
        return actual.equals(expected);
    }

    public void clickToSortByNameAZ(){
        driver.findElement(InventoryPageUI.SORt_BY).click();
        driver.findElement(InventoryPageUI.SORT_BY_NAME_AZ).click();
    }

    public void clickToSortByNameZA(){
        driver.findElement(InventoryPageUI.SORt_BY).click();
        driver.findElement(InventoryPageUI.SORT_BY_NAME_ZA).click();

    }

    public boolean isSortedByNameAZ() {
        return isSortedByName(String::compareToIgnoreCase);
    }

    public boolean isSortedByNameZA() {
        return isSortedByName((s1, s2) -> s2.compareToIgnoreCase(s1));
    }


    private List<Double> getProductPrices() {
        List<WebElement> elements = driver.findElements(InventoryPageUI.PRODUCT_PRICE);
        List<Double> prices = new ArrayList<>();
        for (WebElement el : elements) {
            String priceText = el.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }

    public boolean isSortedByPrice(Comparator<Double> comparator) {
        List<Double> actual = getProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        expected.sort(comparator);
        return actual.equals(expected);
    }

    public void clickToSortByPriceLow(){
        driver.findElement(InventoryPageUI.SORT_BY_PRICE_LOW).click();
    }
    public void clickToSortByPriceHigh(){
        driver.findElement(InventoryPageUI.SORT_BY_PRICE_HIGH).click();
    }

    public boolean isSortedByPriceLowToHigh() {
        return isSortedByPrice(Double::compareTo);
    }

    public boolean isSortedByPriceHighToLow() {
        return isSortedByPrice(Comparator.reverseOrder());
    }

    // 3. Check Add to cart /Remove
    public boolean isButtonAddToCart(String productName) {
        for (WebElement product : driver.findElements(InventoryPageUI.INVENTORY_ITEM)) {
            String name = product.findElement(InventoryPageUI.PRODUCT_NAME).getText().trim();
            if (name.equalsIgnoreCase(productName)) {
                if (!product.findElements(InventoryPageUI.ADD_TO_CART_BUTTON).isEmpty()){
                    wait.until(ExpectedConditions.visibilityOf(product.findElement(InventoryPageUI.ADD_TO_CART_BUTTON)));
                    return product.findElement(InventoryPageUI.ADD_TO_CART_BUTTON).isDisplayed();
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    public void clickAddToCart(String productName) {
        for (WebElement product : driver.findElements(InventoryPageUI.INVENTORY_ITEM)) {
            String name = product.findElement(InventoryPageUI.PRODUCT_NAME).getText().trim();
            if (name.equalsIgnoreCase(productName)) {
                if (!product.findElements(InventoryPageUI.ADD_TO_CART_BUTTON).isEmpty()){
                    String price = product.findElement(InventoryPageUI.PRODUCT_PRICE).getText().trim();
                    product.findElement(InventoryPageUI.ADD_TO_CART_BUTTON).click();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(InventoryPageUI.REMOVE_FROM_CART_BUTTON));
                    selectedProducts.put(productName, price);
                    break;
                }
            }
        }
    }

    public void clickRemove(String productName) {
        for (WebElement product : driver.findElements(InventoryPageUI.INVENTORY_ITEM)) {
            String name = product.findElement(InventoryPageUI.PRODUCT_NAME).getText().trim();
            if (name.equalsIgnoreCase(productName)) {
                if (!product.findElements(InventoryPageUI.REMOVE_FROM_CART_BUTTON).isEmpty()){
                    product.findElement(InventoryPageUI.REMOVE_FROM_CART_BUTTON).click();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(InventoryPageUI.ADD_TO_CART_BUTTON));
                    selectedProducts.remove(productName);
                    break;
                }
            }
        }
    }

    public void clickProductName(String productName) {
        for (WebElement product : driver.findElements(InventoryPageUI.INVENTORY_ITEM)) {
            String name = product.findElement(InventoryPageUI.PRODUCT_NAME).getText().trim();
            if (name.equalsIgnoreCase(productName)) {
                product.findElement(InventoryPageUI.PRODUCT_NAME).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(InventoryPageUI.BACK_BUTTON));
                break;
            }
        }
    }
    public void clickProductImage(String productName) {
        for (WebElement product : driver.findElements(InventoryPageUI.INVENTORY_ITEM)) {
            String name = product.findElement(InventoryPageUI.PRODUCT_NAME).getText().trim();
            if (name.equalsIgnoreCase(productName)) {
                product.findElement(InventoryPageUI.PRODUCT_IMAGE).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(InventoryPageUI.BACK_BUTTON));
                break;
            }
        }
    }

    public void clickBackToProductsButton(){
        driver.findElement(InventoryPageUI.BACK_BUTTON).click();
    }

    public int checkNumOfCart(){
        if(!driver.findElements(InventoryPageUI.NUM_OF_CART).isEmpty()) {
            return Integer.parseInt(driver.findElement(InventoryPageUI.NUM_OF_CART).getText().trim());
        } else {
            return 0;
        }
    }

    public void clickShoppingCart(){
        driver.findElement(InventoryPageUI.SHOPPING_CART).click();
    }

}
