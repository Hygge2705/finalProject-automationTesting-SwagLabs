package feature;

import action.InventoryPage;
import action.LoginPage;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Hook;

import java.util.Objects;

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
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");
        Assert.assertTrue(inventoryPage.isLogoDisplayed(),"Logo is not display!");
        Assert.assertTrue(inventoryPage.isMenuActive(),"Menu is not active");
        Assert.assertTrue(inventoryPage.isSortByActive(),"SortBy is not active");
        Assert.assertEquals(inventoryPage.getInventoryCount(),6,"Number of products is not enough.");
        Assert.assertTrue(inventoryPage.isFooterDisplayed());
        Assert.assertTrue(inventoryPage.isShoppingCartActive(),"ShoppingCart is not display!");
    }

    @Test(description = "Xem chi tiết sản phẩm thành công bằng tên sản phẩm")
    public void getDetailsByName(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");

        inventoryPage.clickProductName("Sauce Labs Backpack");
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id="));
    }

    @Test(description = "Xem chi tiết sản phẩm thành công bằng ảnh minh họa")
    public void getDetailsByImage(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");

        inventoryPage.clickProductImage("Sauce Labs Backpack");
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id="));
    }

    @Test(description = "Xác minh button 'Back to products' hoạt động đúng")
    public void checkBackButtonActive(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");

        inventoryPage.clickProductImage("Sauce Labs Backpack");
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id="));

        inventoryPage.clickBackToProductsButton();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html", "URL mismatch!");
    }

    @Test(description = "Thêm sản phẩm vào giỏ hàng từ trang chi tiết sản phẩm")
    public void checkAddToCartFromDetailPage(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");

        inventoryPage.clickResetAppState();
        String productName = "Sauce Labs Backpack";
        inventoryPage.clickProductName(productName);
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id="));

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
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");

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
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");

        inventoryPage.clickToSortByNameAZ();
        Assert.assertTrue(inventoryPage.isSortedByNameAZ(), "Product list not sorted from A-Z.");
    }

    @Test(description = "Kiểm tra sắp xếp sản phẩm theo tên từ ZA")
    public void checkSortedByNameZA(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");

        inventoryPage.clickToSortByNameZA();
        Assert.assertTrue(inventoryPage.isSortedByNameZA(), "Product list not sorted from A-Z.");
    }

    @Test(description = "Kiểm tra sắp xếp sản phẩm theo giá tăng dần")
    public void checkSortByPriceLow(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");

        inventoryPage.clickToSortByPriceLow();
        Assert.assertTrue(inventoryPage.isSortedByPriceLowToHigh(), "Product list not sorted from A-Z.");
    }

    @Test(description = "Kiểm tra sắp xếp sản phẩm theo giá giảm dần")
    public void checkSortByPriceHigh(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");

        inventoryPage.clickToSortByPriceHigh();
        Assert.assertTrue(inventoryPage.isSortedByPriceHighToLow(), "Product list not sorted from A-Z.");
    }

    @Test(description = "Thêm sản phẩm vào giỏ hàng từ InventoryPage")
    public void checkAddToCart(){
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");

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
        Assert.assertTrue(inventoryPage.verifyOnInventoryPage(), "Không vào được trang Inventory!");

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
}
