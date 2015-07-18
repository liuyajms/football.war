package cn.com.weixunyun.child.module.course.score;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface CourseScoreMapper {

	@Select("select * from Course_Score where id = #{id}")
	public CourseScore select(Long id);

	@SelectProvider(type = CourseScoreMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "courseId") Long courseId,
			@Param(value = "classesId") Long classesId,
			@Param(value = "term") String term,
			@Param(value = "queryDate") String queryDate,
			@Param(value = "keyword") String keyword);

	@SelectProvider(type = CourseScoreMapperProvider.class, method = "getList")
	public List<CourseScore> getList(@Param(value = "offset") int offset,
			@Param(value = "rows") int rows,
			@Param(value = "schoolId") Long schoolId,
			@Param(value = "courseId") Long courseId,
			@Param(value = "classesId") Long classesId,
			@Param(value = "term") String term,
			@Param(value = "queryDate") String queryDate,
			@Param(value = "keyword") String keyword);

	@SelectProvider(type = CourseScoreMapperProvider.class, method = "getCourseList")
	public List<CourseScore> getStudentCourseList(
			@Param(value = "schoolId") Long schoolId,
			@Param(value = "term") String term,
			@Param(value = "studentId") Long studentId,
			@Param(value = "courseId") Long courseId
			);
	
	
	@SelectProvider(type = CourseScoreMapperProvider.class, method = "getCourseList")
	public List<CourseScore> getTeacherCourseList(
			@Param(value = "schoolId") Long schoolId,
			@Param(value = "term") String term,
			@Param(value = "classesId") Long classesId,
			@Param(value = "courseId") Long courseId,
			@Param(value = "teacherId") Long teacherId
			);
	
	
	@Insert("insert into Course_Score (id, score,student_id, type, course_id, create_teacher_id, create_time, update_teacher_id, update_time, school_id, date, term) values (#{id}, #{score}, #{studentId}, #{type}, #{courseId}, #{createTeacherId}, #{createTime},#{updateTeacherId}, #{updateTime}, #{schoolId}, #{date}, #{term})")
	public void insert(CourseScore courseScore);

	@Update("update Course_Score set  student_id=#{studentId},type=#{type}, score=#{score}, update_teacher_id=#{updateTeacherId}, update_time=#{updateTime}, date=#{date} where id=#{id}")
	public void update(CourseScore courseScore);

	@Delete("delete from Course_Score where id=#{id}")
	public void delete(Long id);

    @Insert("insert into Course_Score (id, score,student_id, type, course_id, create_teacher_id, create_time, update_teacher_id, update_time, school_id, date, term) values (#{id}, #{score}, (select id from student where code=#{studentNo} or (name=#{studentName} and #{studentNo}='unkown') limit 1), #{type}, #{courseId}, #{createTeacherId}, #{createTime},#{updateTeacherId}, #{updateTime}, #{schoolId}, #{date}, #{term})")
    void insertMulti(CourseScoreMulti s);

    @Select("select #{term} as term, (select name  from  classes where id=#{classesId}) as classesName , (select name  from course where id=#{courseId}) as courseName")
    CourseScore getCurrentInfo(@Param(value = "term") String term, @Param(value = "classesId") Long classesId, @Param(value = "courseId")  Long courseId);

    @Select("select count(*) from course_score where #{map.type}=type and #{map.date}=date and to_char(create_time,'yyyy-MM-dd') = to_char(current_date,'yyyy-MM-dd')")
    int hasData(@Param(value = "map") Map<String, Object> map);

    @Delete("delete from course_score where to_char(create_time,'yyyy-MM-dd') = to_char(current_date,'yyyy-MM-dd')")
    void deleteCurrentDay();
}
