package cn.wangjie.alipay.domain;

import cn.wangjie.alipay.property.AliMonitorProperties;
import cn.wangjie.alipay.utils.FastDFSClient;
import cn.wangjie.alipay.utils.SendEmailUtil;
import cn.wangjie.alipay.entity.Bill;
import cn.wangjie.alipay.entity.User;
import cn.wangjie.alipay.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 登录并抓取支付宝用户交易记录.
 *
 * @author lin
 */
@Slf4j
@Component
public class Domain {

    @Autowired
    private AliMonitorProperties aliMonitorProperties;
    @Autowired
    private BillService billService;

    private static final String LOGIN_URL = "https://auth.alipay.com/login/index.htm";

    private static final String SUCCESS_URL_MEMBER = "https://my.alipay.com/portal/i.htm";

    private static final String SUCCESS_URL_COMPANY = "https://mrchportalweb.alipay.com/user/ihome.htm";


    private void sleep(long sec) {

        try {
            Thread.sleep(sec);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 登录支付宝.
     */
    private WebDriver login(WebDriver webDriver,User user) {


        webDriver.findElement(By.xpath("//ul[@id='J-loginMethod-tabs']/li[@data-status='show_login']")).click();
        //是否需要输入验证码
        By by = By.className("ui-form-item fn-hide");
        if (ElementExist(webDriver, by)) {
            return webDriver;
        }
        WebElement username = webDriver.findElement(By.id("J-input-user"));
        username.clear();
        for (char c : user.getAccount().toCharArray()
                ) {
            username.sendKeys(String.valueOf(c));
            sleep(100);
        }

        WebElement password = webDriver.findElement(By.id("password_rsainput"));
        password.click();
        for (char c : user.getPassword().toCharArray()
                ) {
            password.sendKeys(String.valueOf(c));
            sleep(100);
        }

        sleep(100L);

        webDriver.findElement(By.id("J-login-btn")).click();
        //等待登录跳转
        sleep(2000);
        return webDriver;

    }

    private WebDriver goToBillPage(WebDriver webDriver) {

        webDriver.get("https://consumeprod.alipay.com/record/advanced.htm");

        return webDriver;


    }

    private void dealWithCheckSecurity(WebDriver webDriver,User user) {
        String url = screenAndUploadImage(webDriver,user);
        String content = "<h1>请使用手机支付宝扫描图中二维码</h1><img src='" + url + "'>";
        SendEmailUtil.sendImageEmail(user.getEmail(), "安全验证", content);
    }

    private WebDriver saveBillsFromTable(WebDriver webDriver,User user) {

        //首先得到所有tr的集合
        List<WebElement> rows = webDriver.findElement(By.id("tradeRecordsIndex")).findElements(By.xpath("//tbody/tr"));

        List<Bill> bills = getBillsFromTable(rows);
        By nextPageLink = By.xpath("//div[@class='page-link']/a[@seed='pageLink-pageTriggerT1']");
        while (true) {
            bills = bills.stream().filter(bill -> "交易成功".equals(bill.getStatus())).collect(Collectors.toList());
            boolean exist = false;
            for(Bill b: bills){
                exist = billService.isBillExit(b);
                if (!exist){
                    billService.insertBill(b);
                }else {
                    break;
                }
            }
            if (exist){
                break;
            }

            if (ElementExist(webDriver, nextPageLink)) {
                WebElement nextPage = webDriver.findElement(nextPageLink);
                nextPage.click();
                if (webDriver.getCurrentUrl().contains("checkSecurity.htm")) {
                    dealWithCheckSecurity(webDriver,user);
                    return webDriver;
                }
                rows = webDriver.findElement(By.id("tradeRecordsIndex")).findElements(By.xpath("//tbody/tr"));
                bills = getBillsFromTable(rows);
                sleep(3000);
                System.out.println("-----");
                nextPageLink = By.xpath("//div[@class='page-link']/a[@seed='pageLink-pageTriggerT2']");
            } else {
                break;
            }
        }
        return webDriver;


    }
    private List<Bill> getBillsFromTable(List<WebElement> rows) {
        List<Bill> bills = new ArrayList<>();
        //打印出所有单元格的数据
        for (WebElement row : rows) {
            //得到当前tr里td的集合
            List<WebElement> cols = row.findElements(By.tagName("td"));
            Bill bill = new Bill();
            for (WebElement col : cols) {
                switch (col.getAttribute("class")) {
                    case "time": {
                        bill.setTime(col.getText().replace("\n", " "));
                        break;
                    }
                    case "memo": {
                        bill.setMemo(col.getText());
                        break;
                    }
                    case "name": {
                        bill.setName(col.getText());
                        break;
                    }
                    case "tradeNo ft-gray": {
                        bill.setTradeNo(col.getText());
                        break;
                    }
                    case "other": {
                        bill.setOther(col.getText());
                        break;
                    }
                    case "amount": {
                        bill.setAmount(col.getText());
                        break;
                    }
                    case "status": {
                        bill.setStatus(col.getText());
                        break;
                    }
                    default:
                        break;
                }
            }
            log.info(bill.toString());
            bills.add(bill);
        }
        return bills;
    }

    private boolean ElementExist(WebDriver webDriver, By locator) {
        try {
            webDriver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 屏幕截图
     */
    private String screenAndUploadImage(WebDriver webDriver,User user) {
        File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        byte[] bytes = new byte[1024];


        try (BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(user.getAccount()+"_screen.jpg"))) {


            int len;
            while ((len = br.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.flush();

        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            return null;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
        try {
            String url =FastDFSClient.uploadFile(file);
            if (url == null) {
                throw new Exception("文件上传错误");
            }
            return FastDFSClient.getResAccessUrl(url);
        } catch (Exception e) {
            log.error("文件上传异常！");

        }
        return null;
    }

    public void begin(User user) {

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        WebDriver webDriver = new ChromeDriver();
        webDriver.get(LOGIN_URL);
        login(webDriver,user);
        while (!(webDriver.getCurrentUrl().contains(SUCCESS_URL_MEMBER) || webDriver.getCurrentUrl().contains(SUCCESS_URL_COMPANY))) {
            sleep(3000);
            webDriver.quit();

            webDriver = new ChromeDriver();
            webDriver.get(LOGIN_URL);
            login(webDriver,user);
        }

        while (true) {
            goToBillPage(webDriver);
            if (webDriver.getCurrentUrl().contains("checkSecurity.htm")) {
                dealWithCheckSecurity(webDriver,user);
                sleep(5000);
                continue;
            }
            saveBillsFromTable(webDriver,user);
            for (String url : aliMonitorProperties.getUrls()
                    ) {
                webDriver.get(url);
                sleep(2000);
            }
        }

    }

}
