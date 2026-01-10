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

import java.util.Objects;

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

    //Chekout:Information
    @Test(description = "Check UI/UX của Checkout:InformationPage")
    public void verifyCheckOutInformationPageUI() {
        setupCartWithProducts();

        yourCartPage.clickCheckoutButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        Assert.assertTrue(checkOutPage.isAPPLogoDisplayed(), "Logo is not display!");
        Assert.assertTrue(checkOutPage.isMenuActive(), "Menu is not active");
        Assert.assertTrue(checkOutPage.isShoppingCartDisplayed(), "ShoppingCart is not display");
        Assert.assertTrue(checkOutPage.isFooterDisplayed(), "Footer is not display");
    }


    @Test(description = "Xác minh checkout không thành công với giỏ hàng rỗng")
    public void testCheckoutWithCartEmpty() {
        checkOutPage.clickResetAppState();
        inventoryPage.clickShoppingCart();

        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(),"Không vào được ShoppingCart");
        Assert.assertEquals(checkOutPage.checkNumOfCart(), 0,"ShoppingCart không rỗng trước khi checkout!");
        yourCartPage.clickCheckoutButton();

        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Lỗi: Hệ thống cho phép checkout khi giỏ hàng rỗng");
        Assert.assertEquals(checkOutPage.checkNumOfCart(), 0, "ShoppingCart thay đổi sau khi checkout!");
    }

    @Test(description = "Xác minh hủy checkout thành công từ Checkout:Information Page")
    public void testCancelCheckoutInformation() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.clickCheckoutInformationCanceled();
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(),"Lỗi: Hủy checkout không thành công từ Checkout:Information Page");
    }

    @Test(description = "Xác minh checkout thành công với thông tin hợp lệ và giỏ hàng không rỗng")
    public void testCheckoutInformationSuccessfullyWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();

        Assert.assertTrue(checkOutPage.verifyOnCheckoutOverviewPage(),"Không vào được trang Checkout:Overview");
    }

    @Test(description = "Xác minh checkout không thành công khi bỏ trống First Name")
    public void testCheckoutInformationWhenCartNotEmptyWithEmptyFirstName() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();

        // Xác nhận báo lỗi
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");
        Assert.assertTrue(checkOutPage.isErrorIconDisplayed(), "Error icon is not displayed");
        Assert.assertTrue(checkOutPage.getErrorMessage().contains("is required"), "Unexpected error message!");
    }

    @Test(description = "Xác minh checkout không thành công khi bỏ trống Last Name")
    public void testCheckoutInformationWhenCartNotEmptyWithEmptyLastName() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "", "158390");
        checkOutPage.clickContinueButton();

        // Xác nhận báo lỗi
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");
        Assert.assertTrue(checkOutPage.isErrorIconDisplayed(), "Error icon is not displayed");
        Assert.assertTrue(checkOutPage.getErrorMessage().contains("is required"), "Unexpected error message!");
    }

    @Test(description = "Xác minh checkout không thành công khi bỏ trống Postal Code")
    public void testCheckoutInformationWhenCartNotEmptyWithEmptyPostalCode() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "");
        checkOutPage.clickContinueButton();
        // Xác nhận báo lỗi
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");
        Assert.assertTrue(checkOutPage.isErrorIconDisplayed(), "Error icon is not displayed");
        Assert.assertTrue(checkOutPage.getErrorMessage().contains("is required"), "Unexpected error message!");
    }

    @Test(description = "Kiểm tra phản hồi của Error button và Error message")
    public void testCloseErrorContainersOnCheckOutInformationPageWithCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "");
        checkOutPage.clickContinueButton();
        Assert.assertTrue(checkOutPage.isErrorMessageDisplayed());
        checkOutPage.clickErrorButton();
        Assert.assertFalse(checkOutPage.isErrorMessageDisplayed());
    }


    //Chekout:Overview
    @Test(description = "Check UI/UX của Checkout:Overview Page")
    public void verifyCheckOutOverviewPageUI() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();

        Assert.assertTrue(checkOutPage.verifyOnCheckoutOverviewPage(),"Should stay on Checkout: Overview page!");

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

    @Test(description = "Xác minh thông tin sản phẩm hiển thị tại Checkout:Overview Page là chính xác")
    public void checkProductsOnInvoiceWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutOverviewPage(),"Should stay on Checkout: Overview page!");

        Assert.assertEquals(checkOutPage.checkNumOfCart(), driver.findElements(CheckOutPageUI.CART_ITEM).size(), "Number of products in Shopping Cart is incorrect!");
        Assert.assertTrue(checkOutPage.areProductsSelected(), "The list displayed on the invoice is incorrect.");
    }

    @Test(description = "Kiểm tra thông tin về giá hiển thị tại Checkout:Overview Page")
    public void checkPriceTotalOfInvoiceWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();

        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutOverviewPage(),"Should stay on Checkout: Overview page!");

        Assert.assertTrue(checkOutPage.isPriceTotalCorrect(), "Price total of invoice is incorrect.");
    }

    @Test(description = "Xác minh hủy checkout thành công từ Checkout:Overview Page")
    public void testCancelFromOverviewWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutOverviewPage(),"Should stay on Checkout: Overview page!");

        checkOutPage.clickCancelFromOverview();
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(),"Should stay on Inventory page!");

    }

    @Test(description = "Xác minh checkout thành công từ Checkout:Overview Page")
    public void testFinishCheckoutWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutOverviewPage(),"Should stay on Checkout: Overview page!");

        checkOutPage.clickFinish();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutCompletePage(),"Should stay on Checkout: Complete! page!");
        Assert.assertTrue(checkOutPage.isCompletedHeaderDisplayed(), "Header is not displayed!");
    }


    @Test(description = "Xác minh xem thông tin sản phẩm trong đơn hàng thành công từ Checkout:Overview Page")
    public void testViewDetailFromOverview() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutOverviewPage(),"Should stay on Checkout: Overview page!");

        checkOutPage.clickItemName();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id="));
    }

    //Checkout:Complete
    @Test(description = "Check UI/UX của Checkout:Complete! Page")
    public void verifyCheckOutCompletePageUI() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutOverviewPage(),"Should stay on Checkout: Overview page!");

        checkOutPage.clickFinish();

        Assert.assertTrue(checkOutPage.verifyOnCheckoutCompletePage(),"Should stay on Checkout: Complete! page!");
        Assert.assertTrue(checkOutPage.isCompletedHeaderDisplayed(), "Complete header is not displayed!");
        Assert.assertTrue(checkOutPage.isBackHomeButtonDisplayed(), "Back Home button is not displayed!");
    }

    @Test(description = "Xác minh thành công truy cập InventoryPage từ Checkout: Complete! Page")
    public void testBackHomeButtonWhenCartNotEmpty() {
        setupCartWithProducts();
        yourCartPage.clickCheckoutButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");

        checkOutPage.inputYourInformation("THOM", "NGUYEN", "158390");
        checkOutPage.clickContinueButton();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutOverviewPage(),"Should stay on Checkout: Overview page!");

        checkOutPage.clickFinish();
        Assert.assertTrue(checkOutPage.verifyOnCheckoutCompletePage(),"Should stay on Checkout: Complete! page!");

        Assert.assertTrue(checkOutPage.isCompletedHeaderDisplayed(), "Header is not displayed!");
        Assert.assertTrue(checkOutPage.isBackHomeButtonDisplayed(), "Back To Home Button is not displayed!");

        checkOutPage.clickBackHomeButton();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html", "URL mismatch!");

    }   
}
