package tests;

import net.lightbody.bmp.BrowserMobProxyServer;
import org.testng.annotations.Test;
import page.PaymentQRPage;
import page.UpgradePlanPage;

import java.io.IOException;

public class UpgradePlanTest extends LoginPageTest{
    private BrowserMobProxyServer proxyServer;
    @Test
    public void UpgradeTest() throws IOException, InterruptedException {
        loginUser();
        // Khởi tạo các đối tượng cần thiết
        proxyServer = new BrowserMobProxyServer();
        proxyServer.start();
        // Khởi tạo QlkvTest đối tượng
        QlkvTest qlkvTest = new QlkvTest(driver);
        PaymentQRPage paymentQr = new PaymentQRPage(getDriver(), this.proxy);
        UpgradePlanPage upgradePlanPage= new UpgradePlanPage(getDriver(), this.proxyServer, paymentQr);
        upgradePlanPage.upgradePlan();
        qlkvTest.loginQlkv();
    }

}
