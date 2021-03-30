package Fakestore;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Cart {


    WebDriver driver;
    WebDriverWait wait;

    By productPageAddToCartButton = By.cssSelector("[name='add-to-cart");
    By categoryPageAddToCartButton = By.cssSelector("[data-product_id='61']");
    By removeProductButton = By.cssSelector("[class='remove']");
    By productPageViewCartButton = By.cssSelector(".woocommerce-message>.button");
    By tableWithInfo = By.cssSelector("[id='post-6']");



    @BeforeEach
    public void driverSetup () {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().window().maximize();

        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);

        driver.navigate().to("https://fakestore.testelka.pl");
        driver.findElement(By.cssSelector("[class='woocommerce-store-notice__dismiss-link']")).click();
    }


    @Test
    public void addToCartFromProductPageTest () {
        addProductToCart("https://fakestore.testelka.pl/product/wakacje-z-yoga-w-kraju-kwitnacej-wisni/");
        WebElement cartMessage = driver.findElement(By.cssSelector("[class='woocommerce-message"));
        Assertions.assertEquals("Zobacz koszyk\n" + "„Wakacje z yogą w Kraju Kwitnącej Wiśni“ został dodany do koszyka.",
                cartMessage.getText(), "Text is not correct.");
    }

    @Test
    public void addToCartFromCategoryPageTest () {
        driver.navigate().to("https://fakestore.testelka.pl/product-category/yoga-i-pilates/");
        driver.findElement(categoryPageAddToCartButton).click();
        By viewCartButton = By.cssSelector(".added_to_cart");
        wait.until(ExpectedConditions.elementToBeClickable(viewCartButton));
        driver.findElement(viewCartButton).click();
    }

    @Test
    public void add10ElementsToCartTest () {
        addProductAndViewCart("https://fakestore.testelka.pl/product/wakacje-z-yoga-w-kraju-kwitnacej-wisni/", "10");
        WebElement itemsInCart = driver.findElement(By.cssSelector("[type='number']"));
        Assertions.assertEquals("10", itemsInCart.getAttribute("value"), "Number of items in cart is not correct." );
    }

    @Test
    public void changeNumberOfProductsTest () {
        addProductAndViewCart("https://fakestore.testelka.pl/product/wakacje-z-yoga-w-kraju-kwitnacej-wisni/", "10");
        WebElement itemsInCart = driver.findElement(By.cssSelector("[type='number']"));
        itemsInCart.clear();
        itemsInCart.sendKeys("12");
        Assertions.assertEquals("12", itemsInCart.getAttribute("value"), "Number of items in cart is not correct." );
    }

    @Test
    public void removeProductsTestWithRemovingButton () {
        addProductAndViewCart("https://fakestore.testelka.pl/product/wakacje-z-yoga-w-kraju-kwitnacej-wisni/", "10");
        driver.findElement(removeProductButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class='cart-empty woocommerce-info']")));
        WebElement emptyCartInfo = driver.findElement(By.cssSelector("[class='cart-empty woocommerce-info']"));
        Assertions.assertTrue(emptyCartInfo.isDisplayed());
    }

    @AfterEach
    public void driverQuit () {

        driver.quit();
    }

    public void addProductToCart () {
        WebElement addToCartButton = driver.findElement(productPageAddToCartButton);
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView()", addToCartButton);
        addToCartButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(productPageViewCartButton));
    }

    public void addProductToCart (String productPageUrl) {
        driver.navigate().to(productPageUrl);
        addProductToCart();
    }

    public void addProductToCart (String productPageUrl, String quantity) {
        driver.navigate().to(productPageUrl);
        WebElement quantityChange = driver.findElement(By.cssSelector("[class='input-text qty text']"));
        quantityChange.clear();
        quantityChange.sendKeys(quantity);
        addProductToCart();
    }

    public void viewCart () {
        wait.until(ExpectedConditions.elementToBeClickable(productPageViewCartButton)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(tableWithInfo));
    }


    public void addProductAndViewCart (String productPageUrl, String quantity) {
        addProductToCart(productPageUrl, quantity);
        viewCart();



    }


}


