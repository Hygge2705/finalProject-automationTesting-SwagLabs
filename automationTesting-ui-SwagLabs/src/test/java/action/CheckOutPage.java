package action;

import ui.CheckOutPageUI;
import ui.LoginPageUI;
import ui.YourCartPageUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Hook;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static utils.TestContext.selectedProducts;

public class CheckOutPage extends Hook {
    WebDriver driver;
    WebDriverWait wait;

    public CheckOutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }


    //1. Check UI
    public boolean isAPPLogoDisplayed() {
        return driver.findElement(CheckOutPageUI.APP_LOGO).isDisplayed();
    }

    public boolean isMenuActive() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.MENU));
        driver.findElement(CheckOutPageUI.MENU).click();

        // Tạo list các item trong menu để chờ và kiểm tra
        List<By> menuOptions = Arrays.asList(CheckOutPageUI.ALL_ITEMS_LINK, CheckOutPageUI.ABOUT_LINK,
                CheckOutPageUI.LOGOUT_LINK, CheckOutPageUI.RESET_APP_STATE_LINK);

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

    public void clickResetAppState() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.MENU));
        driver.findElement(CheckOutPageUI.MENU).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.RESET_APP_STATE_LINK));
        driver.findElement(CheckOutPageUI.RESET_APP_STATE_LINK).click();
    }

    public String getTitleCheckOutPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.PAGE_TITLE));
        return driver.findElement(CheckOutPageUI.PAGE_TITLE).getText();
    }

    public boolean isShoppingCartDisplayed() {
        return driver.findElement(CheckOutPageUI.SHOPPING_CART).isDisplayed();
    }


    public boolean isFooterDisplayed() {
        return driver.findElement(CheckOutPageUI.FOOTER).isDisplayed();
    }

    //2. Checkout: Your Information
    public void clickCheckoutInformationCanceled() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.CANCEL_BUTTON));
        driver.findElement(CheckOutPageUI.CANCEL_BUTTON).click();
    }

    public void inputYourInformation(String firstName, String lastName, String postalCode) {
        driver.findElement(CheckOutPageUI.FIRST_NAME_FIELD).sendKeys(firstName);
        driver.findElement(CheckOutPageUI.LAST_NAME_FIELD).sendKeys(lastName);
        driver.findElement(CheckOutPageUI.CODE_FIELD).sendKeys(postalCode);
    }

    public void clickContinueButton() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.CONTINUE_BUTTON));
        driver.findElement(CheckOutPageUI.CONTINUE_BUTTON).click();
    }

    public String getErrorMessage() {
        return driver.findElement(LoginPageUI.ERROR_MESSAGE).getText();
    }

    public void clickErrorButton() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.ERROR_BUTTON));
        driver.findElement(CheckOutPageUI.ERROR_BUTTON).click();
    }

    public boolean isErrorMessageDisplayed() {
        return !driver.findElements(CheckOutPageUI.ERROR_MESSAGE).isEmpty();
    }

    public boolean isErrorIconDisplayed() {
        return !driver.findElements(CheckOutPageUI.ERROR_ICON).isEmpty();
    }

    //3. Checkout: Overview
    public boolean isLabelDisplayed() {
        return driver.findElement(CheckOutPageUI.QTY).isDisplayed() && driver.findElement(CheckOutPageUI.DESC).isDisplayed();
    }

    public boolean isProductInfoEnough() {
        if (driver.findElements(CheckOutPageUI.CART_ITEM).isEmpty()) {
            return false;
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.CART_ITEM));

        for (WebElement product : driver.findElements(CheckOutPageUI.CART_ITEM)) {
            if (!product.findElement(CheckOutPageUI.QTY_OF_IEM).isDisplayed()
                    || !product.findElement(CheckOutPageUI.NAME_OF_ITEM).isDisplayed()
                    || !product.findElement(CheckOutPageUI.DESC_OF_ITEM).isDisplayed()
                    || !product.findElement(CheckOutPageUI.PRICE_OF_ITEM).isDisplayed()) {
                return false;
            }
        }
        return true;  // Nếu mọi thứ đều ổn
    }

    public int checkNumOfCart() {
        if (!driver.findElements(CheckOutPageUI.NUM_OF_CART).isEmpty()) {
            return Integer.parseInt(driver.findElement(CheckOutPageUI.NUM_OF_CART).getText().trim());
        } else {
            return 0;
        }
    }

    public boolean areProductsSelected() {
        if (driver.findElements(CheckOutPageUI.CART_ITEM).size() == selectedProducts.size() && !selectedProducts.isEmpty()) {
            for (WebElement product : driver.findElements(CheckOutPageUI.CART_ITEM)) {
                String name = product.findElement(CheckOutPageUI.NAME_OF_ITEM).getText();
                String price = product.findElement(YourCartPageUI.PRICE_OF_ITEM).getText();
                if (!selectedProducts.containsKey(name) || !selectedProducts.get(name).equals(price)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isSummaryPaymentDisplayed() {
        return driver.findElement(CheckOutPageUI.PAYMENT_INFO_LABEL).isDisplayed()
                && driver.findElement(CheckOutPageUI.PAYMENT_INFO_VALUE).isDisplayed();
    }

    public boolean isSummaryShippingDisplayed() {
        return driver.findElement(CheckOutPageUI.SHIPPING_INFO_LABEL).isDisplayed()
                && driver.findElement(CheckOutPageUI.SHIPPING_INFO_VALUE).isDisplayed();
    }

    public boolean isPriceTotalDisplayed() {
        return driver.findElement(CheckOutPageUI.PRICE_TOTAL_LABEL).isDisplayed()
                && driver.findElement(CheckOutPageUI.ITEM_TOTAL_LABEL).isDisplayed()
                && driver.findElement(CheckOutPageUI.TAX_LABEL).isDisplayed()
                && driver.findElement(CheckOutPageUI.TOTAL_LABEL).isDisplayed();
    }

    public boolean isPriceTotalCorrect() {
        double sumOfPrice = 0;
        for (WebElement product : driver.findElements(CheckOutPageUI.CART_ITEM)) {
            double priceItem = Double.parseDouble(product.findElement(CheckOutPageUI.PRICE_OF_ITEM).getText().replace("$", "").trim());
            double qtyItem = Double.parseDouble(product.findElement(CheckOutPageUI.QTY_OF_IEM).getText().trim());
            sumOfPrice += priceItem * qtyItem;
        }

        double itemTotal = Double.parseDouble(driver.findElement(CheckOutPageUI.ITEM_TOTAL_LABEL).getText().replace("Item total: $", "").trim());
        double tax = Double.parseDouble(driver.findElement(CheckOutPageUI.TAX_LABEL).getText().replace("Tax: $", "").trim());
        double total = Double.parseDouble(driver.findElement(CheckOutPageUI.TOTAL_LABEL).getText().replace("Total: $", "").trim());

        return Math.abs(sumOfPrice - itemTotal) < 0.01 && Math.abs(sumOfPrice * 0.08 - tax) < 0.01 && Math.abs(total - itemTotal - tax) < 0.01;
    }

    public void clickCancelFromOverview() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.CANCEL_CHECKOUT_BUTTON));
        driver.findElement(CheckOutPageUI.CANCEL_CHECKOUT_BUTTON).click();

    }

    public void clickFinish() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.FINISH_CHECKOUT_BUTTON));
        driver.findElement(CheckOutPageUI.FINISH_CHECKOUT_BUTTON).click();
    }

    //    Checkout: Complete!
    public boolean isCompletedHeaderDisplayed() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.COMPLETED_HEADER));
        return driver.findElement(CheckOutPageUI.COMPLETED_HEADER).isDisplayed();
    }

    public boolean isBackHomeButtonDisplayed() {
        return driver.findElement(CheckOutPageUI.BACK_HOME_BUTTON).isDisplayed();
    }

    public void clickBackHomeButton() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CheckOutPageUI.BACK_HOME_BUTTON));
        driver.findElement(CheckOutPageUI.BACK_HOME_BUTTON).click();
    }
}
