package tests;

import core.BaseTest;
import org.testng.annotations.Test;
import page.LoginPage;

public class LoginPageTest extends BaseTest {
    @Test
    public void loginUser(){
       loginPage loginPage= new loginPage(getDriver());
        loginPage.navigateToLogin("https:****************");
        loginPage.login("***","*****");


    }
}
