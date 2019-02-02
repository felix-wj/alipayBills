package cn.wangjie.alipay.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @program: alipay
 * @description: 支付宝账号
 * @author: WangJie
 * @create: 2018-09-28 11:15
 **/
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private String account;
    private String password;
    private String email;
}
