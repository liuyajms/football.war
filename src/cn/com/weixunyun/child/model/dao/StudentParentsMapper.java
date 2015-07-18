package cn.com.weixunyun.child.model.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.pojo.StudentParents;

public interface StudentParentsMapper {

	@Select("select * from student_parents where student_id = #{studentId} and parents_id = #{parentsId} limit 1")
	public StudentParents select(@Param(value = "studentId") Long studentId, @Param(value = "parentsId") Long parentsId);

	@Insert("insert into student_parents (student_id, parents_id, type, username, school_id) values (#{studentId}, #{parentsId}, #{type}, #{username}, #{schoolId})")
	public void insert(StudentParents parents);

	@Update("update student_parents set type=#{type} , username=#{username} where student_id=#{studentId} and parents_id=#{parentsId}")
	public void update(StudentParents parents);

	@Update("update student_parents set username = #{username} where parents_id = #{parentsId} ")
	public void updateUsername(@Param(value = "parentsId") Long parentsId, @Param(value = "username") String username);

	@Delete("delete from student_parents where student_id = #{studentId} and parents_id = #{parentsId} ")
	public void delete(@Param(value = "studentId") Long studentId, @Param(value = "parentsId") Long parentsId);

}
