package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait webDriverWait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));  // Thiết lập WebDriverWait
        PageFactory.initElements(driver, this);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
    public WebDriver getDriver(){
    return this.driver;
    }

    public WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }

    public void inputText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }
    // Nhập văn bản
    public void typeText(WebElement element, String text) {
        waitForElement(element).sendKeys(text);
    }
    // Chờ phần tử xuất hiện
    public WebElement waitForElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }


    // Nhấn nút hoặc phần tử
    public void click(WebElement element) {
        waitForElement(element).click();
    }
    // Get text from element
    public static String getText(WebElement element) {
        return element.getText();
    }
    // Chuyển sang tab
    public void switchToTab(int index) {
        driver.switchTo().window(driver.getWindowHandles().toArray()[index].toString());
    }
    // Mở tab mới
    public void openNewTab() {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.open('');");
    }
}

