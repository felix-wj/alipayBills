package cn.wangjie.alipay.dao;

import cn.wangjie.alipay.entity.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BillDao extends TkMapper<Bill> {
}
