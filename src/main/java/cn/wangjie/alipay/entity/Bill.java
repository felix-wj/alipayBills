package cn.wangjie.alipay.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @program: alipay
 * @description: 账单数据
 * @author: WangJie
 * @create: 2018-09-27 14:25
 **/
@Data
@Table(name ="bill" )
public class Bill {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    /** 时间 */
    private String time;
    /** 备注 */
    private String memo;
    /** 名称 */
    private String name;
    /** 单号 */
    private String tradeNo;
    /** 对方 */
    private String other;
    /** 金额 */
    private String amount;
    /** 状态 */
    private String status;



}
