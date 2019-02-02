package cn.wangjie.alipay;

import cn.wangjie.alipay.domain.Domain;
import cn.wangjie.alipay.utils.FastDFSClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest

public class AlipayApplicationTests {




    @Autowired
    private Domain domain;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testLogin() {

       // domain.begin();
    }
    @Test
    public void uploadTest(){
        File file = new File("icon.png");
        String url = FastDFSClient.uploadFile(file);
        System.out.println(FastDFSClient.getResAccessUrl(url));
    }


}
