package action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.YourCartPageUI;
import utils.Hook;

import java.time.Duration;
import java.util.*;

import static utils.TestContext.selectedProducts;

public class YourCartPage extends Hook {
    WebDriver driver;
    WebDriverWait wait;

    public YourCartPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public boolean verifyOnYourCartPage() {
        return Objects.equals(driver.getTitle(), "Swag Labs")
                && Objects.requireNonNull(driver.getCurrentUrl()).contains("cart")
                && driver.findElement(YourCartPageUI.PAGE_TITLE).getText().equals("Your Cart");
    }

    //1. Check UI
    public boolean isAPPLogoDisplayed(){
        return driver.findElement(YourCartPageUI.APP_LOGO).isDisplayed();
    }

    public boolean isMenuActive(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(YourCartPageUI.MENU));
        driver.findElement(YourCartPageUI.MENU).click();

        // Tạo list các item trong menu để chờ và kiểm tra
        List<By> menuOptions = Arrays.asList(YourCartPageUI.ALL_ITEMS_LINK,YourCartPageUI.ABOUT_LINK,
                YourCartPageUI.LOGOUT_LINK, YourCartPageUI.RESET_APP_STATE_LINK);

        // Chờ tất cả các item hiển thị
        for (By option : menuOptions) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(option));
            } catch (Exception e) {
                System.out.println("Không tìm thấy hoặc không hiển thị: " + option);
                return false; // Nếu 1 item không hiển thị thì trả về false
            }
        }

        // Sau khi đã chờ đủ, xác nhận tất cả đều hiển thị
        for (By option : menuOptions) {
            if (!driver.findElement(option).isDisplayed()) {
                System.out.println("Item không hiển thị: " + option);
                return false;
            }
        }
        return true;
    }

    public boolean isShoppingCartDisplayed(){
        return driver.findElement(YourCartPageUI.SHOPPING_CART).isDisplayed();
    }


    public boolean isLabelDisplayed(){
        return driver.findElement(YourCartPageUI.QTY).isDisplayed() && driver.findElement(YourCartPageUI.DESC).isDisplayed();
    }

    public boolean isFooterDisplayed(){
        return driver.findElement(YourCartPageUI.LOGO_FACEBOOK).isDisplayed()
                && driver.findElement(YourCartPageUI.LOGO_TWITTER).isDisplayed()
                && driver.findElement(YourCartPageUI.LOGO_LINKEDIN).isDisplayed()
                && driver.findElement(YourCartPageUI.FOOTER_TEXT).isDisplayed();
    }

    public boolean isBackImageDisplayed(){
        return driver.findElement(YourCartPageUI.BACK_IMAGE).isDisplayed();
    }

    public void clickContinueShoppingButton(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(YourCartPageUI.CONTINUE_SHOPPING_BUTTON));
        driver.findElement(YourCartPageUI.CONTINUE_SHOPPING_BUTTON).click();
    }

    public void clickCheckoutButton(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(YourCartPageUI.CHECKOUT_BUTTON));
        driver.findElement(YourCartPageUI.CHECKOUT_BUTTON).click();
    }

    public boolean isProductInfoEnough(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(YourCartPageUI.CART_ITEM));

        for (WebElement product : driver.findElements(YourCartPageUI.CART_ITEM)){
            if (!product.findElement(YourCartPageUI.QTY_OF_ITEM).isDisplayed()
                    || !product.findElement(YourCartPageUI.NAME_OF_ITEM).isDisplayed()
                    || !product.findElement(YourCartPageUI.DESC_OF_ITEM).isDisplayed()
                    || !product.findElement(YourCartPageUI.PRICE_OF_ITEM).isDisplayed()
                    || !product.findElement(YourCartPageUI.REMOVE_FROM_CART_BUTTON).isDisplayed()){
                return false;
            }
        }
        return true;
    }

    // Trong YourCartPage
    public Map<String, String> getProductsInCart() {
        Map<String, String> products = new LinkedHashMap<>();
        for (WebElement item : driver.findElements(YourCartPageUI.CART_ITEM)) {
            String name = item.findElement(YourCartPageUI.NAME_OF_ITEM).getText().trim();
            String price = item.findElement(YourCartPageUI.PRICE_OF_ITEM).getText().trim();
            products.put(name, price);
        }
        return products;
    }

    public boolean isProductsInCartCorrect(Map<String, String> expectedProducts) {
        List<WebElement> itemsInCart = driver.findElements(YourCartPageUI.CART_ITEM);
        if (itemsInCart.size()== expectedProducts.size()){
            for (WebElement item : itemsInCart) {
                String name = item.findElement(YourCartPageUI.NAME_OF_ITEM).getText();
                String price = item.findElement(YourCartPageUI.PRICE_OF_ITEM).getText();
                if (!expectedProducts.containsKey(name) || !expectedProducts.get(name).equals(price)){
                    return false;
                }
            }
            return true;
        }else {
            return false;
        }
    }

    public void clickRemoveButton(String productName) {
        for (WebElement product : driver.findElements(YourCartPageUI.CART_ITEM)) {
            String name = product.findElement(YourCartPageUI.NAME_OF_ITEM).getText().trim();
            if (name.equalsIgnoreCase(productName)) {
                product.findElement(YourCartPageUI.REMOVE_FROM_CART_BUTTON).click();
                selectedProducts.remove(productName);
                break;
            }
        }
    }

    public int checkNumOfCart(){
        if(!driver.findElements(YourCartPageUI.NUM_OF_CART).isEmpty()) {
            return Integer.parseInt(driver.findElement(YourCartPageUI.NUM_OF_CART).getText().trim());
        } else {
            return 0;
        }
    }

    public void removeAllProductsInCart(){
        List<WebElement> removeButtons = driver.findElements(By.xpath("//button[contains(text(),'Remove')]"));
        for(WebElement btn : removeButtons){
            btn.click();
        }
        selectedProducts.clear();
    }

}
