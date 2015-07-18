package cn.com.weixunyun.child.module.homework.check;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface HomeworkCheckMapper {

	@Select("select * from homework_check where id = #{id}")
	public HomeworkCheck get(Long id);

	@SelectProvider(type = HomeworkCheckMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "keyword") String keyword,
			@Param(value = "term") String term, @Param(value = "homeworkId") Long homeworkId);

	@SelectProvider(type = HomeworkCheckMapperProvider.class, method = "getList")
	public List<HomeworkCheck> getList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "keyword") String keyword, @Param(value = "schoolId") Long schoolId,
			@Param(value = "term") String term, @Param(value = "homeworkId") Long homeworkId);
	
	@SelectProvider(type = HomeworkCheckMapperProvider.class, method = "getStudentCheckListCount")
	public int getStudentCheckListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term, 
			@Param(value = "classesId") Long classesId, @Param(value = "homeworkId") Long homeworkId,
			 @Param(value = "date") String date);
	
	@SelectProvider(type = HomeworkCheckMapperProvider.class, method = "getStudentCheckList")
	public List<StudentHomeworkCheck> getStudentCheckList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term,
			 @Param(value = "classesId") Long classesId, @Param(value = "homeworkId") Long homeworkId,
			 @Param(value = "date") String date);
	
	@SelectProvider(type = HomeworkCheckMapperProvider.class, method = "getClassesCheckList")
	public List<StudentHomeworkCheck> getClassesCheckList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term,
			 @Param(value = "classesId") Long classesId, 
			 @Param(value = "dateBegin") String dateBegin, @Param(value = "dateEnd") String dateEnd);
	
	@SelectProvider(type = HomeworkCheckMapperProvider.class, method = "getClassesCheckListCount")
	public int getClassesCheckListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term,
			 @Param(value = "classesId") Long classesId, 
			 @Param(value = "dateBegin") String dateBegin, @Param(value = "dateEnd") String dateEnd);

	@Insert("insert into homework_check (id, school_id, term, classes_id, homework_id, student_id, checked, create_user_id, create_time, update_user_id, update_time, description, course_id) " +
			"values (#{id}, #{schoolId}, #{term}, #{classesId}, #{homeworkId}, #{studentId}, #{checked}, #{createUserId}, #{createTime}, #{updateUserId}, #{updateTime}, #{description}, #{courseId})")
	public void insert(HomeworkCheck check);
	
	
	@Update("update homework_check " +
			"set term=#{term}, classes_id=#{classesId}, "
			+ " checked=#{checked}, create_user_id=#{createUserId}, create_time=#{createTime},"
			+ " update_user_id=#{updateUserId}, update_time=#{updateTime}, description=#{description}"
			+ " where homework_id=#{homeworkId} and student_id=#{studentId} and course_id=#{courseId}")
	public int update(HomeworkCheck check);

	@Delete("delete from homework_check where id = #{id}")
	public void delete(Long id);
	
	@Delete("delete from homework_check where student_id = #{studentId} and to_char(create_time, 'yyyyMMdd') = #{date}")
	public void deleteHomeworkChecks(@Param(value = "studentId") Long studentId, @Param(value = "date") String date);
	
}
