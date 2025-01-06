package page;

import core.BasePage;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;

public class UpgradePlanPage extends BasePage {
    private BrowserMobProxyServer proxyServer;
    private PaymentQRPage paymentQR;
    @FindBy(xpath = "(//button[@class=\"btn btn-xl box-popup-register\"])[3]")
    private WebElement btnUpgrade;
    @FindBy(xpath = "//button[@class=\"btn btn-primary btn-xl btn-pay k-flex-1\"]")
    private WebElement btnContinuePay;

    public UpgradePlanPage(WebDriver driver, BrowserMobProxyServer proxyServer, PaymentQRPage paymentQR) {
        super(driver);
        this.proxyServer = proxyServer;
        this.paymentQR = paymentQR;
    }

    public void upgradePlan() throws IOException, InterruptedException {
        getWebDriverWait().until(ExpectedConditions.visibilityOf(btnUpgrade));
        btnUpgrade.click();
        proxyServer.newHar("topup-har");
        Thread.sleep(3000);
        getWebDriverWait().until(ExpectedConditions.visibilityOf(btnContinuePay));
        btnContinuePay.click();
        getWebDriverWait().until(ExpectedConditions.visibilityOf(btnContinuePay));
        btnContinuePay.click();
        paymentQR.actionPay();
    }
}
