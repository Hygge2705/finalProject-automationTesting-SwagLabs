package feature;

import action.InventoryPage;
import action.LoginPage;
import io.qameta.allure.Feature;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
    public void preToTest(){
        //khởi tạo biến wait
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        //đăng nhập vào inventoryPage
        driver.get("https://www.saucedemo.com/");
        loginPage =new LoginPage(driver);
        loginPage.inputLogin("standard_user","secret_sauce");
        loginPage.clickLoginButton();
        inventoryPage = new InventoryPage(driver);
    }

    @Feature("Menu Testing")

    @Test(description = "Kiểm tra trạng thái active của button Menu")
    public void verifyMenuActive(){
        Assert.assertTrue(inventoryPage.isMenuActive(),"Menu is not active!");
    }

    @Test(description = "Kiểm tra trạng thái active của option 'All Items'")
    public void verifyAllItemsButtonIsActive(){
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("react-burger-menu-btn"))));
        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("inventory_sidebar_link"))));
        driver.findElement(By.id("inventory_sidebar_link")).click();
        inventoryPage.verifyOnInventoryPage();
    }

    @Test(description = "Kiểm tra trạng thái active của option 'About'")
    public void verifyAboutButtonIsActive(){
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("react-burger-menu-btn"))));
        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("about_sidebar_link"))));
        driver.findElement(By.id("about_sidebar_link")).click();
        Assert.assertEquals(driver.getTitle(), "Sauce Labs: Cross Browser Testing, Selenium Testing & Mobile Testing", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://saucelabs.com/", "URL mismatch!");
    }


    @Test(description = "Kiểm tra trạng thái active của option 'LogOut'")
    public void verifyLogOutButtonIsActive(){
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("react-burger-menu-btn"))));
        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("logout_sidebar_link"))));
        driver.findElement(By.id("logout_sidebar_link")).click();
        loginPage.verifyOnLoginPage();
    }

    @Test(description = "Kiểm tra trạng thái active của option 'Reset App State'")
    public void verifyResetAppStateButtonIsActive(){
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("react-burger-menu-btn"))));
        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("reset_sidebar_link"))));
        driver.findElement(By.id("reset_sidebar_link")).click();
        Assert.assertTrue(driver.findElements(By.className("shopping_cart_badge")).isEmpty(),"Reset App State is not active.");
        Assert.assertTrue(driver.findElements(By.cssSelector("button[data-test^='remove']")).isEmpty(),"UI not updating by reset option");
    }
}
