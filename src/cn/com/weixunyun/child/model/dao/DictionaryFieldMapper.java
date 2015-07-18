package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import cn.com.weixunyun.child.model.bean.TableDictionaryField;
import cn.com.weixunyun.child.model.pojo.DictionaryField;

public interface DictionaryFieldMapper {
	
	@Select("select * from dictionary_field where dictionary_table_code = #{code}")
	public List<DictionaryField> getAllFields(String code);
	
	@Select("select f.*, t.name as dictionary_table_name " +
			   " from dictionary_field f " +
			     " join dictionary_table t " +
			      "  on f.dictionary_table_code = t.code  " +
			  "order by dictionary_table_code, code")
	public List<TableDictionaryField> list();
	
	@Select("select count(*) from dictionary_field " )
	public int listCount();
}
