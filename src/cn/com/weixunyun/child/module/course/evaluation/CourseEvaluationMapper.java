package cn.com.weixunyun.child.module.course.evaluation;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.module.course.score.CourseScoreMulti;

public interface CourseEvaluationMapper {
	
	@SelectProvider(type = CourseEvaluationMapperProvider.class, method="selectCount")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);
	
	@SelectProvider(type = CourseEvaluationMapperProvider.class, method="select")
	public List<ClassesCourseEvaluation> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows, 
			@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);
	
	@SelectProvider(type = CourseEvaluationMapperProvider.class, method="selectTodayTeacherCount")
	public int selectTodayTeacherCount(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);
	
	@SelectProvider(type = CourseEvaluationMapperProvider.class, method="selectTodayTeacher")
	public List<ClassesCourseEvaluation> selectTodayTeacher(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows, 
			@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);
	
	@SelectProvider(type = CourseEvaluationMapperProvider.class, method="selectTodayStudentCount")
	public int selectTodayStudentCount(@Param(value = "schoolId") Long schoolId, @Param(value = "studentId") Long studentId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);
	
	@SelectProvider(type = CourseEvaluationMapperProvider.class, method="selectTodayStudent")
	public List<ClassesCourseEvaluation> selectTodayStudent(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows, 
			@Param(value = "schoolId") Long schoolId, @Param(value = "studentId") Long studentId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);
	
	@Insert("insert into course_evaluation (id, school_id, praise, accuracy, course_id, student_id, create_teacher_id, create_time, description, term) " +
			"values (#{id}, #{schoolId}, #{praise}, #{accuracy}, #{courseId}, #{studentId}, #{createTeacherId}, now(), #{description}, #{term})")
	public void insert(CourseEvaluation course);
	
	@Insert("insert into course_evaluation (id, school_id, praise, accuracy, course_id, student_id, create_teacher_id, create_time, description, term) " +
			"values (#{id}, #{schoolId}, #{praise}, #{accuracy}, #{courseId}, (select id from student where name=#{studentName} limit 1), #{createTeacherId}, now(), #{description}, #{term})")
    void insertMulti(ClassesCourseEvaluation e);
	
	@Update("update course_evaluation set student_id=#{studentId}, praise=#{praise}, accuracy=#{accuracy}, description=#{description}, update_teacher_id=#{updateTeacherId}, update_time=now() where id=#{id}")
	public void update(CourseEvaluation course);
	
	@Delete("delete from course_evaluation where id=#{id}")
	public void delete(Long id);
	
	@Delete("delete from course_evaluation where schoolId=#{schoolId} and classesId=#{classesId} and courseId=#{courseId} and term=#{term}")
	public void deleteCourseEvaluations(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "courseId") Long courseId, @Param(value = "term") String term);
	
	@Select("select * from course_evaluation where id=#{id}")
	public CourseEvaluation select(Long id);
	
}
