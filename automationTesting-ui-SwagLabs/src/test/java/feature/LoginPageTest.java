package feature;

import action.InventoryPage;
import action.LoginPage;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Hook;

public class LoginPageTest extends Hook {
    LoginPage loginPage;
    InventoryPage inventoryPage;

    @BeforeMethod
    public void setUpPage(){
        loginPage = new LoginPage(driver);
        //Khởi tạo đối tượng page inventory
        inventoryPage = new InventoryPage(driver);
    }

    @Feature("LoginPage Testing")

    @Test(description = "Xác minh đăng nhập thành công với username và password hợp lệ")
    public void testLoginSuccessfully(){
        loginPage.verifyOnLoginPage();

        // Điền thông tin đúng
        loginPage.inputLogin("standard_user", "secret_sauce");
        loginPage.clickLoginButton();

        // Xác nhận vào trang inventory
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory page!");

        // Kiểm tra logo & sản phẩm
        Assert.assertTrue(loginPage.isLogoDisplayed(), "Logo isn't displayed!");
        Assert.assertFalse(loginPage.getInventoryCount(), "Quantity of items is null!");
    }

    @Test(description = "Xác minh đăng nhập không thành công khi bỏ trống password")
    public void testLoginWithEmptyPassword(){
        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Should stay on login page!");

        // Điền thông tin: bỏ trống password
        loginPage.inputLogin("standard_user", "");
        loginPage.clickLoginButton();

        // Xác nhận báo lỗi
        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Should stay on login page!");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match") || loginPage.getErrorMessage().contains("Epic sadface"), "Unexpected error message!");
    }

    @Test(description = "Xác minh đăng nhập không thành công khi bỏ trống Username")
    public void testLoginWithEmptyUsername(){
        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Should stay on login page!");

        // Điền thông tin: bỏ trống username
        loginPage.inputLogin("", "secret_sauce");
        loginPage.clickLoginButton();

        // Xác nhận báo lỗi
        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Should stay on login page!");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match") || loginPage.getErrorMessage().contains("Epic sadface"), "Unexpected error message!");
    }

    @Test(description = "Xác minh đăng nhập không thành công khi nhập Username không hợp lệ")
    public void testLoginWithInvalidUsername(){
        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Should stay on login page!");

        // Điền thông tin: sai username
        loginPage.inputLogin("standard_user1", "secret_sauce");
        loginPage.clickLoginButton();

        // Xác nhận báo lỗi
        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Should stay on login page!");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match") || loginPage.getErrorMessage().contains("Epic sadface"), "Unexpected error message!");

    }

    @Test(description = "Xác minh đăng nhập không thành công khi nhập Password không hợp lệ")
    public void testLoginWithInvalidPassword(){
        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Should stay on login page!");

        // Điền thông tin: sai password
        loginPage.inputLogin("standard_user", "secret_sauce1");
        loginPage.clickLoginButton();

        // Xác nhận báo lỗi
        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Should stay on login page!");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match") || loginPage.getErrorMessage().contains("Epic sadface"), "Unexpected error message!");
    }

    @Test(description = "Kiểm tra sự duy trì của phiên đăng nhập")
    public void checkSessionPersistenceAfterRefresh(){
        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Should stay on login page!");

        // Điền thông tin đúng
        loginPage.inputLogin("standard_user", "secret_sauce");
        loginPage.clickLoginButton();

        // Xác nhận vào trang inventory
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory page!");

        //refresh trang
        driver.navigate().refresh();

        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory page!");

    }
}
