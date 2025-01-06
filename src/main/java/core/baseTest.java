package core;

//import Page.PaymentPage; // Assuming this is defined elsewhere
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver; // Change to your desired browser
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;

import net.lightbody.bmp.BrowserMobProxyServer;
public class BaseTest {
    public WebDriver driver;
    public BrowserMobProxyServer proxy;
    private WebDriverWait webDriverWait;

    @BeforeTest
    public void beforeSuite() {
        try {
            proxy = new BrowserMobProxyServer();
            proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

            proxy.start(0); // Sử dụng cổng ngẫu nhiên
            // Kiểm tra xem proxy đã khởi động thành công hay chưa
            if (proxy.isStarted()) {
                System.out.println("Proxy started successfully on port: " + proxy.getPort());
            } else {
                System.out.println("Failed to start proxy.");
                return; // Thoát khỏi hàm nếu proxy không khởi động thành công
            }
            // Initialize WebDriver
            System.setProperty("webdriver.gecko.driver", "D:\\autophuong\\geckodriver.exe");
            // Or use WebDriverManager to initialize the driver
            // Create WebDriverWait
            //webDriverWait = new WebDriverWait(driver, 10);
            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
            FirefoxOptions options = new FirefoxOptions();
            options.setCapability(CapabilityType.PROXY, seleniumProxy);
            options.setProxy(seleniumProxy);
            driver = new FirefoxDriver(options);
            proxy.newHar("topUp-har");
        }catch (Exception e){
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace(); // In ra stack trace chi tiết
        }
        System.out.println("Proxy initialized: " + (proxy != null));
    }

    @AfterSuite
    public void afterSuite() {
        // Close driver and stop proxy
        proxy.stop();
        System.out.println("Proxy stopped.");
    }

    // Getters for WebDriver and WebDriverWait
    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }

    public BrowserMobProxyServer getProxy() {
        return proxy;
    }
}
