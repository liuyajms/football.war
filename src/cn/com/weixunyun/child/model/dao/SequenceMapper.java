package cn.com.weixunyun.child.model.dao;

import org.apache.ibatis.annotations.Select;

public interface SequenceMapper {

	@Select("select nextval('sequence')")
	public Long sequence();
}
