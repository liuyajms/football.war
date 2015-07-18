package cn.com.weixunyun.child.module.star;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface StarMapper {

	@Select("select s.*, c.name as classesName, t.name as createTeacherName from star s join classes c on c.id = s.classes_id join teacher t on t.id = s.create_teacher_id where s.id = #{id} limit 1")
	public StarClasses get(Long id);

	@SelectProvider(type = StarMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "classesId") Long classesId);

	@SelectProvider(type = StarMapperProvider.class, method = "getList")
	public List<StarClasses> getList(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId,
			@Param(value = "classesId") Long classesId);

	@Insert("insert into star (id, school_id, classes_id, name, description, create_teacher_id, create_time) values (#{id}, #{schoolId}, #{classesId}, #{name}, #{description}, #{createTeacherId}, now())")
	public void insert(Star star);

	@Update("update star set name = #{name}, description = #{description}, update_teacher_id = #{updateTeacherId}, update_time = now() where id = #{id}")
	public void update(Star star);

	@Update("update star set update_teacher_id = #{teacherId}, update_time = now() where id = #{id}")
	public void updated(@Param(value = "id") Long id,
			@Param(value = "teacherId") Long teacherId);

	@Delete("delete from star where id = #{id}")
	public void delete(Long id);
	
	@Delete("delete from star where classes_id = #{classesId}")
	public void deleteStars(@Param(value = "classesId") Long classesId);

}
