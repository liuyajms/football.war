package cn.com.weixunyun.child.module.cook;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface CookMapper {

	@Select("select * from cook where id = #{id}")
	public Cook get(int id);

	@SelectProvider(type = CookMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "keyword") String keyword,
			@Param(value = "term") String term);

	@SelectProvider(type = CookMapperProvider.class, method = "getList")
	public List<Cook> getList(@Param(value = "offset") int offset, @Param(value = "rows") int rows,
			@Param(value = "keyword") String keyword, @Param(value = "schoolId") Long schoolId,
			@Param(value = "term") String term);

	@Insert("insert into cook (id, name, description, create_teacher_id, create_time, update_teacher_id, update_time, school_id, term) values (#{id}, #{name}, #{description}, #{createTeacherId}, now(), #{updateTeacherId}, now(), #{schoolId}, #{term})")
	public void insert(Cook menu);

	@Update("update cook set name = #{name}, description = #{description}, update_teacher_id = #{updateTeacherId}, update_time = now() where id = #{id}")
	public void update(Cook menu);

	@Delete("delete from cook where id = #{id}")
	public void delete(int id);
	
	@Delete("delete from cook where school_id = #{schoolId} and term = #{term}")
	public void deleteCooks(@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term);

}
