package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.pojo.Popedom;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PopedomMapper {

    @Select("select * from popedom order by idx asc ")
    public List<Popedom> select();

}
