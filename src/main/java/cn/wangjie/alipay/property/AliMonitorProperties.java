package cn.wangjie.alipay.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: alipay
 * @description:
 * @author: WangJie
 * @create: 2019-02-02 17:18
 **/
@Data
@Component
@ConfigurationProperties("alipay-monitor")
public class AliMonitorProperties {
    private List<String> urls;
}
