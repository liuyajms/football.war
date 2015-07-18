package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.bean.CourseClasses;
import cn.com.weixunyun.child.model.bean.DictionaryTeacher;
import cn.com.weixunyun.child.model.pojo.Menu;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;

public interface TeacherMapper {

	@Select("select * from teacher where id = #{id}")
	public Teacher select(Long id);
	
	@SelectProvider(type = TeacherMapperProvider.class, method = "selectDictionaryTeacher")
	public DictionaryTeacher selectDictionaryTeacher(@Param(value = "id") Long id);
	
	@SelectProvider(type = TeacherMapperProvider.class, method = "selectCount")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId, @Param(value = "keyword") String keyword);

	@Select("select * from teacher where school_id=#{schoolId} order by id desc")
	public List<Teacher> selectTeacher(@Param(value = "schoolId") Long schoolId);

	@SelectProvider(type = TeacherMapperProvider.class, method = "select")
	public List<DictionaryTeacher> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "keyword") String keyword);

	@SelectProvider(type = TeacherMapperProvider.class, method = "selectAdminCount")
	public int getAdminListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "keyword") String keyword);

	@SelectProvider(type = TeacherMapperProvider.class, method = "selectAdmin")
	public List<DictionaryTeacher> getAdminList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "keyword") String keyword);
	
	@SelectProvider(type = TeacherMapperProvider.class, method = "selectNotAdmin")
	public List<DictionaryTeacher> getNotAdminList(@Param(value = "schoolId") Long schoolId);

	@Select("select t.* from teacher t join classes_teacher ct on ct.teacher_id = t.id where ct.classes_id = #{classesId} and t.mobile = #{mobile} limit 1")
	public Teacher selectMobile(@Param(value = "classesId") Long classesId, @Param(value = "mobile") String mobile);

	@Select("select t.* from teacher t where t.school_id = #{schoolId} and t.username = #{username} and t.password = #{password} limit 1")
	public Teacher getSchoolTeacher(@Param(value = "schoolId") Long schoolId,
			@Param(value = "username") String username, @Param(value = "password") String password);

	@SelectProvider(type = TeacherMapperProvider.class, method = "getClassesList")
	public List<CourseClasses> getClassesList(@Param(value = "schoolId") Long schoolId,
			@Param(value = "teacherId") Long teacherId);

	@Select("select s.* from teacher t join school s on s.id = t.school_id where t.mobile = #{mobile} and t.password = #{password}")
	public List<School> getSchoolList(@Param(value = "mobile") String mobile, @Param(value = "password") String password);

	@Select("select m.* from teacher t join role_menu rm join menu m on m.id = rm.menu_id on rm.role_id = t.role_id where t.id = #{id} order by m.ord asc ")
	public List<Menu> getMenuList(@Param(value = "id") long id);

	@Insert("insert into teacher (id, role_id, name, password, title, remark, description, school_id, type,available,mobile,gender,email, code, card, card_available, username) "
			+ "values (#{id}, #{roleId}, #{name}, #{password}, #{title}, #{remark}, #{description}, #{schoolId}, #{type},#{available},#{mobile},#{gender},#{email},#{code},#{card}, #{cardAvailable}, #{username})")
	public void insert(Teacher teacher);

	@Update("update teacher set role_id=#{roleId}, mobile=#{mobile},remark=#{remark},name=#{name}, title=#{title}, description=#{description}, code=#{code}, card=#{card} , card_available=#{cardAvailable}, username=#{username}, email=#{email}, type=#{type}, gender=#{gender} where id=#{id}")
	public void update(Teacher teacher);
	
	@Update("update teacher set id_disk = #{idDisk} where id = #{id}")
	public void idDisk(@Param(value = "id") Long id, @Param(value = "idDisk") Long idDisk);

	@Update("update teacher set password = #{password} where id = #{id}")
	public void password(@Param(value = "id") Long id, @Param(value = "password") String password);

	@Update("update teacher set username = #{username}, mobile = #{username} where id = #{id}")
	public void username(@Param(value = "id") Long id, @Param(value = "username") String username);
	
	@Update("update teacher set type = #{type} where id = #{id}")
	public void admin(@Param(value = "id") Long id, @Param(value = "type") Long type);

	@Delete("delete from teacher where id=#{id}")
	public void delete(Long id);
	
	@Delete("delete from teacher where school_id=#{schoolId}")
	public void deleteTeachers(@Param(value = "schoolId") Long schoolId);

	@Update("update teacher set update_time = now() where id = #{id}")
	public void updated(@Param(value = "id") Long id);

	@Update("update teacher set card_available = #{cardAvailable} where id = #{id}")
	public void cardAvailable(@Param(value = "id") Long id, @Param(value = "cardAvailable") Boolean cardAvailable);

	@Select("select count(*) from classes where id = #{clsId} and  school_id = ${schoolId}")
	public int selectAllTeacherCount(@Param(value = "clsId") Long clsId, @Param(value = "schoolId") Long schoolId);

	@Select("select * from classes where id = #{clsId} and school_id = #{schoolId} order by id desc limit #{rows} offset #{offset}")
	public List<Teacher> selectAllTeacher(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "clsId") Long clsId, @Param(value = "schoolId") Long schoolId);

	@Select("select t.* from teacher t where t.school_id = #{schoolId} and t.mobile = #{mobile} limit 1")
	public Teacher selectTeacherMobile(@Param(value = "schoolId") Long schoolId, @Param(value = "mobile") String mobile); // TODO
																															// 学校一致性判断
	
	@Update("update teacher set point = #{point} , update_time = now() where id = #{id}")
	public void updatePoint(@Param(value = "id") Long id, @Param(value = "point") int point);
}
