package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.StudentParents;
import cn.com.weixunyun.child.model.pojo.Parents;

public interface ParentsMapper {

	@Select("select p.*, sp.type, sp.username, sp.student_id from parents p join student_parents sp on p.id = sp.parents_id where p.id = #{id} limit 1")
	public StudentParents select(Long id);

	@SelectProvider(type = ParentsMapperProvider.class, method = "getAllParentsCount")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId, @Param(value = "studentId") Long studentId,
			@Param(value = "keyword") String keyword);

	@SelectProvider(type = ParentsMapperProvider.class, method = "getAllParents")
	public List<StudentParents> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "studentId") Long studentId,
			@Param(value = "keyword") String keyword);

	@Insert("insert into parents (id, mobile, name, gender, email, password, available, pta, create_time) values (#{id}, #{mobile}, #{name}, #{gender}, #{email}, #{password}, #{available}, #{pta}, now())")
	public void insert(Parents parent);

	@Update("update parents set mobile=#{mobile}, name=#{name}, gender=#{gender}, update_time=now(), pta=#{pta} where id=#{id}")
	public void update(Parents parent);

	@Update("update parents set update_time = now() where id = #{id} ")
	public void updated(@Param(value = "id") Long id);

	@Update("update parents set mobile = #{username} where id = #{id}")
	public void username(@Param(value = "id") Long id, @Param(value = "username") String username);

	@Update("update parents set password = #{password} where id = #{id}")
	public void password(@Param(value = "id") Long id, @Param(value = "password") String password);
	
	@Update("update parents set id_disk = #{idDisk} where id = #{id}")
	public void idDisk(@Param(value = "id") Long id, @Param(value = "idDisk") Long idDisk);

	@Delete("delete from parents where id=#{id}")
	public void delete(Long id);

	@Select("select * from parents where student_id = #{studentId} and type = #{type} limit 1")
	public Parents selectCode(@Param(value = "studentId") long studentId, @Param(value = "type") String type);

	@Select("select * from parents where student_id = #{studentId} and mobile = #{mobile} limit 1")
	public Parents selectMobile(@Param(value = "studentId") long studentId, @Param(value = "mobile") String mobile);

	@SelectProvider(type = ParentsMapperProvider.class, method = "getStudentList")
	public List<ClassesStudent> getStudentList(@Param(value = "schoolId") Long schoolId,
			@Param(value = "parentsId") Long parentsId);

	@Select("select * from parents where mobile = #{mobile} limit 1")
	public Parents selectParentsMobile(@Param(value = "mobile") String mobile);

	@Select("select p.* from parents p join student s on p.student_id = s.id join classes c on c.id = s.classes_id where school_id = #{schoolId} order by p.id desc")
	public List<Parents> selectParentsInSchool(@Param(value = "schoolId") Long schoolId);

	@SelectProvider(type = ParentsMapperProvider.class, method = "getSchoolParents")
	public Parents getSchoolParents(@Param(value = "schoolId") Long schoolId,
			@Param(value = "username") String username, @Param(value = "password") String password);
	
	@Update("update parents set point = #{point} , update_time = now() where id = #{id}")
	public void updatePoint(@Param(value = "id") Long id, @Param(value = "point") int point);

}
