package page;

import core.basePage;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.Random;

public class buyPlanPage extends basePage {
    @FindBy(xpath = "(//button[@class=\"btn box-popup-register\"])[2]")
    private WebElement btnBuyPlan;
    @FindBy(id="name")
    private WebElement txtName;
    @FindBy(xpath = "//input[@value=\"MALE\"]")
    private WebElement radioGender;
    @FindBy(id="phone")
    private WebElement txtPhone;
    @FindBy(id="email")
    private WebElement txtEmail;
    @FindBy(id="identity_code")
    private WebElement txtIdentityCode;
    @FindBy(id="address")
    private WebElement address;
    @FindBy(xpath = "//input[@value=\"COMPANY\"]")
    private WebElement radioCompany;
    @FindBy(id="company_name")
    private WebElement companyName;
    @FindBy(id="company_tax_code")
    private WebElement companyTaxCode;
    @FindBy(id="agent_name")
    private WebElement agentName;
    @FindBy(id="agent_phone")
    private WebElement agentPhone;
    @FindBy(id="company_address")
    private WebElement companyAddress;
    @FindBy(id="company_email")
    private WebElement companyEmail;
    @FindBy(id="isReceivedEInvoice")
    private WebElement isReceivedEInvoice;
    @FindBy(id="ship_name")
    private WebElement shipName;
    @FindBy(id="ship_phone")
    private WebElement shipPhone;
    @FindBy(id="ship_email")
    private WebElement shipEmail;
    @FindBy(id="ship_address")
    private WebElement shipAddress;
    @FindBy(xpath = "//button[@class=\"btn btn-primary btn-pay\"]")
    private WebElement btnContinuePay;

    private BrowserMobProxyServer proxyServer;
    private paymentQRPage paymentQR;
    public buyPlanPage(WebDriver driver,BrowserMobProxyServer proxyServer, paymentQRPage paymentQR) {
        super(driver);
        this.proxyServer = proxyServer;
        this.paymentQR = paymentQR;
    }
    public void startBuyingPlan() throws InterruptedException {
        Thread.sleep(3000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(btnBuyPlan));
        btnBuyPlan.click();
    }

    public void fillContractInformation(String name, String phone, String email, String identityCode, String txtAddress,
                                        String txtCompanyName, String taxCode, String agentName, String agentPhone,
                                        String companyAddress, String companyEmail) throws IOException, InterruptedException {
        boolean isBusiness = new Random().nextBoolean();

        if (isBusiness) {
            enterBusinessContract(txtCompanyName, taxCode, agentName, agentPhone, companyAddress, companyEmail);
        } else {
            enterIndividualContract(name, phone, email, identityCode, txtAddress);
        }
        buyPlan();
    }

    private void enterIndividualContract(String name, String phone, String email, String identityCode, String txtAddress) {
        if (txtName.getAttribute("readonly").equals("false")) {
            txtName.sendKeys(name);
        }
        txtPhone.clear();
        txtPhone.sendKeys(phone);
        txtEmail.clear();
        txtEmail.sendKeys(email);
        txtIdentityCode.clear();
        txtIdentityCode.sendKeys(identityCode);
        address.clear();
        address.sendKeys(txtAddress);
    }

    private void enterBusinessContract(String txtCompanyName, String taxCode, String name, String phone, String address, String email) {
        companyName.clear();
        companyName.sendKeys(txtCompanyName);
        companyTaxCode.clear();
        companyTaxCode.sendKeys(taxCode);
        agentName.clear();
        agentName.sendKeys(name);
        agentPhone.clear();
        agentPhone.sendKeys(phone);
        companyAddress.clear();
        companyAddress.sendKeys(address);
        companyEmail.clear();
        companyEmail.sendKeys(email);
    }

    public void buyPlan() throws IOException, InterruptedException {
        proxyServer.newHar("topup-har");

        Thread.sleep(5000);
        // Lần nhấn đầu tiên
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(btnContinuePay));
        btnContinuePay.click();

        // Chờ cho đến khi nút sẵn sàng lại
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(btnContinuePay));
        btnContinuePay.click();
        Thread.sleep(15000);
        paymentQR.actionPay();
        }
}
