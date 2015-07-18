package cn.com.weixunyun.child.module.homework;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface HomeworkMapper {
	
	@Select("select * from homework where id = #{id}")
	public Homework select(Long id);

//	 @Select("select count(*) from homework t where school_id=#{schoolId} and teacher_id=#{teacherId} and classes_id=#{classesId} and description like '%'||#{keyword}||'%' ")
	@SelectProvider(type = HomeworkMapperProvider.class, method = "getCount")
	public int getCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "classesId") Long classesId,
			@Param(value = "courseId") Long courseId,
			@Param(value = "teacherId") Long teacherId,
			@Param(value = "term") String term,
			@Param(value = "keyword") String keyword,
			@Param(value = "queryDate") String queryDate);// 教师登陆

//	@Select("select count(*) from homework t where school_id=#{schoolId} and classes_id=#{classesId} and description like '%'||#{keyword}||'%' ")
	@SelectProvider(type = HomeworkMapperProvider.class, method = "getOtherCount")
	public int getOtherCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "classesId") Long classesId,
			@Param(value = "courseId") Long courseId,
			@Param(value = "term") String term,
			@Param(value = "keyword") String keyword,
			@Param(value = "queryDate") String queryDate);// 其他登陆

	@SelectProvider(type = HomeworkMapperProvider.class, method = "getOtherList")
	public List<Homework> getOtherList(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId,
			@Param(value = "classesId") Long classesId,
			@Param(value = "courseId") Long courseId,
			@Param(value = "term") String term,
			@Param(value = "keyword") String keyword,
			@Param(value = "queryDate") String queryDate);

	
	@SelectProvider(type = HomeworkMapperProvider.class, method = "getList")
	public List<Homework> getList(
			@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId,
			@Param(value = "classesId") Long classesId,
			@Param(value = "courseId") Long courseId,
			@Param(value = "teacherId") Long teacherId,
			@Param(value = "term") String term,
			@Param(value = "keyword") String keyword,
			@Param(value = "queryDate") String queryDate);
	
	@SelectProvider(type = HomeworkMapperProvider.class, method = "getStudentHomeworkList")
	public List<StudentHomework> getStudentHomeworkList(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "schoolId") Long schoolId, 
			@Param(value = "classesId") Long classesId, @Param(value = "studentId") Long studentId,
			@Param(value = "term") String term, @Param(value = "date") String date);
	
	@SelectProvider(type = HomeworkMapperProvider.class, method = "getStudentHomeworkListCount")
	public int getStudentHomeworkListCount(@Param(value = "schoolId") Long schoolId, 
			@Param(value = "classesId") Long classesId, @Param(value = "studentId") Long studentId,
			@Param(value = "term") String term, @Param(value = "date") String date);
	
	@SelectProvider(type = HomeworkMapperProvider.class, method = "getHomeworkList")
	public List<StudentHomework> getHomeworkList(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "schoolId") Long schoolId, 
			@Param(value = "teacherId") Long teacherId, @Param(value = "term") String term, 
			@Param(value = "date") String date);

	@Insert("insert into homework (id, classes_id, course_id, description, create_teacher_id, create_time, update_teacher_id, update_time, school_id, term) values (#{id}, #{classesId}, #{courseId}, #{description}, #{createTeacherId}, now(),#{updateTeacherId}, now(), #{schoolId}, #{term})")
	public void insert(Homework homework);

	@Update("update homework set  description=#{description}, update_teacher_id=#{updateTeacherId}, update_time=now() where id=#{id}")
	public void update(Homework homework);

	@Delete("delete from homework where id=#{id}")
	public void delete(Long id);


}
