package cn.com.weixunyun.child.module.news;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface NewsMapper {

	@Select("select * from news where id = #{id}")
	public News select(Long id);

	@SelectProvider(type = NewsMapperProvider.class, method = "selectCount")
	public int queryListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "keyword") String keyword,
			@Param(value = "type") Long type, @Param(value = "pic") Boolean pic);

	@SelectProvider(type = NewsMapperProvider.class, method = "select")
	public List<News> queryList(@Param(value = "schoolId") Long schoolId, @Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "keyword") String keyword,
			@Param(value = "type") Long type, @Param(value = "pic") Boolean pic);

	@Insert("insert into news (id, school_id, type, title, title_short, title_color, tag, pic, up, comment, source, author, description, description_summary, create_teacher_id, create_time, update_teacher_id, update_time) values (#{id}, #{schoolId}, #{type}, #{title}, #{titleShort}, #{titleColor}, #{tag}, #{pic}, #{up}, #{comment}, #{source}, #{author}, #{description}, #{descriptionSummary}, #{createTeacherId}, now(), #{updateTeacherId}, now())")
	public void insert(News news);

	@Update("update news set title=#{title}, type=#{type},title_short=#{titleShort}, title_color=#{titleColor}, tag=#{tag}, pic=#{pic}, up=#{up}, comment=#{comment}, source=#{source}, author=#{author}, description=#{description}, description_summary=#{descriptionSummary}, update_teacher_id=#{updateTeacherId}, update_time=now() where id=#{id}")
	public void update(News news);

	@Update("update news set pic = #{image}, update_time = now() where id = #{id}")
	public void updateImage(@Param(value = "id") Long id, @Param(value = "image") boolean image);

	@Delete("delete from news where id=#{id}")
	public void delete(Long id);
	
	@Delete("delete from news where type=#{type}")
	public void deleteNews(@Param(value = "type") Long type);

}
