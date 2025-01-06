package tests;

import core.BaseTest;
import org.testng.annotations.Test;
import page.LoginPage;

public class LoginPageTest extends BaseTest {
    @Test
    public void loginUser(){
       LoginPage loginPage= new LoginPage(getDriver());
        loginPage.navigateToLogin("https://retaildev03.kvpos.com/man/#/");
        loginPage.login("admin","kiotviet123456");


    }
}
