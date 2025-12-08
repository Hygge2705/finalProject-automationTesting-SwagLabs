package feature;

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

        loginPage.inputLogin("standard_user", "secret_sauce");
        loginPage.clickLoginButton();
        inventoryPage.clickShoppingCart();

    }
    @Feature("YourCartPage Testing")

    @Test(description = "Check UI/UX")
    public void checkYourCartPageUI(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Không vào được trang Your cart!");

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

    @Test(description = "Xác minh button 'Continue' hoạt động đúng khi session còn hiệu lực")
    public void verifyContinueButtonActive(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Không vào được trang Your cart!");

        yourCartPage.clickContinueButton();
        Assert.assertEquals(driver.getCurrentUrl(),"https://www.saucedemo.com/inventory.html","Inventory Page is not display!");
    }

    @Test(description = "Xác minh 'Continue Shopping' không thành công khi session hết hiệu lực")
    public void continueShoppingFailsWhenSessionExpired(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Không vào được trang Your cart!");

        driver.manage().deleteAllCookies();

        yourCartPage.clickContinueButton();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/", "Should stay on login page!");
    }

    @Test(description = "Xác minh button 'Checkout' hoạt động đúng khi session còn hiệu lực")
    public void verifyCheckoutButtonActive(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Không vào được trang Your cart!");

        yourCartPage.clickCheckoutButton();
        Assert.assertEquals(driver.getCurrentUrl(),"https://www.saucedemo.com/checkout-step-one.html","ShoppingCart is not display!");
    }

    @Test(description = "Xác minh checkout không thành công khi session hết hiệu lực")
    public void checkoutFailsWhenSessionExpired(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Không vào được trang Your cart!");

        driver.manage().deleteAllCookies();

        yourCartPage.clickCheckoutButton();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/", "Should stay on login page!");
    }

    @Test(description = "Xác minh checkout không thành công với giỏ hàng trống")
    public void checkoutFailsWhenShoppingCartIsEmpty(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Không vào được trang Your cart!");

        //kiểm tra giỏ hàng trống
        if (yourCartPage.checkNumOfCart() > 0) {
            yourCartPage.removeAllProductsInCart(); // method này sẽ implement thêm trong YourCartPage
            Assert.assertEquals(yourCartPage.checkNumOfCart(), 0, "Cart is not empty after removal!");
        }

        yourCartPage.clickCheckoutButton();

        // Verify không chuyển trang
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Không vào được trang Your cart!");

    }

    @Test(description = "Kiểm tra sản phẩm trong giỏ hàng sau khi thêm")
    public void checkProductInCartAfterAdd(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Không vào được trang Your cart!");

        String productName = "Sauce Labs Backpack";
        //Add to cart
        yourCartPage.clickContinueButton();
        Assert.assertEquals(driver.getCurrentUrl(),"https://www.saucedemo.com/inventory.html","Inventory Page is not display!");
        inventoryPage.clickAddToCart(productName);
        inventoryPage.clickShoppingCart();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/cart.html", "URL mismatch!");
        Assert.assertTrue(yourCartPage.isProductsInCartCorrect(selectedProducts), "Information of products in Shopping Cart is incorrect!");
    }

    @Test(description = "Kiểm tra sản phẩm trong giỏ hàng sau khi click 'Remove' trực tiếp từ giỏ")
    public void checkProductInCartAfterRemoveFromCart(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Không vào được trang Your cart!");

        String productName = "Sauce Labs Backpack";
        //Remove from cart
        yourCartPage.clickRemoveButton(productName);
        Assert.assertTrue(yourCartPage.isProductsInCartCorrect(selectedProducts), "Information of products in Shopping Cart is incorrect!");
    }

    @Test(description = "Kiểm tra sản phẩm trong giỏ hàng sau khi click 'Remove' từ inventoryPage")
    public void checkProductInCartAfterRemoveFromInventoryPage(){
        Assert.assertTrue(yourCartPage.verifyOnYourCartPage(), "Không vào được trang Your cart!");

        String productName = "Sauce Labs Backpack";

        //Remove from InventoryPage
        yourCartPage.clickContinueButton();
        Assert.assertEquals(driver.getCurrentUrl(),"https://www.saucedemo.com/inventory.html","Inventory Page is not display!");
        if(inventoryPage.isButtonAddToCart(productName)){
            inventoryPage.clickAddToCart(productName);
            Assert.assertFalse(inventoryPage.isButtonAddToCart(productName), "Remove button for " + productName + " is not displayed!");
        }
        inventoryPage.clickRemove(productName);
        Assert.assertTrue(yourCartPage.isProductsInCartCorrect(selectedProducts), "Information of products in Shopping Cart is incorrect!");
    }

}
