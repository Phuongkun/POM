package page;

import core.BasePage;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.Optional;

public class QlkvPage extends BasePage {


    @FindBy(name = "UserName")
    private WebElement usernameField;
    @FindBy(name = "Password")
    private WebElement passwordField;
    @FindBy(name = "quan-ly")
    private WebElement loginButton;
    @FindBy(xpath = "//input[@placeholder=\"Keyword\"]")
    private WebElement inputRetailer;
    @FindBy(xpath = "//button[@ng-click=\"searchRetailer()\"]")
    private WebElement btnSearch;
    @FindBy(xpath = "//span[@class=\"k-widget k-dropdown k-header ng-scope\"]//span[@class=\"k-input ng-scope\"]")
    private static WebElement planInfo;
    @FindBy(xpath = "//button[@ng-click=\"create()\"]")
    private WebElement btnUpdate;
    @FindBy(xpath = "(//input[@class='iptPhone k-input' and @data-role='datepicker'])[3]")
    private WebElement expiryDateQlkv;
    private BrowserMobProxyServer proxyServer;
    private PaymentQRPage paymentQR;

    public QlkvPage(WebDriver driver) {
        super(driver);
    }

    // Login to QLKV
    public void loginToQLKV(String usernameTxt, String passwordTxt, String retailerTxt) {
        getWebDriverWait().until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.sendKeys(usernameTxt);
        getWebDriverWait().until(ExpectedConditions.visibilityOf(passwordField));
        passwordField.sendKeys(passwordTxt);
        loginButton.click();
        getWebDriverWait().until(ExpectedConditions.visibilityOf(inputRetailer));
        inputRetailer.sendKeys(retailerTxt);
        btnSearch.click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String packageName = PaymentQRPage.globalPackageName;
        System.out.println("Global Package Name: " + packageName);
        String expiryDate = PaymentQRPage.globalDatePlan;
        System.out.println("Global expiryDate: " + expiryDate);
        // Chuẩn hóa tên gói để so sánh
        String normalizedPackageName = normalizePackageName(packageName);
        Assert.assertTrue(isPlanUpdated(normalizedPackageName), "QLKV plan info not updated!");
        // So sánh giá trị
        String dateQlkv = expiryDateQlkv.getAttribute("value");
        System.out.println("Ngày hết hạn trên QLKV: "+ dateQlkv);
        Assert.assertEquals(dateQlkv, expiryDate, "Ngày hết hạn chưa được update");
    }
    // Verify QLKV plan info
    public static boolean isPlanUpdated(String expectedPlanInfo) {
        return getText(planInfo).contains(expectedPlanInfo);
    }
    // Phương thức chuẩn hóa tên gói
    private String normalizePackageName(String packageName) {
        // Thực hiện thay thế các giá trị tên gói không khớp
        if ("Gói chuyên nghiệp".equalsIgnoreCase(packageName)) {
            return "Gói nâng cao";
        }
        // Nếu không có sự thay đổi thì trả về tên gói nguyên bản
        return packageName;
    }
    // nhiều tên gói với sự khác biệt về cách hiển thị
//    private String normalizePackageName(String packageName) {
//        // Các quy tắc chuẩn hóa tên gói
//        switch (packageName.toLowerCase()) {
//            case "text chuyên nghiệp":
//                return "Gói nâng cao";
//            case "gói cơ bản":
//                return "Gói tiêu chuẩn";
//            // Thêm các tên gói khác nếu cần
//            default:
//                return packageName;
//        }
//    }?

}
