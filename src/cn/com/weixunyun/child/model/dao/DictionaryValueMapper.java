package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;

import cn.com.weixunyun.child.model.pojo.DictionaryValue;

@CacheNamespace
public interface DictionaryValueMapper {
	@SelectProvider(type = DictionaryValueMapperProvider.class, method = "queryList")
	public List<DictionaryValue> getDicValues(@Param(value = "code") String code,@Param(value = "offset") int offset, @Param(value = "rows") int rows);
	
	@Select("select count(*) from dictionary_value where dictionary_field_code = #{code} ")
	public int selectAllCount( @Param(value = "code") String code);
	
	@Select("select * from dictionary_value")
	public List<DictionaryValue> getList();
	
	@Insert("insert into dictionary_value (dictionary_table_code,dictionary_field_code,code, name,ord) "
			+ "values (#{dictionaryTableCode},#{dictionaryFieldCode},#{code}, #{name},#{ord})")
    @Options(flushCache = true)
	public void insert(DictionaryValue dictionaryValue);

	@Update("update dictionary_value set name=#{name},ord=#{ord} where code=#{code} and dictionary_table_code=#{dictionaryTableCode} and dictionary_field_code=#{dictionaryFieldCode}")
    @Options(flushCache = true)
	public void update(DictionaryValue dictionaryValue);

	@Delete("delete from dictionary_value where code = #{code} and dictionary_table_code = #{dictionaryTableCode} and dictionary_field_code=#{dictionaryFieldCode} and school_id=#{schoolId}")
    @Options(flushCache = true)
	public void delete(DictionaryValue d);
	
	@SelectProvider(type = DictionaryValueMapperProvider.class, method = "getValueList")
	public List<DictionaryValue> getValueList(@Param(value = "tableCode") String tableCode, @Param(value = "fieldCode") String fieldCode);
	
	@SelectProvider(type = DictionaryValueMapperProvider.class, method = "getValueListCount")
	//@Select("select count(*) from dictionary_value where school_id = #{schoolId} and dictionary_table_code = #{tableCode} and dictionary_field_code = #{fieldCode} ")
	public int getValueListCount(@Param(value = "tableCode") String tableCode, @Param(value = "fieldCode") String fieldCode);
	
	@Select("select * from dictionary_value where dictionary_table_code = #{tableCode} and dictionary_field_code = #{fieldCode} and code = #{code} limit 1 ")
	public DictionaryValue get(@Param(value = "tableCode") String tableCode, @Param(value = "fieldCode") String fieldCode, @Param(value = "code") String code);
}
