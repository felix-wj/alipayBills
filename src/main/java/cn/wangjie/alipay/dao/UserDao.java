package cn.wangjie.alipay.dao;

import cn.wangjie.alipay.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao  extends TkMapper<User> {
}
