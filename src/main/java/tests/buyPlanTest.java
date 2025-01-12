package tests;


import net.lightbody.bmp.BrowserMobProxyServer;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.BuyPlanPage;
import page.PaymentQRPage;
import page.QlkvPage;

import java.io.IOException;

public class BuyPlanTest extends LoginPageTest {

    private BrowserMobProxyServer proxyServer;

    @Test
    public void buyPlanTest() throws IOException, InterruptedException {
        loginUser();

        // Khởi tạo các đối tượng cần thiết
        proxyServer = new BrowserMobProxyServer();
        proxyServer.start();
        // Khởi tạo QlkvTest đối tượng
        QlkvTest qlkvTest = new QlkvTest(driver);
        PaymentQRPage paymentQr = new PaymentQRPage(getDriver(), this.proxy);

        // Khởi tạo BuyPlanPage với driver, topUpPage và proxyServer
        BuyPlanPage buyPlanPage = new BuyPlanPage(driver,  proxyServer, paymentQr);

        // Gọi phương thức startBuyingPlan trước khi điền thông tin
        buyPlanPage.startBuyingPlan();
        qlkvTest.loginQlkv();

    }
}
