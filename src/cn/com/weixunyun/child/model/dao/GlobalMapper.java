package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.pojo.Global;

public interface GlobalMapper {

	@Select("select * from global where school_id = #{schoolId} and code_parent = #{codeParent} and code = #{code} limit 1")
	public Global select(@Param(value = "schoolId") Long schoolId, @Param(value = "codeParent") String codeParent,
			@Param(value = "code") String code);

	@Select("select count(*) from global where school_id = #{schoolId} ")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId);

	@Select("select * from global where school_id = #{schoolId} ")
	public List<Global> selectAll(@Param(value = "schoolId") Long schoolId);
	
	@Select("select * from global order by code_parent, code, value desc ")
	public List<Global> getList();

	@Insert("insert into global (school_id, code_parent, code, value) values (#{schoolId}, #{codeParent}, #{code}, #{value})")
	public void insert(Global global);

	@Update("update global set value=#{value} where code_parent=#{codeParent} and code=#{code} and school_id = #{schoolId}")
	public void update(Global global);

	@Delete("delete from global where school_id = #{schoolId} and code_parent = #{codeParent} and code = #{code}")
	public void delete(Global global);

}
