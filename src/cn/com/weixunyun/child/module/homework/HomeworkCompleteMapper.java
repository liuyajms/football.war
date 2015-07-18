package cn.com.weixunyun.child.module.homework;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.bean.ClassesStudent;

public interface HomeworkCompleteMapper {

	@Select("select count(*) from homework")
	public int selectAllCount();

	@Select("select a.id student_id, b.id homework_id, b.course, b.description"
			+ " from student a, homework b, parent c where c.student_id = a.id  and   a.classes_id = b.class_id and   c.student_id=60")
	public List<HomeworkComplete> selectAll();

	@Insert("insert into homework_complete(homework_id, child_id, complete, begin_time, end_time, school_id) values(#{homeworkId}, #{childId}, #{complete}, #{beginTime}, #{endTime}, #{schoolId})")
	public void insert(HomeworkComplete hwc);

	@SelectProvider(type = HomewokCompleteMapperProvider.class, method = "selectCount")
	public int selectAllStudentCount(@Param(value = "teacherId") Long teacherId);

	@SelectProvider(type = HomewokCompleteMapperProvider.class, method = "select")
	public List<ClassesStudent> selectAllStudent(
			@Param(value = "offset") int offset,
			@Param(value = "rows") int rows,
			@Param(value = "teacherId") Long teacherId);

	@SelectProvider(type = HomewokCompleteMapperProvider.class, method = "selectCount")
	public int selectStudentHomeworkCount(
			@Param(value = "teacherId") Long teacherId,
			@Param(value = "studentId") Long studentId);

	@SelectProvider(type = HomewokCompleteMapperProvider.class, method = "select")
	public List<StudentHomeworkComplete> selectStudentHomework(
			@Param(value = "teacherId") Long teacherId,
			@Param(value = "studentId") Long studentId);
	
	@Update("update homework_complete set child_id = #{childId}, complete = #{complete}, begin_time = #{beginTime}, end_time = #{endTime}, comment = #{comment}")
	public void update(HomeworkComplete hwc);
}
