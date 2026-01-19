package feature;

import action.InventoryPage;
import action.LoginPage;
import io.qameta.allure.Feature;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Hook;

import java.time.Duration;

public class MenuTest extends Hook {
    LoginPage loginPage;
    InventoryPage inventoryPage;
    WebDriverWait wait;

    @BeforeMethod
    public void preToTest() {
        //khởi tạo biến wait
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        //đăng nhập vào inventoryPage
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
        loginPage.inputLogin("standard_user", "secret_sauce");
        loginPage.clickLoginButton();
        inventoryPage = new InventoryPage(driver);
    }

    @Feature("Menu Testing")

    @Test(description = "Kiểm tra trạng thái active của button Menu")
    public void verifyMenuActive() {
        inventoryPage.openMenu();
        Assert.assertTrue(inventoryPage.isMenuActive(), "Menu is not active!");
    }

    @Test(description = "Kiểm tra trạng thái active của option 'All Items'")
    public void verifyAllItemsButtonIsActive() {
        inventoryPage.openMenu();
        inventoryPage.clickAllItems();

        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Shout stay on Your Inventory page");
    }

    @Test(description = "Kiểm tra trạng thái active của option 'About'")
    public void verifyAboutButtonIsActive() {
        inventoryPage.openMenu();
        inventoryPage.clickAbout();
        Assert.assertEquals(driver.getTitle(), "Sauce Labs: Cross Browser Testing, Selenium Testing & Mobile Testing", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://saucelabs.com/", "URL mismatch!");
    }


    @Test(description = "Kiểm tra trạng thái active của option 'LogOut'")
    public void verifyLogOutButtonIsActive() {
        inventoryPage.openMenu();
        inventoryPage.clickLogout();
        loginPage.verifyOnLoginPage();
    }

    @Test(description = "Kiểm tra trạng thái active của option 'Reset App State'")
    public void verifyResetAppStateButtonIsActive() {
        inventoryPage.openMenu();
        inventoryPage.clickResetAppState();
        Assert.assertTrue(driver.findElements(By.className("shopping_cart_badge")).isEmpty(), "Reset App State is not active.");
        Assert.assertTrue(driver.findElements(By.cssSelector("button[data-test^='remove']")).isEmpty(), "UI not updating by reset option");
    }

    @Test(description = "Kiểm tra đóng menu")
    public void verifyClosedMenu() {
        inventoryPage.openMenu();
        inventoryPage.closeMenu();
        Assert.assertTrue(inventoryPage.isMenuClosed(), "Menu is not closed.");
    }

    @Test(description = "Stress test: Click Menu button nhiều lần")
    public void stressClickMenuButton() {
        int stressTimes = 20;
        for (int i = 0; i < stressTimes; i++) {
            inventoryPage.openMenu();
            inventoryPage.closeMenu();
        }
        inventoryPage.openMenu();
        Assert.assertTrue(inventoryPage.isMenuActive(), "Menu is not active after stress testing!");
    }

}
