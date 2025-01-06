package page;

import core.BasePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver){

        super(driver);
    }
    @FindBy(name="UserName")
    private WebElement username;
    @FindBy(name="Password")
    private WebElement password;

    @FindBy(xpath = "//input[@name=\"quan-ly\"]")
    private WebElement login;

    @FindBy(xpath = "(//span[text()='admin'])[2]")
    private WebElement admin;

    @FindBy(id = "iframe-kma")
    private WebElement iframe;

    @FindBy(xpath = "(//span[text()='Hồ sơ gian hàng'])[2]")
    public   WebElement mystore;
    @FindBy(xpath = "//*[contains(text(),'Gia hạn ngay')]")
    private WebElement popupExpried;
    @FindBy(xpath = "//div[@class=\"PopupSO\"]")
    private WebElement popupSo;
    @FindBy(xpath = "(//span[@class='vodal-close'])[1]")
    private WebElement popupCloseButton;
    public void navigateToLogin(String url){
        getDriver().get(url);
    }
    // Hàm xử lý popup hiện tại
    public void handlePopupIfExists() {
        try {
            int maxAttempts = 10; // Số lần tối đa để kiểm tra popup
            int attempt = 0; // Số lần đã kiểm tra

            while (attempt < maxAttempts) {
                boolean popupClosed = false;

                // Kiểm tra xem phần tử popupSo có hiển thị không
                try {
                    getWebDriverWait().until(ExpectedConditions.visibilityOf(popupSo)); // Đợi đến khi popupSo hiển thị
                    if (popupSo.isDisplayed()) {
                        System.out.println("PopupSO hiển thị, tiến hành đóng.");
                        closePopup(popupSo, popupCloseButton);
                        popupClosed = true;
                    }
                } catch (Exception e) {
                    System.out.println("Không thể tìm thấy popupSo: " + e.getMessage());
                }

//                // Kiểm tra nếu popupExpried hiển thị
//                try {
//                    getWebDriverWait().until(ExpectedConditions.visibilityOf(popupExpried)); // Đợi đến khi popupExpried hiển thị
//                    if (popupExpried.isDisplayed()) {
//                        System.out.println("PopupExpried hiển thị, tiến hành đóng.");
//                        closePopup(popupExpried, popupCloseButton);
//                        popupClosed = true;
//                    }
//                } catch (Exception e) {
//                    System.out.println("Không thể tìm thấy popupExpried: " + e.getMessage());
//                }

                if (!popupClosed) {
                    System.out.println("Không có popup nào hiển thị, thoát vòng lặp.");
                    break;
                }

                attempt++;
                //Thread.sleep(1000); // Chờ 1 giây trước lần kiểm tra tiếp theo
            }

            System.out.println("Hoàn tất kiểm tra và xử lý popup.");

        } catch (Exception e) {
            System.out.println("Lỗi khi xử lý popup: " + e.getMessage());
        }
    }

    // Hàm đóng popup (sử dụng lại hàm đã có)
    private void closePopup(WebElement popup, WebElement closeButton) {
        try {
            if (popup != null && closeButton != null && popup.isDisplayed()) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", closeButton);
                System.out.println("Đã đóng popup.");
            } else {
                System.out.println("Popup hoặc nút đóng không tồn tại hoặc không hiển thị.");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi đóng popup: " + e.getMessage());
        }
    }
    public void reloadAndHandlePopup(int reloadCount) {
        for (int i = 0; i < reloadCount; i++) {
            try {
                // Reload trang
                System.out.println("Làm mới trang, lần thứ: " + (i + 1));
                driver.navigate().refresh(); // Reload lại trang hiện tại

                // Thêm thời gian chờ ngắn để đảm bảo trang đã tải lại
                //Thread.sleep(1000);

                // Gọi hàm xử lý popup sau khi reload
                handlePopupIfExists();

            } catch (Exception e) {
                System.out.println("Lỗi khi làm mới hoặc xử lý popup: " + e.getMessage());
            }
        }
    }

    public  void login(String usernameTxt, String passwordTxt) {

        getWebDriverWait().until(ExpectedConditions.visibilityOf(username));

        username.sendKeys(usernameTxt);

        getWebDriverWait().until(ExpectedConditions.visibilityOf(password));
        password.sendKeys(passwordTxt);

        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(login));
        login.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Gọi hàm xử lý popup
       // reloadAndHandlePopup(3);

        getWebDriverWait().until(ExpectedConditions.visibilityOf(admin));
        admin.click();

        getWebDriverWait().until(ExpectedConditions.visibilityOf(mystore));
        mystore.click();

// Đóng popup nếu xuất hiện
        // Chuyển sang iframe sau khi nó hiển thị
        getWebDriverWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
    }
    }
