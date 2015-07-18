package cn.com.weixunyun.child.module.course.classroom;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;


public interface CourseClassroomMapper {

	@Select("select * from course_classroom where id = #{id}")
	public CourseClassroom select(Long id);

	@Insert("insert into course_classroom (id, name, size, description, create_teacher_id, create_time, school_id, classes_id, type, course_id, term) " +
			"values (#{id}, #{name}, #{size}, #{description}, #{createTeacherId}, now(), #{schoolId}, #{classesId}, #{type}, #{courseId}, #{term})")
	public void insert(CourseClassroom classroom);

	@Delete("delete from course_classroom where id=#{id}")
	public void delete(Long id);
	
	@Delete("delete from course_classroom where schoolId=#{schoolId} and classesId=#{classesId} and courseId=#{courseId} and term=#{term}")
	public void deleteCourseClassrooms(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);

	@SelectProvider(type = CourseClassroomMapperProvider.class, method = "selectCount")
	public int selectCount(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);

	@SelectProvider(type = CourseClassroomMapperProvider.class, method = "select")
	public List<ClassesCourseClassroom> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId,
			@Param(value = "classesId") Long classesId,
			@Param(value = "courseId") Long courseId,
			@Param(value = "term") String term);

}
