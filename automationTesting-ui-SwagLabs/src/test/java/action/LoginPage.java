package action;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import ui.LoginPageUI;

public class LoginPage {
    WebDriver driver;

    public LoginPage(WebDriver driver){
        this.driver = driver;
    }

    public void inputLogin (String username, String password){
        driver.findElement(LoginPageUI.USERNAME_FIELD).sendKeys(username);
        driver.findElement(LoginPageUI.PASSWORD_FIELD).sendKeys(password);
    }

    public void clickLoginButton(){
        driver.findElement(LoginPageUI.LOGIN_BUTTON).click();
    }

    //Kiểm tra tiêu đề và url
    public void verifyOnLoginPage() {
        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title mismatch!");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/", "URL mismatch!");
    }


    //Kiểm tra hiển thị logo
    public boolean isLogoDisplayed(){
        return driver.findElement(LoginPageUI.APP_LOGO).isDisplayed();
    }

    //Kiểm tra sau hiển thị sản phẩm sau khi đăng nhập
    public int getInventoryCount(){
        return driver.findElements(LoginPageUI.INVENTORY_ITEM).size();
    }

    //Lấy nội dung thông báo lỗi
    public String getErrorMessage(){
        return driver.findElement(LoginPageUI.ERROR_MESSAGE).getText();
    }

}
