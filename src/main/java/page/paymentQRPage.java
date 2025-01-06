package page;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import core.BasePage;
import io.restassured.response.Response;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class PaymentQRPage extends BasePage {
    public static String globalPackageName;
    @FindBy(xpath = "//button[@class='btn btn-pay']")
    private WebElement btnContinue;
    @FindBy(className = "payment-full-logo")
    private static WebElement paymentLogo;
    @FindBy(xpath = "(//div[@class=\"form-field-info-value\"])[4]")
    private WebElement expiryDate;
    @FindBy(xpath = "//button[text()='Hoàn tất']")
    private WebElement btncompleted;
    // Biến tĩnh để lưu giá trị cho toàn bộ ứng dụng
    public static String globalDatePlan;


    private final BrowserMobProxyServer proxyServer;
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


    public PaymentQRPage(WebDriver driver, BrowserMobProxyServer proxyServer) {
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
                    Payment payment = new Payment();
                    // Gọi webhook nếu cần
                    payment.callWebhookAPI(responseBody);
                    // Lấy package_name và package_id
                    globalPackageName = payment.parseKfRequestIdFromResponse(responseBody, "package_name");

                    System.out.println("Extracted package_name: " + globalPackageName);
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
        Thread.sleep(3000);
        // Phương thức lưu tên gói vào biến tĩnh

        // Chờ đợi WebElement xuất hiện
        getWebDriverWait().until(ExpectedConditions.visibilityOf(expiryDate));

        // Kiểm tra xem WebElement có hiển thị không
        if (expiryDate.isDisplayed()) {
            globalDatePlan = expiryDate.getText(); // Lấy giá trị text
            System.out.println("Stored date: " + globalDatePlan);
        } else {
            System.out.println("expiryDate element is not displayed!");
        }

        getWebDriverWait().until(ExpectedConditions.visibilityOf(btncompleted));
        btncompleted.click();
        //Thread.sleep(5000);

    }
    public static String getGlobalPlanName() {
        return globalDatePlan; // Phương thức truy xuất giá trị gói toàn cục
    }


    // Phương thức đã cập nhật để lấy cả kf_request_id và package_name




    public class JsonResponseParser {
        private String packageName;
        public  void main(String[] args) {
            String responseBody = "/* JSON bạn cung cấp ở đây */";

            JsonObject result = parseResponse(responseBody);
            if (result != null) {
                System.out.println("KF Request ID: " + result.get("kf_request_id").getAsString());
                System.out.println("Price: " + result.get("price").getAsInt());
                System.out.println("Package Name: " + result.get("package_name").getAsString());
                System.out.println("Package ID: " + result.get("package_id").getAsInt());
            }
        }

        public  JsonObject parseResponse(String responseBody) {
            try {
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

                JsonObject result = new JsonObject();

                // Lấy kf_request_id và price từ current_qr_image
                if (jsonObject.has("current_qr_image")) {
                    JsonObject currentQrImage = jsonObject.getAsJsonObject("current_qr_image");
                    if (currentQrImage.has("kf_request_id")) {
                        result.addProperty("kf_request_id", currentQrImage.get("kf_request_id").getAsString());
                    }
                    if (currentQrImage.has("price")) {
                        result.addProperty("price", currentQrImage.get("price").getAsInt());
                    }
                }

                // Lấy package_name từ detail
                if (jsonObject.has("detail")) {
                    JsonObject detail = jsonObject.getAsJsonObject("detail");
                    if (detail.has("package_name")) {
                        packageName = detail.get("package_name").getAsString();
                        System.out.println(packageName);
                    }
                }

                // Lấy package_id từ đối tượng chính
                if (jsonObject.has("package_id")) {
                    result.addProperty("package_id", jsonObject.get("package_id").getAsInt());
                }

                return result;

            } catch (Exception e) {
                System.err.println("Error parsing response: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }




    public static class Payment {
        private String kfRequestId; // Biến toàn cục để lưu KF Request ID
        private String priceAmount; // Biến toàn cục để lưu giá trị Amount
        private String package_name;
        // Phương thức gọi Webhook API
        public void callWebhookAPI(String responseBody) throws IOException {
            try {
                // Phân tích và gán giá trị cho kfRequestId và priceAmount
                this.kfRequestId = parseKfRequestIdFromResponse(responseBody, "kf_request_id");
                this.priceAmount = parseKfRequestIdFromResponse(responseBody, "price");
                this.package_name=parseKfRequestIdFromResponse(responseBody,"package_name");
                System.out.println("package_name: "+package_name);
                System.out.println("package_name"+package_name);
                if (kfRequestId == null || priceAmount == null) {
                    throw new IllegalArgumentException("kf_request_id hoặc price không tồn tại trong response.");
                }

                System.out.println("amount value response: " + priceAmount);

                // Tạo đối tượng webhook
                webhook request = new webhook();
                request.setEvent("qr_status");

                // Tạo đối tượng payload
                Payload payload = new Payload();
                payload.setId(1046);
                payload.setMerchant_id("1000");
                payload.setMerchant_code("Cma");
                payload.setTransaction_method("Bank");
                payload.setAmount(Integer.parseInt(priceAmount));

                // Sinh ngẫu nhiên các giá trị cho nội dung và trạng thái
                Random random = new Random();
                String[] contentList = {"Test", "Chuyển khoản", "Nạp tiền", "Gian hàng chuyển khoản"};
                payload.setContent(contentList[random.nextInt(contentList.length)]);

                String[] statusList = {"COMPLETED", "COMPLETED"};
                String status = statusList[random.nextInt(statusList.length)];
                payload.setStatus(status);

                // Sinh ngẫu nhiên thời gian giao dịch
                long currentTime = System.currentTimeMillis();
                long randomTime = currentTime - (random.nextInt(7) * 24 * 60 * 60 * 1000L);
                payload.setTransaction_time(randomTime);

                // Sinh ngẫu nhiên partner_trans_id
                long randomPartnerTransId = (long) (Math.random() * 10000000000000L);
                payload.setPartner_trans_id(String.valueOf(randomPartnerTransId));

                payload.setSub_transaction_id(77);
                int subTransactionAmount = status.equals("INPROCESS")
                        ? (int) (Integer.parseInt(priceAmount) * 0.9)
                        : Integer.parseInt(priceAmount);
                payload.setSub_transaction_amount(subTransactionAmount);
                payload.setUuid(kfRequestId);

                request.setPayload(payload);

                // Gửi yêu cầu đến Webhook API
                String webhookURL = "https://open-kma-stg.kvip.fun/v1/payments/webhook";
                Response response = given()
                        .header("Content-Type", "application/json")
                        .header("X-API-Key", "W5wrbst6ovfBKzLqTnBb9toDTWxGiD9RAzT1aYsf2rmHrh9O")
                        .body(request)
                        .post(webhookURL);

                // Kiểm tra kết quả phản hồi từ Webhook API
                if (response.getStatusCode() == 200) {
                    System.out.println("Webhook successfully called.");
                    System.out.println("Response: " + response.getBody().asString());
                } else {
                    System.err.println("Webhook call failed with status code: " + response.getStatusCode());
                    System.err.println("Error Response: " + response.getBody().asString());
                }

                // In ra JSON payload đã gửi
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequest = objectMapper.writeValueAsString(request);
                System.out.println("JSON Payload Sent: " + jsonRequest);

            } catch (Exception e) {
                System.err.println("Error in callWebhookAPI: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Phương thức parse JSON từ response
        // Phương thức parse JSON từ response
        public String parseKfRequestIdFromResponse(String responseBody, String field) {
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

            // Kiểm tra trong "current_qr_image" nếu có
            if (jsonObject.has("current_qr_image")) {
                JsonObject currentQrImage = jsonObject.getAsJsonObject("current_qr_image");
                if (currentQrImage.has(field)) {
                    return currentQrImage.get(field).getAsString();
                }
            }

            // Kiểm tra trong "detail" nếu có
            if (jsonObject.has("detail")) {
                JsonObject detail = jsonObject.getAsJsonObject("detail");
                if (detail.has(field)) {
                    return detail.get(field).getAsString();
                }
            }

            // Kiểm tra nếu trường là "package_id" và lấy từ đối tượng chính
            if (jsonObject.has("package_id") && field.equals("package_id")) {
                return jsonObject.get("package_id").getAsString();
            }

            // Trả về null nếu không tìm thấy trường
            return null;
        }
}

}
