package cn.com.weixunyun.child.module.curriculum;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface CurriculumMapper {

	@Select("select * from curriculum where id = #{id}")
	public Curriculum select(Long id);

	@Insert("insert into curriculum (id, classes_id, type, defaultc, description, create_teacher_id, create_time, update_teacher_id, update_time, school_id, term) values (#{id}, #{classesId}, #{type}, #{defaultc}, #{description}, #{createTeacherId}, now(), #{updateTeacherId}, now(), #{schoolId}, #{term})")
	public void insert(Curriculum curriculum);

	@Update("update curriculum set type=#{type}, defaultc=#{defaultc}, description=#{description}, update_teacher_id=#{updateTeacherId}, update_time = now() where id=#{id}")
	public void update(Curriculum curriculum);

	@Delete("delete from curriculum where id=#{id}")
	public void delete(int id);

	@Delete("delete from curriculum where classes_id=#{classesId} and school_id=#{schoolId} and term=#{term}")
	public void deleteMulti(@Param(value = "classesId") Long classesId, @Param(value = "schoolId") Long schoolId, @Param(value = "term") String term);
	
	@SelectProvider(type = CurriculumMapperProvider.class, method = "selectCount")
	public int selectAllCount(@Param(value = "type") String type, @Param(value = "classesId") Long classesId,
			@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term);

	@SelectProvider(type = CurriculumMapperProvider.class, method = "select")
	public List<ClassesCurriculum> selectAll(@Param(value = "offset") int offset, @Param(value = "rows") int rows,
			@Param(value = "type") String type, @Param(value = "classesId") Long classesId,
			@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term);

	@Update("update curriculum set update_time = now() where id = #{id}")
	public void updateImage(@Param(value = "id") Long id);

}
