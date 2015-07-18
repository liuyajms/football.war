package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.StudentClasses;
import cn.com.weixunyun.child.model.pojo.Student;

public interface StudentMapper {
	
	@Select("select * from student where id=#{id}")
	public Student selectGet(Long id);

	@Select("select s.*, c.name as classesName, c.teacher_id as classesTeacherId from student s join classes c on c.id = s.classes_id where c.school_id = #{schoolId} and s.card = #{card} limit 1 ")
	public ClassesStudent getCardStudent(@Param(value = "schoolId") Long schoolId, @Param(value = "card") String card);

	@Select("select a.*, b.name as classesName, b.year as classesYear from student a"
			+ " left join classes b on a.classes_id = b.id where a.id = #{id} ")
	public ClassesStudent select(Long id);

	@SelectProvider(type = StudentMapperProvider.class, method = "getListCount")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId, @Param(value = "keyword") String keyword);

	@SelectProvider(type = StudentMapperProvider.class, method = "getList")
	public List<StudentClasses> selectAll(@Param(value = "schoolId") Long schoolId,
			@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "keyword") String keyword);
	
	@SelectProvider(type = StudentMapperProvider.class, method = "getExportList")
	public List<StudentClasses> selectExportList(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId,
			@Param(value = "keyword") String keyword);

	@Insert("insert into student(id, school_id, classes_id, name, gender, birthday, code, address, description, create_time, card, card_available) "
			+ "values (#{id}, #{schoolId}, #{classesId}, #{name}, #{gender},#{birthday}, #{code}, #{address}, #{description}, now(), #{card}, #{cardAvailable})")
	public void insert(Student student);

	@Update("update student set name=#{name}, gender=#{gender}, birthday=#{birthday},code=#{code},address=#{address}, description=#{description}, update_time=now(), card=#{card} where id=#{id}")
	public void update(Student student);

	@Update("update student set update_time = now() where id = #{id}")
	public void updated(@Param(value = "id") Long id);

	@Update("update student set card_available = #{cardAvailable} where id = #{id}")
	public void updatedCardAvailable(@Param(value = "id") Long id, @Param(value = "cardAvailable") Boolean cardAvailable);

	@Delete("delete from student where id=#{id}")
	public void delete(Long id);
	
	@Delete("delete from student where classes_id=#{classesId}")
	public void deleteClassesStudent(@Param(value = "classesId") Long classesId);

	@Select("select count(*) from classes a, student b where a.id = b.classes_id")
	public int selectAllStudentCount(@Param(value = "clsId") Long clsId, @Param(value = "schoolId") Long schoolId);

	@Select("select b.name studentName from classes a, student b where a.id = b.classes_id order by a.id desc limit #{rows} offset #{offset}")
	public List<Student> selectAllStudent(@Param(value = "offset") int offset, @Param(value = "rows") int rows,
			@Param(value = "clsId") Long clsId, @Param(value = "schoolId") Long schoolId);

	@Select("select count(*) from classes a left join student b on a.id = b.classes_id where a.teacher_id = #{teacherId} and a.school_id = #{schoolId}")
	public int selectStudentCount(@Param(value = "teacherId") Long teacherId, @Param(value = "schoolId") Long schoolId);

	@Select("select b.name, b.id from student b left join classes a on a.id = b.classes_id where a.teacher_id = #{teacherId} and a.school_id = #{schoolId} order by id desc ")
	public List<Student> selectStudent(@Param(value = "teacherId") Long teacherId,
			@Param(value = "schoolId") Long schoolId);

	@SelectProvider(type = StudentMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId,
			@Param(value = "keyword") String keyword);

	@SelectProvider(type = StudentMapperProvider.class, method = "getList")
	public List<ClassesStudent> getList(@Param(value = "schoolId") Long schoolId, @Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "classesId") Long classesId,
			@Param(value = "keyword") String keyword);
}
