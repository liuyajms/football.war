package cn.com.weixunyun.child.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import cn.com.weixunyun.child.model.pojo.ClassesTeacher;

public interface ClassesTeacherMapper {
	
	@Insert("insert into classes_teacher (classes_id, course_id, teacher_id, school_id) values (#{classesId}, #{courseId}, #{teacherId}, #{schoolId})")
	public void insert(ClassesTeacher classes);
	
	@Select("select count(teacher_id) as count from classes_teacher where classes_id = #{classesId} and teacher_id = #{teacherId}")
	public int selectTeacherCount(@Param(value = "classesId") Long classesId, @Param(value = "teacherId") Long teacherId);
	
	@SelectProvider(type = ClassesTeacherMapperProvider.class, method = "selectCount")
	public int selectAllCount(@Param(value = "classesId") Long classesId, @Param(value = "keyword") String keyword,
			@Param(value = "schoolId") Long schoolId);
	
	@SelectProvider(type = ClassesTeacherMapperProvider.class, method = "select")
	public List<ClassesTeacher> selectAll(@Param(value = "classesId") Long classesId, 
			@Param(value = "offset") Long offset, @Param(value = "rows") Long rows, 
			@Param(value = "keyword") String keyword, @Param(value = "schoolId") Long schoolId);
	
	@SelectProvider(type = ClassesTeacherMapperProvider.class, method = "selectTeacherClassesCount")
	public int selectTeacherClassesCount(@Param(value = "classesId") Long classesId, @Param(value = "teacherId") Long teacherId,
			@Param(value = "schoolId") Long schoolId);
	
	@SelectProvider(type = ClassesTeacherMapperProvider.class, method = "selectTeacherClasses")
	public List<ClassesTeacher> selectTeacherClasses(@Param(value = "classesId") Long classesId, 
			@Param(value = "offset") Long offset, @Param(value = "rows") Long rows, 
			@Param(value = "teacherId") Long teacherId, @Param(value = "schoolId") Long schoolId);
	
	@Delete("delete from classes_teacher where classes_id=${classesId} and course_id=${courseId}")
	public void delete(@Param(value = "classesId") Long classesId, @Param(value = "courseId") Long courseId);
	
	@Delete("delete from classes_teacher where classes_id=${classesId} and school_id=${schoolId}")
	public void deleteMulti(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId);

	@Select("select * from classes_teacher where classes_id=${classesId} and course_id=${courseId}")
	public ClassesTeacher select(@Param(value = "classesId") Long classesId, @Param(value = "courseId") Long courseId);
	
	@Select("select tc.id, tc.name					"
			+ "	from course tc 						"
			+ "	where tc.id not in					"
			+ "	(									"
			+ "		select course_id 				"
			+ "		from classes_teacher			" 
			+ "		where classes_id = #{classesId}	"
			+ "	)									"
			+ "	and tc.school_id = #{schoolId}")
	public List<Map<String, ?>> selectResidue(@Param(value = "schoolId") Long schoolId, 
			@Param(value = "classesId") Long classesId);
}
