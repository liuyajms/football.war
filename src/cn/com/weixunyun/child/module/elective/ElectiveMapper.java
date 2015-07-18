package cn.com.weixunyun.child.module.elective;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.module.course.score.CourseScoreMulti;

public interface ElectiveMapper {

	@SelectProvider(type = ElectiveMapperProvider.class, method = "selectCount")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term);
	
	@SelectProvider(type = ElectiveMapperProvider.class, method = "select")
	public List<TeacherElective> selectAll(@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term,
			 @Param(value = "offset") Long offset, @Param(value = "rows") Long rows);

	@SelectProvider(type = ElectiveMapperProvider.class, method = "selectStudent")
	public List<TeacherElective> getStudentList(@Param(value = "schoolId") Long schoolId,
			@Param(value = "studentId") Long studentId, @Param(value = "grade") Integer grade,
			@Param(value = "term") String term, @Param(value = "offset") Long offset, @Param(value = "rows") Long rows);
	
	@Insert("insert into elective (id, school_id, name, description, term, teacher_id, date, num, time_begin, time_end, grade, create_teacher_id, create_time) " +
			" values (#{id}, #{schoolId}, #{name}, #{description}, #{term}, #{teacherId}, #{date}, #{num}, #{timeBegin}, #{timeEnd}, #{grade}, #{createTeacherId}, now())")
	public void insert(Elective course);
	
	@Insert("insert into elective (id, school_id, name, description, term, teacher_id, date, num, time_begin, time_end, grade, create_teacher_id, create_time) " +
			"values (#{id}, #{schoolId}, #{name}, #{description}, #{term}, (select id from teacher where name=#{teacherName} and school_id=#{schoolId}  limit 1), #{date}, #{num}, #{timeBegin}, #{timeEnd}, #{grade}, #{createTeacherId}, now())")
    void insertMulti(TeacherElective s);

	@Update("update elective set name=#{name}, description=#{description}, teacher_id=#{teacherId}, date=#{date}, num=#{num}, time_begin=#{timeBegin}, time_end=#{timeEnd}," +
			" grade=#{grade}, update_teacher_id=#{updateTeacherId}, update_time=now() where id=#{id}")
	public void update(Elective course);

	@Delete("delete from elective where id=#{id}")
	public void delete(Long id);
	
	@Delete("delete from elective where school_id=#{schoolId} and term=#{term}")
	public void deleteElectives(@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term);

	@Select("select * from elective where id=#{id}")
	public Elective select(Long id);
}
