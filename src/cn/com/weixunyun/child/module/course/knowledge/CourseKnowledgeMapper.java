package cn.com.weixunyun.child.module.course.knowledge;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;


public interface CourseKnowledgeMapper {
	
	@SelectProvider(type = CourseKnowledgeMapperProvider.class, method="selectCount")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);
	
	@SelectProvider(type = CourseKnowledgeMapperProvider.class, method="select")
	public List<ClassesCourseKnowledge> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows, 
			@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);
	
	@SelectProvider(type = CourseKnowledgeMapperProvider.class, method="selectExport")
	public List<ClassesCourseKnowledge> selectExportList(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);

	@Insert("insert into course_knowledge (id, school_id, name, description, course_id, classes_id, create_teacher_id, create_time, term) " +
			"values (#{id}, #{schoolId}, #{name}, #{description}, #{courseId}, #{classesId}, #{createTeacherId}, now(), #{term})")
	public void insert(CourseKnowledge course);
	
	@Update("update course_knowledge set name=#{name}, description=#{description}, update_teacher_id=#{updateTeacherId}, update_time=now() where id=#{id}")
	public void update(CourseKnowledge course);
	
	@Delete("delete from course_knowledge where id=#{id}")
	public void delete(Long id);
	
	@Delete("delete from course_knowledge where schoolId=#{schoolId} and classesId=#{classesId} and courseId=#{courseId} and term=#{term}")
	public void deleteCourseKnowledges(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);
	
	@Select("select * from course_knowledge where id=#{id}")
	public CourseKnowledge select(Long id);
}
