package cn.wangjie.alipay.dao;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


public interface TkMapper<T> extends Mapper<T>, MySqlMapper<T> {
//特别注意，该接口不能被扫描到，否则会出错
}
