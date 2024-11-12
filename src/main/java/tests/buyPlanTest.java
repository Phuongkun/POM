package tests;


import net.lightbody.bmp.BrowserMobProxyServer;
import org.testng.annotations.Test;
import page.buyPlanPage;
import page.paymentQRPage;

import java.io.IOException;

public class buyPlanTest extends loginPageTest {

    private BrowserMobProxyServer proxyServer;

    @Test
    public void buyPlanTest() throws IOException, InterruptedException {
        loginUser();

        // Khởi tạo các đối tượng cần thiết
        proxyServer = new BrowserMobProxyServer();
        proxyServer.start();

        paymentQRPage paymentQr = new paymentQRPage(getDriver(), this.proxy);

        // Khởi tạo BuyPlanPage với driver, topUpPage và proxyServer
        buyPlanPage buyPlanPage = new buyPlanPage(driver,  proxyServer, paymentQr);

        // Gọi phương thức startBuyingPlan trước khi điền thông tin
        buyPlanPage.startBuyingPlan();

        // Điền thông tin hợp đồng
        buyPlanPage.fillContractInformation(
                "Nguyen Van A", "0123456789", "test@example.com", "123456789", "123 Main St",
                "Company XYZ", "123456789", "Tran Van B", "0987654321", "456 Business Rd", "company@example.com"
        );

    }
}
