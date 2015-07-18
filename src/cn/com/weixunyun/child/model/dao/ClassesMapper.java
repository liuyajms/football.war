package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.DictionaryTeacher;
import cn.com.weixunyun.child.model.bean.TeacherClasses;
import cn.com.weixunyun.child.model.pojo.Classes;

public interface ClassesMapper {
	@Select("select * from classes where school_id = #{schoolId} order by a.year asc, a.num asc")
	public List<Classes> selectAllClasses(Long schoolId);

	@Select("select * from classes where id = #{id}")
	public Classes select(Long id);

	@Select("select count(*) from classes where school_id=#{schoolId}")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId);

	@Select("select a.id, a.teacher_id teacherId, a.name, a.description, b.name teacherName from classes a left join teacher b on a.teacher_id = b.id where a.school_id=#{schoolId} order by a.year asc, a.num asc")
	public List<Classes> selectAll(@Param(value = "schoolId") Long schoolId);

	@Insert("insert into classes (id, teacher_id, name, year, num, description, school_id, create_teacher_id, create_time) values (#{id}, #{teacherId}, #{name}, #{year}, #{num}, #{description}, #{schoolId}, #{createTeacherId}, now())")
	public void insert(Classes classes);

	@Update("update classes set teacher_id=#{teacherId}, year=#{year}, num=#{num}, name=#{name}, description=#{description}, update_teacher_id=#{updateTeacherId}, update_time=now() where id=#{id}")
	public void update(Classes classes);
	
	@Update("update classes set id_disk = #{idDisk} where id = #{id}")
	public void idDisk(@Param(value = "id") Long id, @Param(value = "idDisk") String idDisk);
	
	@Update("update classes set create_teacher_id = #{createTeacherId}, create_time = now() where id = #{id}")
	public void updateCreateTeacher(@Param(value = "id") Long id, @Param(value = "createTeacherId") Long createTeacherId);

	@Delete("delete from classes where id=#{id}")
	public void delete(Long id);
	
	@Delete("delete from classes where school_id=#{schoolId}")
	public void deleteClasses(@Param(value = "schoolId") Long schoolId);

	@Select("select t.*, c.id as course_id, c.name as course_name from classes_teacher ct join teacher t on t.id = ct.teacher_id join course c on c.id = ct.course_id where ct.classes_id = #{classesId} order by t.id desc ")
	public List<DictionaryTeacher> getTeacherList(@Param(value = "classesId") Long classesId);

	@SelectProvider(type = ClassesMapperProvider.class, method = "selectClassCount")
	public int getStudentListCount(@Param(value = "classesId") Long classesId);

	@SelectProvider(type = ClassesMapperProvider.class, method = "selectClass")
	public List<ClassesStudent> getStudentList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "classesId") Long classesId);

	@SelectProvider(type = ClassesMapperProvider.class, method = "selectCount")
	public int selectAllClassCount(@Param(value = "classesName") String classesName,
			@Param(value = "schoolId") Long schoolId);

	@SelectProvider(type = ClassesMapperProvider.class, method = "select")
	public List<TeacherClasses> selectAllClass(@Param(value = "offset") int offset, @Param(value = "rows") int rows,
			@Param(value = "classesName") String classesName, @Param(value = "schoolId") Long schoolId);

	@Select("select id from classes where school_id = #{schoolId} and year = #{year} ")
	public List<Long> getYearClassesList(@Param(value = "schoolId") Long schoolId, @Param(value = "year") int year);
}
