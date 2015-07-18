package cn.com.weixunyun.child.module.download;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.pojo.School;

public interface DownloadMapper {

	@Select("select * from download where id = #{id}")
	public Download select(Long id);

	@Insert("insert into download (id, name, size, description, create_teacher_id, create_time, school_id, classes_id, content_type,top_days,name_file) values (#{id}, #{name}, #{size}, #{description}, #{createTeacherId}, now(), #{schoolId}, #{classesId}, #{contentType}, #{topDays},#{nameFile})")
	public void insert(Download download);
	
	@Update("update download set description=#{description}, top_days = #{topDays}, name_file = #{nameFile} where id=#{id}")
	public void update(Download download);

	@Delete("delete from download where id=#{id}")
	public void delete(Long id);
	
	@Delete("delete from download where school_id=#{schoolId} and classes_id=#{classesId}")
	public void deleteDownloads(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId);

	@SelectProvider(type = DownloadMapperProvider.class, method = "selectCount")
	public int selectCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "classesId") Long classesId, @Param(value = "flag") Long flag);

	@SelectProvider(type = DownloadMapperProvider.class, method = "select")
	public List<ClassesDownload> selectAll(@Param(value = "offset") int offset,
			@Param(value = "rows") int rows,
			@Param(value = "schoolId") Long schoolId,
			@Param(value = "classesId") Long classesId,
			@Param(value = "flag") Long flag);

}
