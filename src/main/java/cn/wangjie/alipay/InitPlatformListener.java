
package cn.wangjie.alipay;

import cn.wangjie.alipay.domain.Domain;
import cn.wangjie.alipay.entity.User;
import cn.wangjie.alipay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Component
public class InitPlatformListener implements ApplicationRunner {

    @Autowired
    private Domain domain;
    @Autowired
    private UserService userService;



    @Override
    public void run(ApplicationArguments args) {

        List<User> users = userService.getAllUser();
        CompletableFuture[] futures = users.stream().map(user -> CompletableFuture.runAsync(()->domain.begin(user))).toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();


    }

}
