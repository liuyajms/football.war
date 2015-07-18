package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.com.weixunyun.child.model.pojo.Concern;

public interface ConcernMapper {

	@Select("select * from concern where userIdConcern = #{userIdConcern} and userIdConcerned = #{userIdConcerned} order by id desc")
	public List<Concern> selectConcern(@Param(value = "userIdConcern") Long userIdConcern, @Param(value = "userIdConcerned") Long userIdConcerned);

	@Insert("insert into concern (id, userIdConcern, userIdConcerned, school_id) values (#{id}, #{userIdConcern}, #{userIdConcerned}, #{schoolId})")
	public void insert(Concern concern);

	@Delete("delete from concern where id = #{id}")
	public void delete(Long id);
}
