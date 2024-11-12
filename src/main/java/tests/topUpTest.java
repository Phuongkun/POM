package tests;

import net.lightbody.bmp.BrowserMobProxyServer;
import page.paymentQRPage;
import page.topUpPage;
import org.testng.annotations.Test;


import java.io.IOException;

public class topUpTest extends loginPageTest {
    private BrowserMobProxyServer proxyServer;
    @Test
    public void topup() throws IOException, InterruptedException {
        loginUser();
        proxyServer = new BrowserMobProxyServer();
        proxyServer.start();
        paymentQRPage paymentQr = new paymentQRPage(getDriver(), this.proxy);
        topUpPage topupPage = new topUpPage(getDriver(),proxyServer, paymentQr);
        topupPage.actionTopup("5000");
    }
}
