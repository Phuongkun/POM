package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import java.time.Duration;

public class basePage {
    protected WebDriver driver;
    protected WebDriverWait webDriverWait;

    public basePage(WebDriver driver) {
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
}

