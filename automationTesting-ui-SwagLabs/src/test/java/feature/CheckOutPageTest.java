package feature;

import ui.CheckOutPageUI;
import action.CheckOutPage;
import action.InventoryPage;
import action.LoginPage;
import action.YourCartPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Hook;

public class CheckOutPageTest extends Hook {
    LoginPage loginPage;
    InventoryPage inventoryPage;
    YourCartPage yourCartPage;
    CheckOutPage checkOutPage;

    @BeforeMethod
    public void preToTest() {
        //Truy cập vào trang web và đăng nhập
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
        loginPage.inputLogin("standard_user", "secret_sauce");
        loginPage.clickLoginButton();

        //Khởi tạo đối tượng page
        inventoryPage = new InventoryPage(driver);
        yourCartPage = new YourCartPage(driver);
        checkOutPage = new CheckOutPage(driver);
    }

    private void setupCartWithProducts() {
        inventoryPage.clickAddToCart("Sauce Labs Backpack");
        inventoryPage.clickAddToCart("Sauce Labs Bike Light");
        inventoryPage.clickShoppingCart();
        Assert.assertTrue(yourCartPage.checkNumOfCart() > 0, "Cart is empty!");
    }

    @Test
    public void checkCheckOutInformationPageUI() {
        setupCartWithProducts();

        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");
        Assert.assertEquals(checkOutPage.getTitleCheckOutPage(), "Checkout: Your Information", "Title mismatch!");

        Assert.assertTrue(checkOutPage.isAPPLogoDisplayed(), "Logo is not display!");
        Assert.assertTrue(checkOutPage.isMenuActive(), "Menu is not active");
        Assert.assertTrue(checkOutPage.isShoppingCartDisplayed(), "ShoppingCart is not display");
        Assert.assertTrue(checkOutPage.isFooterDisplayed(), "Footer is not display");
    }


    @Test
    public void testCheckoutWithCartEmpty() {
        checkOutPage.clickResetAppState();
        inventoryPage.clickShoppingCart();
        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/cart.html", "URL mismatch!");

        yourCartPage.clickCheckoutButton();
        //chỉ cho phép checkout với số lượng sản phẩm trong giỏ lớn hơn 0
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/cart.html", "URL mismatch!");
    }

    @Test
    public void testCancelCheckoutInformation() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.clickCheckoutInformationCanceled();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/cart.html", "URL mismatch!");
    }


    @Test
    public void testCheckoutInformationSuccessfullyWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();

        Assert.assertEquals(checkOutPage.getTitleCheckOutPage(), "Checkout: Overview", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-two.html", "URL mismatch!");
    }

    @Test
    public void testCheckoutInformationWhenCartNotEmptyWithEmptyFirstName() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.inputYourInformation("", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();
        // Xác nhận báo lỗi
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "Should stay on checkout information page!");
        Assert.assertTrue(checkOutPage.isErrorIconDisplayed(), "Error icon is not displayed");
        Assert.assertTrue(checkOutPage.getErrorMessage().contains("is required"), "Unexpected error message!");
    }

    @Test
    public void testCheckoutInformationWhenCartNotEmptyWithEmptyLastName() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.inputYourInformation("THOM", "", "158390");
        checkOutPage.clickContinueButton();
        // Xác nhận báo lỗi
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "Should stay on checkout information page!");
        Assert.assertTrue(checkOutPage.isErrorIconDisplayed(), "Error icon is not displayed");
        Assert.assertTrue(checkOutPage.getErrorMessage().contains("is required"), "Unexpected error message!");
    }

    @Test
    public void testCheckoutInformationWhenCartNotEmptyWithEmptyPostalCode() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "");
        checkOutPage.clickContinueButton();
        // Xác nhận báo lỗi
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "Should stay on checkout information page!");
        Assert.assertTrue(checkOutPage.isErrorIconDisplayed(), "Error icon is not displayed");
        Assert.assertTrue(checkOutPage.getErrorMessage().contains("is required"), "Unexpected error message!");
    }

    @Test
    public void testCloseErrorContainersOnCheckOutInformationPageWithCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "");
        checkOutPage.clickContinueButton();
        Assert.assertTrue(checkOutPage.isErrorMessageDisplayed());
        checkOutPage.clickErrorButton();
        Assert.assertFalse(checkOutPage.isErrorMessageDisplayed());
    }

    @Test
    public void testCheckOutOverviewPageUIWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-two.html", "URL mismatch!");
        Assert.assertEquals(checkOutPage.getTitleCheckOutPage(), "Checkout: Overview", "Title mismatch!");

        Assert.assertTrue(checkOutPage.isAPPLogoDisplayed(), "Logo is not display!");
        Assert.assertTrue(checkOutPage.isLabelDisplayed(), "Label is not display");
        Assert.assertTrue(checkOutPage.isProductInfoEnough(), "Number of products is not enough.");
        Assert.assertTrue(checkOutPage.isSummaryPaymentDisplayed(), "Payment information is not displayed");
        Assert.assertTrue(checkOutPage.isSummaryShippingDisplayed(), "Shipping information is not displayed");
        Assert.assertTrue(checkOutPage.isPriceTotalDisplayed(), "Price total is not displayed");
        Assert.assertTrue(checkOutPage.isShoppingCartDisplayed(), "ShoppingCart is not display");
        Assert.assertTrue(checkOutPage.isFooterDisplayed(), "Footer is not display");
        Assert.assertTrue(checkOutPage.isMenuActive(), "Menu is not active");

    }

    @Test
    public void checkProductsOnInvoiceWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();

        Assert.assertEquals(checkOutPage.checkNumOfCart(), driver.findElements(CheckOutPageUI.CART_ITEM).size(), "Number of products in Shopping Cart is incorrect!");
        Assert.assertTrue(checkOutPage.areProductsSelected(), "The list displayed on the invoice is incorrect.");
    }

    @Test
    public void checkPriceTotalOfInvoiceWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();

        Assert.assertTrue(checkOutPage.isPriceTotalCorrect(), "Price total of invoice is incorrect.");
    }

    @Test
    public void testCancelFromOverviewWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();

        checkOutPage.clickCancelFromOverview();
        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html", "URL mismatch!");
    }

    @Test
    public void testFinishCheckoutWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();

        checkOutPage.clickFinish();
        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-complete.html", "URL mismatch!");
        Assert.assertTrue(checkOutPage.isCompletedHeaderDisplayed(), "Header is not displayed!");
    }

    @Test
    public void testBackHomeButtonWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html", "URL mismatch!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-two.html", "URL mismatch!");

        checkOutPage.clickFinish();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-complete.html", "URL mismatch!");
        Assert.assertTrue(checkOutPage.isCompletedHeaderDisplayed(), "Header is not displayed!");
        Assert.assertTrue(checkOutPage.isBackHomeButtonDisplayed(), "Back To Home Button is not displayed!");

        checkOutPage.clickBackHomeButton();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html", "URL mismatch!");

    }   
}
