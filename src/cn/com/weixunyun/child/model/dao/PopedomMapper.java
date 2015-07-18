package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.pojo.Popedom;
import cn.com.weixunyun.child.module.cook.Cook;

public interface PopedomMapper {

	@Select("select * from popedom order by idx asc ")
	public List<Popedom> select();

}
