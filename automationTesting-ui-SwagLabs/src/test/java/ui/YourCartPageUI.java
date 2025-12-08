package ui;

import org.openqa.selenium.By;

public class YourCartPageUI {
    public static final By APP_LOGO = By.className("app_logo");
    public static final By PAGE_TITLE = By.cssSelector("[data-test='title']");


    //Menu
    public static final By MENU = By.id("react-burger-menu-btn");
    public static final By ALL_ITEMS_LINK = By.id("inventory_sidebar_link");
    public static final By ABOUT_LINK = By.id("about_sidebar_link");
    public static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    public static final By RESET_APP_STATE_LINK = By.id("reset_sidebar_link");

    //shopping cart icon
    public static final By SHOPPING_CART = By.id("shopping_cart_container");
    public static final By NUM_OF_CART = By.className("shopping_cart_badge");

    //Label
    public static final By QTY = By.className("cart_quantity_label");
    public static final By DESC = By.className("cart_desc_label");

    //item
    public static final By CART_ITEM = By.className("cart_item");
    public static final By QTY_OF_ITEM = By.className("cart_quantity");
    public static final By NAME_OF_ITEM = By.className("inventory_item_name");
    public static final By DESC_OF_ITEM = By.className("inventory_item_desc");
    public static final By PRICE_OF_ITEM = By.className("inventory_item_price");
    public static final By REMOVE_FROM_CART_BUTTON = By.cssSelector("button[data-test^='remove']");

    public static final By CONTINUE_SHOPPING_BUTTON = By.id("continue-shopping");
    public static final By BACK_IMAGE = By.className("back-image");
    public static final By CHECKOUT_BUTTON = By.id("checkout");

    //Footer
    public static final By LOGO_TWITTER = By.cssSelector("a[data-test='social-twitter']");
    public static final By LOGO_FACEBOOK = By.cssSelector("a[data-test='social-facebook']");
    public static final By LOGO_LINKEDIN = By.cssSelector("a[data-test='social-linkedin']");
    public static final By FOOTER_TEXT = By.cssSelector("[data-test='footer-copy']");

}