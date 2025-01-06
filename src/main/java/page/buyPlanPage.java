package page;

import core.BasePage;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.Random;

import static org.openqa.selenium.devtools.v85.fetch.Fetch.getResponseBody;

public class BuyPlanPage extends BasePage {
    @FindBy(xpath = "(//button[@class=\"btn btn-xl box-popup-register\"])[2]")
    private WebElement btnBuyPlan;
    @FindBy(id = "name")
    private WebElement txtName;
    @FindBy(xpath = "//input[@value=\"MALE\"]")
    private WebElement radioGender;
    @FindBy(id = "phone")
    private WebElement txtPhone;
    @FindBy(id = "email")
    private WebElement txtEmail;
    @FindBy(id = "identity_code")
    private WebElement txtIdentityCode;
    @FindBy(id = "address")
    private WebElement address;
    @FindBy(xpath = "//input[@value=\"COMPANY\"]")
    private WebElement radioCompany;
    @FindBy(id = "company_name")
    private WebElement companyName;
    @FindBy(id = "company_tax_code")
    private WebElement companyTaxCode;
    @FindBy(id = "agent_name")
    private WebElement agentName;
    @FindBy(id = "agent_phone")
    private WebElement agentPhone;
    @FindBy(id = "company_address")
    private WebElement companyAddress;
    @FindBy(id = "company_email")
    private WebElement companyEmail;
    @FindBy(id = "isReceivedEInvoice")
    private WebElement isReceivedEInvoice;
    @FindBy(id = "ship_name")
    private WebElement shipName;
    @FindBy(id = "ship_phone")
    private WebElement shipPhone;
    @FindBy(id = "ship_email")
    private WebElement shipEmail;
    @FindBy(id = "ship_address")
    private WebElement shipAddress;
    @FindBy(xpath = "//button[@class=\"btn btn-primary btn-xl btn-pay k-flex-1\"]")
    private WebElement btnContinuePay;

    private BrowserMobProxyServer proxyServer;
    private PaymentQRPage paymentQR;

    public BuyPlanPage(WebDriver driver, BrowserMobProxyServer proxyServer, PaymentQRPage paymentQR) {
        super(driver);
        this.proxyServer = proxyServer;
        this.paymentQR = paymentQR;
    }

    public void startBuyingPlan() throws InterruptedException, IOException {
        Thread.sleep(3000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(btnBuyPlan));
        wait.until(ExpectedConditions.elementToBeClickable(btnBuyPlan));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnBuyPlan);
        btnBuyPlan.click();
        buyPlan();
    }

    public void buyPlan() throws IOException, InterruptedException {
        proxyServer.newHar("topup-har");
        Thread.sleep(3000);
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(btnContinuePay));
        btnContinuePay.click();

        // Chờ cho đến khi nút sẵn sàng lại
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(btnContinuePay));
        btnContinuePay.click();

        Thread.sleep(5000);


        paymentQR.actionPay();
    }
}
