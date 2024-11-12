package page;

import core.basePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class loginPage extends basePage {

    public loginPage(WebDriver driver){

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
    @FindBy(xpath = "(//span[@class='vodal-close'])[1]")
    private WebElement popupCloseButton;
    public void navigateToLogin(String url){
        getDriver().get(url);
    }
    public  void login(String usernameTxt, String passwordTxt) {

        getWebDriverWait().until(ExpectedConditions.visibilityOf(username));

        username.sendKeys(usernameTxt);

        getWebDriverWait().until(ExpectedConditions.visibilityOf(password));
        password.sendKeys(passwordTxt);

        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(login));
        login.click();

        getWebDriverWait().until(ExpectedConditions.visibilityOf(admin));
        admin.click();


        getWebDriverWait().until(ExpectedConditions.visibilityOf(mystore));
        mystore.click();

// Đóng popup nếu xuất hiện
        try {
            getWebDriverWait().until(ExpectedConditions.visibilityOf(popupExpried));
            getWebDriverWait().until(ExpectedConditions.visibilityOf(popupCloseButton));
            popupCloseButton.click();
        } catch (Exception e) {
            System.out.println("Popup không xuất hiện.");
        }
        // Chuyển sang iframe sau khi nó hiển thị
        getWebDriverWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
    }
    }
