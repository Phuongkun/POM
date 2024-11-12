package page;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import core.basePage;
import io.restassured.response.Response;
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
import java.util.Optional;
import java.util.Random;
import static io.restassured.RestAssured.given;
public class topUpPage extends basePage {
    private paymentQRPage paymentQR;
    @FindBy(xpath = "(//div[@class='menu-profile']/li)[6]")
    private WebElement menuTopup;

    @FindBy(xpath = "//button[@class='btn-topup btn-success']")
    private WebElement btnTopup;

    @FindBy(xpath = "(//button[@class='btn btn-primary'])[2]")
    private WebElement selectTopup;

    @FindBy(xpath = "//div[@class='cash-form-money d-flex']/input")
    private WebElement inputAmount;

    @FindBy(xpath = "//button[@class='btn btn-pay']")
    private WebElement btnContinue;

    @FindBy(className = "payment-full-logo")
    private WebElement paymentLogo;
    @FindBy(xpath = "//button[@class=\"btn payment-full-btn-success\"]")
    private WebElement btncompleted;
    @FindBy(xpath = "//div[@class='cash-info-amount']")
    private WebElement walletBalance;
    private BrowserMobProxyServer proxyServer;
    private WebDriverWait wait;
    public class webhook {
        private String event;
        private Payload payload;

        public String getEvent() {
            return event;
        }
        public void setEvent(String event) {
            this.event = event;
        }
        public Payload getPayload() {
            return payload;
        }
        public void setPayload(Payload payload) {
            this.payload = payload;
        }
    }
    private class Payload {
        private int id;
        private String merchant_id;
        private String merchant_code;
        private String status;
        private int amount;
        private String uuid;
        private int sub_transaction_id;
        private int  sub_transaction_amount;
        private  long transaction_time;
        private String transaction_method;
        private String content;
        private String partner_trans_id;

        public int getId() {
            return id;
        }
        public void setId(int id){
            this.id=id;
        }
        public String getMerchant_id(){
            return merchant_id;
        }
        public void setMerchant_id(String merchant_id){
            this.merchant_id= merchant_id;
        }
        public String getMerchant_code(){
            return merchant_code;
        }
        public void setMerchant_code(String merchant_code){
            this.merchant_code=merchant_code;
        }
        public String getStatus(){
            return status;
        }
        public void setStatus(String status){
            this.status=status;
        }
        public int getAmount(){
            return amount;
        }
        public void setAmount(int amount){
            this.amount=amount;
        }
        public String getUuid(){
            return uuid;
        }
        public void setUuid(String uuid){
            this.uuid=uuid;
        }
        public int getSub_transaction_id(){
            return sub_transaction_id;
        }
        public void setSub_transaction_id(int sub_transaction_id){
            this.sub_transaction_id=sub_transaction_id;
        }
        public int getSub_transaction_amount(){
            return sub_transaction_amount;
        }
        public void setSub_transaction_amount(int sub_transaction_amount){
            this.sub_transaction_amount=sub_transaction_amount;
        }
        public String getTransaction_method(){
            return transaction_method;
        }
        public void setTransaction_method(String transaction_method){
            this.transaction_method=transaction_method;
        }
        public String getContent(){
            return content;
        }
        public void setContent(String content){
            this.content=content;
        }
        public String getPartner_trans_id(){
            return partner_trans_id;
        }
        public void setPartner_trans_id(String partner_trans_id){
            this.partner_trans_id=partner_trans_id;
        }
        public long getTransaction_time(){
            return transaction_time;
        }
        public void setTransaction_time(long transaction_time){
            this.transaction_time=transaction_time;
        }

    }
    public topUpPage(WebDriver driver, BrowserMobProxyServer proxyServer,paymentQRPage paymentQR) {
        super(driver);
        this.proxyServer = proxyServer;
        this.paymentQR = paymentQR;
    }
    // Hàm để lấy số dư hiện tại
    public int getWalletBalance() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String balanceText;
        try {
            WebElement walletBalance = wait.until(ExpectedConditions.visibilityOf(this.walletBalance));
            balanceText = walletBalance.getText();

            if (balanceText.isEmpty()) {
                System.out.println("Không lấy được số dư ví. Vui lòng kiểm tra lại phần tử walletBalance.");
                return 0;  // Trả về giá trị mặc định nếu cần
            }

            balanceText = balanceText.replaceAll("[^0-9]", "");
            return Integer.parseInt(balanceText);

        } catch (Exception e) {
            System.out.println("Lỗi khi lấy số dư ví: " + e.getMessage());
            return 0;
        }
    }
    // Hàm WebDriverWait có thời gian chờ được khởi tạo
    public WebDriverWait getWebDriverWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public void actionTopup(String amountValue) throws IOException, InterruptedException {
        proxyServer.newHar("topup-har");
        // Wait and click on the top-up menu
        Thread.sleep(5000);
        getWebDriverWait().until(ExpectedConditions.visibilityOf(menuTopup));
        menuTopup.click();
        // Lấy số dư hiện tại trước khi nạp tiền
        int initialBalance = getWalletBalance();
        int topupAmount = Integer.parseInt(amountValue);

        getWebDriverWait().until(ExpectedConditions.visibilityOf(btnTopup));
        btnTopup.click();

        getWebDriverWait().until(ExpectedConditions.visibilityOf(selectTopup));
        selectTopup.click();

        inputAmount.sendKeys(amountValue);
        //btnContinue.click();
        Thread.sleep(5000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(btnContinue));
        wait.until(ExpectedConditions.elementToBeClickable(btnContinue));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnContinue);
        //Thread.sleep(5000);
        // Retrieve orderId after the request is sent
        paymentQR.actionPay();
        // Lấy số dư hiện tại sau khi nạp tiền
        int finalBalance = getWalletBalance();

        // Kiểm tra số dư
        if (finalBalance == initialBalance + topupAmount) {
            System.out.println("Nạp tiền thành công. Số dư hiện tại: " + finalBalance);
        } else {
            System.out.println("Nạp tiền thất bại hoặc số dư không khớp. Số dư hiện tại: " + finalBalance);
        }
    }

    }


