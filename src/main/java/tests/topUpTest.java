package tests;

import page.topUpPage;
import org.testng.annotations.Test;

import java.io.IOException;

public class topUpTest extends loginPageTest {
    @Test
    public void topup() throws IOException, InterruptedException {
        loginUser();
        topUpPage topupPage = new topUpPage(getDriver(),this.proxy);
        topupPage.actionTopup("5000");
    }
}
