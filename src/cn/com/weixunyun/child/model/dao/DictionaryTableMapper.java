package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.com.weixunyun.child.model.pojo.DictionaryTable;

public interface DictionaryTableMapper {
	
	@Select("select * from dictionary_table")
	public List<DictionaryTable> getDicTables();
	
	@Select("select count(*) from dictionary_table")
	public int getDicTablesCount();
	
	@Select("select * from dictionary_table where code = #{code}")
	public DictionaryTable get(@Param(value = "code") String code);
	
}
