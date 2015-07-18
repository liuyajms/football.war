package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.bean.PerformanceStudent;
import cn.com.weixunyun.child.model.pojo.Performance;

public interface PerformanceMapper {

	@Select("select * from performance where id = #{id}")
	public Performance select(Long id);

	@SelectProvider(type = PerformanceMapperProvider.class, method = "selectCount")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "con") String con);

	@SelectProvider(type = PerformanceMapperProvider.class, method = "select")
	public List<PerformanceStudent> selectAll(
			@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "con") String con,
			@Param(value = "schoolId") Long schoolId);

	@Insert("insert into performance (id, student_id, description, create_teacher_id, create_time, update_teacher_id, update_time, school_id) values (#{id}, #{studentId}, #{description}, #{createTeacherId}, now(), #{updateTeacherId}, now(), #{schoolId})")
	public void insert(Performance performance);

	@Update("update performance set student_id=#{studentId}, description=#{description}, update_teacher_id=#{updateTeacherId}, update_time=now() where id=#{id}")
	public void update(Performance performance);

	@Delete("delete from performance where id=#{id}")
	public void delete(Long id);

}
