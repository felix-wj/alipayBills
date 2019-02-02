package cn.wangjie.alipay.service.impl;

import cn.wangjie.alipay.dao.UserDao;
import cn.wangjie.alipay.entity.User;
import cn.wangjie.alipay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: alipay
 * @description:
 * @author: WangJie
 * @create: 2018-09-28 11:19
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public List<User> getAllUser() {
        return userDao.selectAll();
    }
}
