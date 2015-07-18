package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.pojo.Course;

public interface CourseMapper {
	
	@SelectProvider(type = CourseMapperProvider.class, method="selectCount")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId, @Param(value = "keyword") String keyword);
	
	@SelectProvider(type = CourseMapperProvider.class, method="select")
	public List<Course> selectAll(@Param(value = "schoolId") Long schoolId, @Param(value = "offset") Long offset, @Param(value = "rows") Long rows, @Param(value = "keyword") String keyword);

	@Insert("insert into course (id, school_id, name, description) values (#{id}, #{schoolId}, #{name}, #{description})")
	public void insert(Course course);
	
	@Update("update course set name=#{name}, description=#{description} where id=#{id}")
	public void update(Course course);
	
	@Delete("delete from course where id=#{id}")
	public void delete(Long id);
	
	@Delete("delete from course where school_id=#{schoolId}")
	public void deleteCourses(@Param(value = "schoolId") Long schoolId);
	
	@Select("select * from course where id=#{id}")
	public Course select(Long id);
}
