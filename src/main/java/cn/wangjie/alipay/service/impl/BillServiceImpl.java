package cn.wangjie.alipay.service.impl;

import cn.wangjie.alipay.dao.BillDao;
import cn.wangjie.alipay.entity.Bill;
import cn.wangjie.alipay.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: alipay
 * @description:
 * @author: WangJie
 * @create: 2018-09-27 15:54
 **/
@Service
public class BillServiceImpl implements BillService {
    private final BillDao billDao;

    @Autowired
    public BillServiceImpl(BillDao billDao) {
        this.billDao = billDao;
    }
    @Override
    public boolean isBillExit(Bill bill){

       int result =  billDao.selectCount(bill);
       return result>0;
    }

    @Override
    public int insertBill(Bill bill) {

        return billDao.insertSelective(bill);
    }
}
