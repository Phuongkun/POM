package tests;

import net.lightbody.bmp.BrowserMobProxyServer;
import page.PaymentQRPage;
import page.TopUpPage;
import org.testng.annotations.Test;


import java.io.IOException;

public class TopUpTest extends LoginPageTest {

    @Test
    public void topUp() throws IOException, InterruptedException {
        BrowserMobProxyServer proxyServer;
        loginUser();
        proxyServer = new BrowserMobProxyServer();
        proxyServer.start();
        PaymentQRPage paymentQr = new PaymentQRPage(getDriver(), this.proxy);
        TopUpPage topupPage = new TopUpPage(getDriver(),proxyServer, paymentQr);
        topupPage.actionTopup("5000");
    }
}
