package ui;

import org.openqa.selenium.By;

public class InventoryPageUI {
    public static final By APP_LOGO = By.className("app_logo");
    public static final By PAGE_TITLE = By.cssSelector("[data-test='title']");

    //Menu
    public static final By MENU = By.id("react-burger-menu-btn");
    public static final By ALL_ITEMS_LINK = By.id("inventory_sidebar_link");
    public static final By ABOUT_LINK = By.id("about_sidebar_link");
    public static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    public static final By RESET_APP_STATE_LINK = By.id("reset_sidebar_link");
    public static final By CLOSE = By.id("react-burger-cross-btn");
    public static final By MENU_HIDDEN = By.cssSelector("[aria-hidden='false']");

    //Sort_by
    public static final By SORt_BY = By.cssSelector("[data-test='product-sort-container']");
    public static final By SORT_BY_NAME_AZ = By.cssSelector("[value='az']");
    public static final By SORT_BY_NAME_ZA = By.cssSelector("[value='za']");
    public static final By SORT_BY_PRICE_LOW = By.cssSelector("[value='lohi']");
    public static final By SORT_BY_PRICE_HIGH = By.cssSelector("[value='hilo']");

    //item
    public static final By INVENTORY_ITEM = By.className("inventory_item");
    public static final By PRODUCT_IMAGE = By.className("inventory_item_img");
    public static final By PRODUCT_NAME = By.className("inventory_item_name");
    public static final By PRODUCT_PRICE = By.className("inventory_item_price");
    public static final By ADD_TO_CART_BUTTON = By.cssSelector("button[data-test^='add-to-cart']");
    public static final By REMOVE_FROM_CART_BUTTON = By.cssSelector("button[data-test^='remove']");

    public static final By BACK_BUTTON = By.id("back-to-products");

    public static final By SHOPPING_CART = By.id("shopping_cart_container");
    public static final By NUM_OF_CART = By.className("shopping_cart_badge");

    public static final By LOGO_TWITTER = By.cssSelector("a[data-test='social-twitter']");
    public static final By LOGO_FACEBOOK = By.cssSelector("a[data-test='social-facebook']");
    public static final By LOGO_LINKEDIN = By.cssSelector("a[data-test='social-linkedin']");
    public static final By FOOTER_TEXT = By.cssSelector("[data-test='footer-copy']");

}