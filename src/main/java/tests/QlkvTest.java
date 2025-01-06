package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import page.QlkvPage;

public class QlkvTest extends QlkvPage{
        public QlkvTest(WebDriver driver) {
                super(driver);
        }
    QlkvPage qlkvPage= new QlkvPage(driver);
    @Test
  public void loginQlkv(){
            driver.get("https://qlkv.kvpos.com/login?"); // Thay bằng URL thật
            loginToQLKV("admin@kiotviet.com", "123456", "retaildev03");

    }


}
