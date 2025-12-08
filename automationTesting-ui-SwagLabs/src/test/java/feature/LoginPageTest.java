package feature;

import action.LoginPage;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Hook;

public class LoginPageTest extends Hook {
    LoginPage loginPage;

    @BeforeMethod
    public void setUpPage(){
        loginPage = new LoginPage(driver);
    }

    @Feature("LoginPage Testing")

    @Test(description = "Xác minh đăng nhập thành công với username và password hợp lệ")
    public void testLoginSuccessfully(){
        loginPage.verifyOnLoginPage();

        // Điền thông tin đúng
        loginPage.inputLogin("standard_user", "secret_sauce");
        loginPage.clickLoginButton();

        // Xác nhận vào trang inventory
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html", "Login failed!");

        // Kiểm tra logo & sản phẩm
        loginPage.isLogoDisplayed();
        loginPage.getInventoryCount();
    }

    @Test(description = "Xác minh đăng nhập không thành công khi bỏ trống password")
    public void testLoginWithEmptyPassword(){
        loginPage.verifyOnLoginPage();

        // Điền thông tin: bỏ trống password
        loginPage.inputLogin("standard_user", "");
        loginPage.clickLoginButton();

        // Xác nhận báo lỗi
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/", "Should stay on login page!");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match") || loginPage.getErrorMessage().contains("Epic sadface"), "Unexpected error message!");
    }

    @Test(description = "Xác minh đăng nhập không thành công khi bỏ trống Username")
    public void testLoginWithEmptyUsername(){
        loginPage.verifyOnLoginPage();

        // Điền thông tin: bỏ trống username
        loginPage.inputLogin("", "secret_sauce");
        loginPage.clickLoginButton();

        // Xác nhận báo lỗi
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/", "Should stay on login page!");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match") || loginPage.getErrorMessage().contains("Epic sadface"), "Unexpected error message!");
    }

    @Test(description = "Xác minh đăng nhập không thành công khi nhập Username không hợp lệ")
    public void testLoginWithInvalidUsername(){
        loginPage.verifyOnLoginPage();

        // Điền thông tin: sai username
        loginPage.inputLogin("standard_user1", "secret_sauce");
        loginPage.clickLoginButton();

        // Xác nhận báo lỗi
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/", "Should stay on login page!");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match") || loginPage.getErrorMessage().contains("Epic sadface"), "Unexpected error message!");

    }

    @Test(description = "Xác minh đăng nhập không thành công khi nhập Password không hợp lệ")
    public void testLoginWithInvalidPassword(){
        loginPage.verifyOnLoginPage();

        // Điền thông tin: sai password
        loginPage.inputLogin("standard_user", "secret_sauce1");
        loginPage.clickLoginButton();

        // Xác nhận báo lỗi
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/", "Should stay on login page!");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match") || loginPage.getErrorMessage().contains("Epic sadface"), "Unexpected error message!");

    }
}
