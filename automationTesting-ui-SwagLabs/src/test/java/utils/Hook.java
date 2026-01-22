package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;

public class Hook {
    protected WebDriver driver;

    @BeforeMethod
    public void setUpWeb() {
        // 1) Auto-download chromedriver
        WebDriverManager.chromedriver().setup();

        // 2) Chrome options
        ChromeOptions options = new ChromeOptions();

        // 1) Chrome Incognito (Ẩn danh giúp tránh cookies/profile/popups)
        options.addArguments("--incognito");

//        // 2) Tắt password manager / save password prompt
//        Map<String, Object> prefs = new HashMap<>();
//        prefs.put("credentials_enable_service", false);
//        prefs.put("profile.password_manager_enabled", false);
//        options.setExperimentalOption("prefs", prefs);
//
//        // 3) Loại bỏ thông báo "Chrome is being controlled..."
//        options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
//        options.addArguments("--disable-blink-features=AutomationControlled");
//
//        // 4) Một số flag nhẹ để ổn định (tuỳ chọn)
//        options.addArguments("--disable-notifications");
//        options.addArguments("--disable-popup-blocking");

        driver = new ChromeDriver(options);

        // Timeouts (giữ implicit = 0 để ưu tiên explicit waits)
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com");

    }

    @AfterMethod
    public void bugImgReport(ITestResult result) {
        if (driver != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                String methodName = result.getMethod().getMethodName();
                try {
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Files.write(Paths.get("target/allure-results/" + methodName + "_Fail.png"), screenshot);
                } catch (Exception e) {
                    System.out.println("Không thể chụp ảnh màn hình: " + e.getMessage());
                }
            }
        }
    }
    @AfterMethod
    public void exitWeb() {
        if (driver != null) {
            driver.quit();
        }
    }


}
