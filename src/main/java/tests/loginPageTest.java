package tests;

import core.baseTest;
import org.testng.annotations.Test;
import page.loginPage;

import java.io.IOException;

public class loginPageTest extends baseTest {
    @Test
    public void loginUser(){
       loginPage loginPage= new loginPage(getDriver());
        loginPage.navigateToLogin("https://retaildev03.kvpos.com/man/#/login");
        loginPage.login("admin","kiotviet123456");


    }
}