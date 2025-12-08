package ui;

import org.openqa.selenium.By;

public class CheckOutPageUI {
    public static final By APP_LOGO = By.className("app_logo");
    public static final By PAGE_TITLE = By.cssSelector("[data-test='title']");

    public static final By SHOPPING_CART = By.id("shopping_cart_container");
    public static final By NUM_OF_CART = By.className("shopping_cart_badge");

    public static final By MENU = By.id("react-burger-menu-btn");
    public static final By ALL_ITEMS_LINK = By.id("inventory_sidebar_link");
    public static final By ABOUT_LINK = By.id("about_sidebar_link");
    public static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    public static final By RESET_APP_STATE_LINK = By.id("reset_sidebar_link");

    public static final By FOOTER = By.className("footer");

    //Checkout: Your Information
    public static final By FIRST_NAME_FIELD = By.id("first-name");
    public static final By LAST_NAME_FIELD = By.id("last-name");
    public static final By CODE_FIELD = By.id("postal-code");
    public static final By CANCEL_BUTTON = By.id("cancel");
    public static final By CONTINUE_BUTTON = By.id("continue");

    public static final By ERROR_MESSAGE = By.className("error");
    public static final By ERROR_BUTTON = By.className("error-button");
    public static final By ERROR_ICON = By.cssSelector("[data-icon='times-circle']");

    //Checkout: Overview
    public static final By QTY = By.className("cart_quantity_label");
    public static final By DESC = By.className("cart_desc_label");

    public static final By CART_ITEM = By.className("cart_item");
    public static final By QTY_OF_IEM = By.className("cart_quantity");
    public static final By NAME_OF_ITEM = By.className("inventory_item_name");
    public static final By DESC_OF_ITEM = By.className("inventory_item_desc");
    public static final By PRICE_OF_ITEM = By.className("inventory_item_price");

    public static final By PAYMENT_INFO_LABEL = By.cssSelector("[data-test='payment-info-label']");
    public static final By PAYMENT_INFO_VALUE = By.cssSelector("[data-test='payment-info-value']");
    public static final By SHIPPING_INFO_LABEL = By.cssSelector("[data-test='shipping-info-label']");
    public static final By SHIPPING_INFO_VALUE = By.cssSelector("[data-test='shipping-info-value']");
    public static final By PRICE_TOTAL_LABEL = By.cssSelector("[data-test='total-info-label']");
    public static final By ITEM_TOTAL_LABEL = By.className("summary_subtotal_label");
    public static final By TAX_LABEL = By.className("summary_tax_label");
    public static final By TOTAL_LABEL = By.className("summary_total_label");

    public static final By CANCEL_CHECKOUT_BUTTON = By.id("cancel");
    public static final By FINISH_CHECKOUT_BUTTON = By.id("finish");

    //Checkout: Complete!
    public static final By COMPLETED_HEADER = By.className("complete-header");
    public static final By BACK_HOME_BUTTON = By.id("back-to-products");

}