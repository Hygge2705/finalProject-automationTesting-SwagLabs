package action;

import org.openqa.selenium.WebDriver;
import ui.LoginPageUI;
import java.util.Objects;

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
    public boolean verifyOnLoginPage() {
        return Objects.equals(driver.getTitle(),"Swag Labs")
                && Objects.requireNonNull(driver.getCurrentUrl()).contains("www.saucedemo.com");
    }

    //Kiểm tra hiển thị logo
    public boolean isLogoDisplayed(){
        return driver.findElement(LoginPageUI.APP_LOGO).isDisplayed();
    }

    //Kiểm tra sau hiển thị sản phẩm sau khi đăng nhập
    public boolean getInventoryCount(){
        return driver.findElements(LoginPageUI.INVENTORY_ITEM).isEmpty();
    }

    //Lấy nội dung thông báo lỗi
    public String getErrorMessage(){
        return driver.findElement(LoginPageUI.ERROR_MESSAGE).getText();
    }

}
