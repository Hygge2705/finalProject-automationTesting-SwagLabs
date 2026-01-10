package feature;

import action.InventoryPage;
import action.LoginPage;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Hook;
import static utils.TestContext.selectedProducts;

public class InventoryPageTest extends Hook {
    InventoryPage inventoryPage;
    LoginPage loginPage;

    @BeforeMethod
    public void preToTest(){
        selectedProducts.clear();
        //Truy cập vào trang web và đăng nhập
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
        loginPage.inputLogin("standard_user", "secret_sauce");
        loginPage.clickLoginButton();

        //Khởi tạo đối tượng page inventory
        inventoryPage = new InventoryPage(driver);
    }

    @Feature("InventoryPage Testing")

    @Test(description = "Check UI/UX")
    public void checkInventoryPageUI(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");
        Assert.assertTrue(inventoryPage.isLogoDisplayed(),"Logo không hiển thị!");
        inventoryPage.openMenu();
        Assert.assertTrue(inventoryPage.isMenuActive(),"Menu không hoạt động!");
        Assert.assertTrue(inventoryPage.isSortByActive(),"SortBy không hoạt động!");
        Assert.assertEquals(inventoryPage.getInventoryCount(),6,"Số lượng sản phẩm không đúng!");
        Assert.assertTrue(inventoryPage.isFooterDisplayed());
        Assert.assertTrue(inventoryPage.isShoppingCartActive(),"ShoppingCart không hiển thị!");
    }

    @Test(description = "Kiểm tra truy cập Inventory Page khi session hết hiệu lực")
    public void accessInventoryWithoutLogin() {
        driver.manage().deleteAllCookies();
        driver.get("https://www.saucedemo.com/inventory.html");

        Assert.assertTrue(loginPage.verifyOnLoginPage(), "Should stay on Login Page");
    }
    @Test(description = "Xem chi tiết sản phẩm thành công bằng tên sản phẩm")
    public void getDetailsByName(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");

        inventoryPage.clickProductName("Sauce Labs Backpack");
        Assert.assertTrue(inventoryPage.verifyOnProductDetailPage(),"Should stay on Product Detail Page!");
    }

    @Test(description = "Xem chi tiết sản phẩm thành công bằng ảnh minh họa")
    public void getDetailsByImage(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");

        inventoryPage.clickProductImage("Sauce Labs Backpack");
        Assert.assertTrue(inventoryPage.verifyOnProductDetailPage(),"Should stay on Product Detail Page!");
    }

    @Test(description = "Xác minh button 'Back to products' hoạt động đúng")
    public void checkBackButtonActive(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");

        inventoryPage.clickProductImage("Sauce Labs Backpack");
        Assert.assertTrue(inventoryPage.verifyOnProductDetailPage(),"Should stay on Product Detail Page!");

        inventoryPage.clickBackToProductsButton();
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");
    }

    @Test(description = "Thêm sản phẩm vào giỏ hàng từ trang chi tiết sản phẩm")
    public void checkAddToCartFromDetailPage(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");
        inventoryPage.openMenu();
        inventoryPage.clickResetAppState();
        String productName = "Sauce Labs Backpack";
        inventoryPage.clickProductName(productName);
        Assert.assertTrue(inventoryPage.verifyOnProductDetailPage(),"Should stay on Product Detail Page!");

        if (selectedProducts.containsKey(productName)){
            inventoryPage.clickRemove(productName);
            Assert.assertEquals(inventoryPage.checkNumOfCart(), selectedProducts.size(), "Number of products in Shopping Cart is incorrect!");
            Assert.assertFalse(inventoryPage.isButtonAddToCart(productName), "Add to cart button for " + productName + " is not displayed!");
        }
        inventoryPage.clickAddToCart(productName);
        Assert.assertFalse(inventoryPage.isButtonAddToCart(productName));
        Assert.assertEquals(inventoryPage.checkNumOfCart(), selectedProducts.size(), "Number of products in Shopping Cart is incorrect!");
    }

    @Test(description = "Xóa sản phẩm khỏi giỏ hàng từ trang chi tiết sản phẩm")
    public void checkRemoveButtonFromDetailPage(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");

        String productName1 = "Sauce Labs Backpack";

        if(inventoryPage.isButtonAddToCart(productName1)){
            inventoryPage.clickAddToCart(productName1);
            Assert.assertFalse(inventoryPage.isButtonAddToCart(productName1), "Remove button for " + productName1 + " is not displayed!");
            Assert.assertEquals(inventoryPage.checkNumOfCart(), selectedProducts.size(), "Number of products in Shopping Cart is incorrect!");
        }
        inventoryPage.clickRemove(productName1);
        Assert.assertTrue(inventoryPage.isButtonAddToCart(productName1));
        Assert.assertEquals(inventoryPage.checkNumOfCart(), selectedProducts.size(), "Number of products in Shopping Cart is incorrect!");
    }

    @Test(description = "Kiểm tra sắp xếp sản phẩm theo tên từ AZ")
    public void checkSortedByNameAZ(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");

        inventoryPage.clickToSortByNameAZ();
        Assert.assertTrue(inventoryPage.isSortedByNameAZ(), "Product list not sorted from A-Z.");
    }

    @Test(description = "Kiểm tra sắp xếp sản phẩm theo tên từ ZA")
    public void checkSortedByNameZA(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");

        inventoryPage.clickToSortByNameZA();
        Assert.assertTrue(inventoryPage.isSortedByNameZA(), "Product list not sorted from A-Z.");
    }

    @Test(description = "Kiểm tra sắp xếp sản phẩm theo giá tăng dần")
    public void checkSortByPriceLow(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");

        inventoryPage.clickToSortByPriceLow();
        Assert.assertTrue(inventoryPage.isSortedByPriceLowToHigh(), "Product list not sorted from A-Z.");
    }

    @Test(description = "Kiểm tra sắp xếp sản phẩm theo giá giảm dần")
    public void checkSortByPriceHigh(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");

        inventoryPage.clickToSortByPriceHigh();
        Assert.assertTrue(inventoryPage.isSortedByPriceHighToLow(), "Product list not sorted from A-Z.");
    }

    @Test(description = "Thêm sản phẩm vào giỏ hàng từ InventoryPage")
    public void checkAddToCart(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");

        String productName = "Sauce Labs Backpack";

        if (!inventoryPage.isButtonAddToCart(productName)){
            inventoryPage.clickRemove(productName);
            Assert.assertTrue(inventoryPage.isButtonAddToCart(productName), "Add to cart button for " + productName + " is not displayed!");
            Assert.assertEquals(inventoryPage.checkNumOfCart(), selectedProducts.size(), "Number of products in Shopping Cart is incorrect!");
        }
        inventoryPage.clickAddToCart(productName);
        Assert.assertFalse(inventoryPage.isButtonAddToCart(productName));
        Assert.assertEquals(inventoryPage.checkNumOfCart(), selectedProducts.size(), "Number of products in Shopping Cart is incorrect!");
    }

    @Test(description = "Xóa sản phẩm khỏi giỏ hàng từ InventoryPage")
    public void checkRemoveButton(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");

        String productName1 = "Sauce Labs Backpack";

        if(inventoryPage.isButtonAddToCart(productName1)){
            inventoryPage.clickAddToCart(productName1);
            Assert.assertFalse(inventoryPage.isButtonAddToCart(productName1), "Remove button for " + productName1 + " is not displayed!");
            Assert.assertEquals(inventoryPage.checkNumOfCart(), selectedProducts.size(), "Number of products in Shopping Cart is incorrect!");
        }
        inventoryPage.clickRemove(productName1);
        Assert.assertTrue(inventoryPage.isButtonAddToCart(productName1));
        Assert.assertEquals(inventoryPage.checkNumOfCart(), selectedProducts.size(), "Number of products in Shopping Cart is incorrect!");
    }

    @Test(description = "Kiểm tra Logout từ InventoryPage")
    public void verifyLogoutFromInventoryPage(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");
        inventoryPage.openMenu();
        inventoryPage.clickLogout();

        Assert.assertTrue(inventoryPage.isLogoutSuccessful(),"Logout không thành công!");
    }

    @Test(description = "Kiểm tra quyền truy cập InventoryPage sau khi Logout")
    public void verifyCannotAccessInventoryAfterLogout(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");
        inventoryPage.openMenu();
        inventoryPage.clickLogout();
        driver.navigate().back();
        Assert.assertTrue(inventoryPage.isLogoutSuccessful(),"Lỗi: User có thể truy cập InventoryPage sau logout!");
    }

    @Test(description = "Kiểm tra Reset App State từ InventoryPage khi giỏ hàng rỗng")
    public void verifyResetFromInventoryPageWithEmptyCart(){
        if (inventoryPage.checkNumOfCart() > 0) {
            inventoryPage.openMenu();
            inventoryPage.clickResetAppState();
            Assert.assertTrue(inventoryPage.isCartEmpty(),"Không thể làm trống giỏ hàng trước khi test!");
        }

        inventoryPage.openMenu();
        inventoryPage.clickResetAppState();

        Assert.assertTrue(inventoryPage.isCartEmpty(),"Cart không rỗng sau khi Reset App State!");
        Assert.assertTrue(inventoryPage.areAllProductsReset(),"UI không reset về trạng thái Add to cart!");
    }

    @Test(description = "Kiểm tra Reset App State từ InventoryPage khi giỏ hàng không rỗng")
    public void verifyResetFromInventoryPageWithCartNotEmpty(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Should stay on Inventory Page!");

        // Đảm bảo giỏ hàng không rỗng
        if (inventoryPage.checkNumOfCart() == 0) {
            String productName = "Sauce Labs Backpack";
            inventoryPage.clickAddToCart(productName);
            Assert.assertTrue(inventoryPage.checkNumOfCart() > 0,"Không thể thêm sản phẩm vào giỏ hàng!");
        }

        inventoryPage.openMenu();
        inventoryPage.clickResetAppState();

        Assert.assertTrue(inventoryPage.isCartEmpty(),"Cart không rỗng sau khi Reset App State!");
        Assert.assertTrue(inventoryPage.areAllProductsReset(),"UI có item không reset về trạng thái Add to cart!");
    }

}
