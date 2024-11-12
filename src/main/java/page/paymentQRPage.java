package page;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import core.basePage;
import io.restassured.response.Response;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class paymentQRPage extends basePage {
    @FindBy(xpath = "//button[@class='btn btn-pay']")
    private WebElement btnContinue;
    @FindBy(className = "payment-full-logo")
    private static WebElement paymentLogo;
    @FindBy(xpath = "//button[@class=\"btn payment-full-btn-success\"]")
    private WebElement btncompleted;
    private BrowserMobProxyServer proxyServer;
    public static class webhook {
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

    private static class Payload {
        private int id;
        private String merchant_id;
        private String merchant_code;
        private String status;
        private int amount;
        private String uuid;
        private int sub_transaction_id;
        private int sub_transaction_amount;
        private long transaction_time;
        private String transaction_method;
        private String content;
        private String partner_trans_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMerchant_id() {
            return merchant_id;
        }

        public void setMerchant_id(String merchant_id) {
            this.merchant_id = merchant_id;
        }

        public String getMerchant_code() {
            return merchant_code;
        }

        public void setMerchant_code(String merchant_code) {
            this.merchant_code = merchant_code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getSub_transaction_id() {
            return sub_transaction_id;
        }

        public void setSub_transaction_id(int sub_transaction_id) {
            this.sub_transaction_id = sub_transaction_id;
        }

        public int getSub_transaction_amount() {
            return sub_transaction_amount;
        }

        public void setSub_transaction_amount(int sub_transaction_amount) {
            this.sub_transaction_amount = sub_transaction_amount;
        }

        public String getTransaction_method() {
            return transaction_method;
        }

        public void setTransaction_method(String transaction_method) {
            this.transaction_method = transaction_method;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPartner_trans_id() {
            return partner_trans_id;
        }

        public void setPartner_trans_id(String partner_trans_id) {
            this.partner_trans_id = partner_trans_id;
        }

        public long getTransaction_time() {
            return transaction_time;
        }

        public void setTransaction_time(long transaction_time) {
            this.transaction_time = transaction_time;
        }

    }


    public paymentQRPage(WebDriver driver, BrowserMobProxyServer proxyServer) {
        super(driver);
        this.proxyServer = proxyServer;
    }
    public void actionPay() throws IOException, InterruptedException {
        Thread.sleep(5000);
        // Lấy HAR và xử lý
        if (proxyServer != null && proxyServer.isStarted()) {

            Har har = proxyServer.getHar();
            if (har != null) {
                System.out.println("HAR content captured: " + har.getLog().getEntries().size() + " entries.");
                Optional<HarEntry> continuePaymentEntry = har.getLog().getEntries().stream()
                        .filter(entry -> entry.getRequest().getUrl().contains("/continuePayment") && entry.getRequest().getMethod().equals("POST"))
                        .findFirst();

                if (continuePaymentEntry.isPresent()) {
                    String responseBody = continuePaymentEntry.get().getResponse().getContent().getText();

                    // Gọi webhook nếu cần
                    callWebhookAPI(responseBody);
                } else {
                    System.out.println("No /continuePayment request found.");
                }
            } else {
                System.out.println("HAR is null.");
            }
        }

        Thread.sleep(5000);
    // Chờ đến khi biểu tượng thanh toán xuất hiện và hiển thị
        getWebDriverWait().until(ExpectedConditions.visibilityOf(paymentLogo));
        assert paymentLogo.isDisplayed();

        getWebDriverWait().until(ExpectedConditions.visibilityOf(btncompleted));
        btncompleted.click();
        Thread.sleep(5000);
    }
    private String parseKfRequestIdFromResponse(String responseBody, String field) {
        // Parse the responseBody thành JsonObject
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        System.out.println("log json object: " + jsonObject);
        // Kiểm tra nếu có "current_qr_image" và "kf_request_id" trong response
        if (jsonObject.has("current_qr_image")) {
            JsonObject currentQrImage = jsonObject.getAsJsonObject("current_qr_image");

            // Trả về "kf_request_id" nếu tồn tại, ngược lại trả về null
            return currentQrImage.has(field) ? currentQrImage.get(field).getAsString() : null;
        }

        // Trả về null nếu không tìm thấy "current_qr_image"
        return null;
    }
    public void callWebhookAPI(String responseBody) throws IOException, InterruptedException {
        String kfRequestId = parseKfRequestIdFromResponse(responseBody, "kf_request_id");
        String priceAmount = parseKfRequestIdFromResponse(responseBody, "price");

        System.out.println("amount value response " + priceAmount);
        webhook request= new webhook();
        request.setEvent("qr_status");

        Payload payload= new Payload();
        payload.setId(1046);
        payload.setMerchant_id("1000");
        payload.setMerchant_code("Cma");
        payload.setTransaction_method("Bank");
        payload.setAmount(Integer.parseInt(priceAmount));
        String[] statuses = {"INPROCESS", "COMPLETED"};
        Random random = new Random();
        // Tạo danh sách các giá trị có thể cho "content"
        String[] contentList = {"Test", "Chuyển khoản", "Nạp tiền", "Gian hàng chuyển khoản"};
        String content = contentList[random.nextInt(contentList.length)];
        System.out.println("Random Content: " + content);
        payload.setContent(content);

        // Randomly assign status
// Tạo danh sách các giá trị có thể cho "Status"
        String[] statusList = {"COMPLETED", "INPROGRESS"};
        String status = statusList[random.nextInt(statusList.length)];
        System.out.println("Random Status: " + status);
        payload.setStatus(status);
        // Generate random transaction time (within a range)
        long currentTime = System.currentTimeMillis(); // Get current time in milliseconds
        long randomTime = currentTime - (random.nextInt(7) * 24 * 60 * 60 * 1000L); // Random time within the last 7 days
        payload.setTransaction_time(randomTime);
// Tạo giá trị ngẫu nhiên cho partner_trans_id
        long randomPartnerTransId = (long) (Math.random() * 10000000000000L); // Tạo số ngẫu nhiên có 13 chữ số
        System.out.println("Random Partner Trans ID: " + randomPartnerTransId);

        payload.setPartner_trans_id(String.valueOf(randomPartnerTransId));
        payload.setSub_transaction_id(77);
        int subTransactionAmount;
        if (status.equals("INPROGRESS")) {
            // Nếu status là INPROGRESS, sub_transaction_amount nhỏ hơn amount
            subTransactionAmount = (int) (Integer.parseInt(priceAmount) * 0.9); // 90% của amount
        } else {
            // Nếu status là COMPLETED, sub_transaction_amount bằng amount
            subTransactionAmount = Integer.parseInt(priceAmount);
        }
        System.out.println("Sub Transaction Amount: " + subTransactionAmount);
        payload.setSub_transaction_amount(subTransactionAmount);
        payload.setUuid(kfRequestId);
        request.setPayload(payload);

        // Gọi Webhook API bằng RestAssured
        String webhookURL = "https://open-kma-stg.kvip.fun/v1/payments/webhook";
        Response response = given()
                .header("Content-Type", "application/json")
                .header("X-API-Key", "W5wrbst6ovfBKzLqTnBb9toDTWxGiD9RAzT1aYsf2rmHrh9O")
                .body(request) // Tự động chuyển đổi payload sang JSON
                .post(webhookURL);

        // Kiểm tra kết quả từ webhook API
        if (response.getStatusCode() == 200) {
            System.out.println("Webhook successfully called.");
            System.out.println("Response: " + response.getBody().asString());
        } else {
            System.out.println("Webhook call failed with status code: " + response.getStatusCode());
            System.out.println("Error Response: " + response.getBody().asString());
        }
        // In ra JSON payload đã gửi
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        System.out.println(jsonRequest);

    }
}
