package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.pojo.Template;

public interface TemplateMapper {

	@Select("select * from template where code = #{code} and school_id = #{schoolId}")
	public Template select(@Param(value = "code") String code, @Param(value = "schoolId") Long schoolId);
	
	@Select("select count(*) from template where school_id = #{schoolId}")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId);

	@Select("select * from template where school_id = #{schoolId} order by code")
	public List<Template> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows, @Param(value = "schoolId") Long schoolId);

	@Insert("insert into template (code, description, school_id) values (#{code}, #{description}, #{schoolId})")
	public void insert(Template template);

	@Update("update template set description=#{description} where code=#{code}")
	public void update(Template template);


}
