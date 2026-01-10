package feature;

import action.CheckOutPage;
import action.InventoryPage;
import action.LoginPage;
import action.YourCartPage;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Hook;

import static utils.TestContext.selectedProducts;

public class YourCartPageTest extends Hook {
    YourCartPage yourCartPage;
    InventoryPage inventoryPage;
    LoginPage loginPage;
    CheckOutPage checkoutPage;

    @BeforeMethod
    public void preToTest(){
        selectedProducts.clear();
        //Truy cập vào trang web và đăng nhập
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);

        //Khởi tạo đối tượng page inventory
        inventoryPage = new InventoryPage(driver);

        //Khởi tạo đối tượng yourCartPage
        yourCartPage = new YourCartPage(driver);

        //Khởi tạo đối tượng checkoutInfomationPage
        checkoutPage = new CheckOutPage(driver);

        loginPage.inputLogin("standard_user", "secret_sauce");
        loginPage.clickLoginButton();
        inventoryPage.clickShoppingCart();

    }
    @Feature("YourCartPage Testing")

    @Test(description = "Check UI/UX")
    public void checkYourCartPageUI(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");

        Assert.assertTrue(yourCartPage.isAPPLogoDisplayed(),"Logo is not displayed!");
        Assert.assertTrue(yourCartPage.isMenuActive(),"Menu is not active");
        Assert.assertTrue(yourCartPage.isShoppingCartDisplayed(),"ShoppingCart is not displayed");
        Assert.assertTrue(yourCartPage.isLabelDisplayed(),"Label is not displayed");
        Assert.assertTrue(yourCartPage.isFooterDisplayed(), "Footer is not displayed");
        Assert.assertTrue(yourCartPage.isBackImageDisplayed(),"Back image is not displayed!");
        if (yourCartPage.checkNumOfCart() != 0 && yourCartPage.checkNumOfCart() == selectedProducts.size()){
            Assert.assertTrue(yourCartPage.isProductInfoEnough(),"Information of products is not enough.");
        }
    }

    @Test(description = "Xác minh button 'Continue Shopping' hoạt động đúng khi session còn hiệu lực")
    public void verifyContinueShoppingButtonActive(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");

        yourCartPage.clickContinueShoppingButton();
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Shout stay on Inventory page");
    }

    @Test(description = "Xác minh 'Continue Shopping' không thành công khi session hết hiệu lực")
    public void continueShoppingFailsWhenSessionExpired(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");

        driver.manage().deleteAllCookies();

        yourCartPage.clickContinueShoppingButton();
        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Shout stay on Login page");
    }

    @Test(description = "Xác minh button 'Checkout' hoạt động đúng khi session còn hiệu lực")
    public void verifyCheckoutButtonActive(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");

        yourCartPage.clickCheckoutButton();
        Assert.assertTrue(checkoutPage.verifyOnCheckoutInformationPage(),"Should stay on checkout information page!");
    }

    @Test(description = "Xác minh checkout không thành công khi session hết hiệu lực")
    public void checkoutFailsWhenSessionExpired(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");

        driver.manage().deleteAllCookies();

        yourCartPage.clickCheckoutButton();
        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Shout stay on Login page");
    }

    @Test(description = "Xác minh checkout không thành công với giỏ hàng trống")
    public void checkoutFailsWhenShoppingCartIsEmpty(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");

        //kiểm tra giỏ hàng trống
        if (yourCartPage.checkNumOfCart() > 0) {
            yourCartPage.removeAllProductsInCart(); // method này sẽ implement thêm trong YourCartPage
            Assert.assertEquals(yourCartPage.checkNumOfCart(), 0, "Cart is not empty after removal!");
        }

        yourCartPage.clickCheckoutButton();

        // Verify không chuyển trang
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");

    }

    @Test(description = "Kiểm tra sản phẩm trong giỏ hàng sau khi thêm")
    public void checkProductInCartAfterAdd(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");

        String productName = "Sauce Labs Backpack";
        //Add to cart
        yourCartPage.clickContinueShoppingButton();
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Shout stay on Inventory page");
        inventoryPage.clickAddToCart(productName);
        inventoryPage.clickShoppingCart();
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");
        Assert.assertTrue(yourCartPage.isProductsInCartCorrect(selectedProducts), "Information of products in Shopping Cart is incorrect!");
    }

    @Test(description = "Kiểm tra sản phẩm trong giỏ hàng sau khi refresh Your cart Page")
    public void checkProductInCartAfterRefresh(){
        String productName = "Sauce Labs Backpack";
        //Add to cart
        yourCartPage.clickContinueShoppingButton();
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Shout stay on Inventory page");
        inventoryPage.clickAddToCart(productName);
        inventoryPage.clickShoppingCart();
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");
        Assert.assertEquals(yourCartPage.checkNumOfCart(), selectedProducts.size(), "Cart quantity is incorrect before refresh!");
        Assert.assertTrue(yourCartPage.isProductsInCartCorrect(selectedProducts), "Information of products in Shopping Cart is incorrect before refresh!");

        driver.navigate().refresh();

        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page after refresh!");
        Assert.assertEquals(yourCartPage.checkNumOfCart(), selectedProducts.size(), "Cart quantity is incorrect after refresh!");
        Assert.assertTrue(yourCartPage.isProductsInCartCorrect(selectedProducts), "Information of products in Shopping Cart is incorrect after refresh!!");
    }


    @Test(description = "Kiểm tra sản phẩm trong giỏ hàng sau khi re-login")
    public void checkProductInCartAfterReLogin(){
        String productName = "Sauce Labs Backpack";
        //Add to cart
        yourCartPage.clickContinueShoppingButton();
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Shout stay on Inventory page");
        inventoryPage.clickAddToCart(productName);
        inventoryPage.clickShoppingCart();
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");
        Assert.assertEquals(yourCartPage.checkNumOfCart(), selectedProducts.size(), "Cart quantity is incorrect before refresh!");
        Assert.assertTrue(yourCartPage.isProductsInCartCorrect(selectedProducts), "Information of products in Shopping Cart is incorrect before refresh!");

        inventoryPage.openMenu();
        inventoryPage.clickLogout();
        Assert.assertTrue(loginPage.verifyOnLoginPage(),"Should stay on Login page");

        loginPage.inputLogin("standard_user", "secret_sauce");
        loginPage.clickLoginButton();
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Shout stay on Inventory page");
        inventoryPage.clickShoppingCart();

        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page after refresh!");
        Assert.assertEquals(yourCartPage.checkNumOfCart(), selectedProducts.size(), "Cart quantity is incorrect after refresh!");
        Assert.assertTrue(yourCartPage.isProductsInCartCorrect(selectedProducts), "Information of products in Shopping Cart is incorrect after refresh!!");
    }

    @Test(description = "Kiểm tra sản phẩm trong giỏ hàng sau khi click 'Remove' trực tiếp từ giỏ")
    public void checkProductInCartAfterRemoveFromCart(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");

        String productName = "Sauce Labs Backpack";
        //Remove from cart
        yourCartPage.clickRemoveButton(productName);
        Assert.assertTrue(yourCartPage.isProductsInCartCorrect(selectedProducts), "Information of products in Shopping Cart is incorrect!");
    }

    @Test(description = "Kiểm tra sản phẩm trong giỏ hàng sau khi click 'Remove' từ inventoryPage")
    public void checkProductInCartAfterRemoveFromInventoryPage(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Shout stay on Your cart page");

        String productName = "Sauce Labs Backpack";

        //Remove from InventoryPage
        yourCartPage.clickContinueShoppingButton();
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Shout stay on Inventory page");
        if(inventoryPage.isButtonAddToCart(productName)){
            inventoryPage.clickAddToCart(productName);
            Assert.assertFalse(inventoryPage.isButtonAddToCart(productName), "Remove button for " + productName + " is not displayed!");
        }
        inventoryPage.clickRemove(productName);
        Assert.assertTrue(yourCartPage.isProductsInCartCorrect(selectedProducts), "Information of products in Shopping Cart is incorrect!");
    }

}
