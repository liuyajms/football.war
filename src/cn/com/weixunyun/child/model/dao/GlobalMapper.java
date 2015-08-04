package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;

import cn.com.weixunyun.child.model.pojo.Global;

@CacheNamespace
public interface GlobalMapper {

	@Select("select * from global where code_parent = #{codeParent} and code = #{code} limit 1")
	public Global select(@Param(value = "codeParent") String codeParent,
			@Param(value = "code") String code);

	@Select("select count(*) from global ")
	public int selectAllCount();

	@Select("select * from global ")
	public List<Global> selectAll();
	
	@Select("select * from global order by code_parent, code, value desc ")
	public List<Global> getList();

	@Insert("insert into global (code_parent, code, value) values (#{codeParent}, #{code}, #{value})")
	public void insert(Global global);

	@Update("update global set value=#{value} where code_parent=#{codeParent} and code=#{code}")
	public void update(Global global);

	@Delete("delete from global where code_parent = #{codeParent} and code = #{code}")
	public void delete(Global global);

}
