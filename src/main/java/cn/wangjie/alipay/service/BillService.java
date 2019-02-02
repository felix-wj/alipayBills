package cn.wangjie.alipay.service;

import cn.wangjie.alipay.entity.Bill;

public interface BillService {
    boolean isBillExit(Bill bill);

    int insertBill(Bill bill);
}
