package cn.com.weixunyun.child.model.dao;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import cn.com.weixunyun.child.model.bean.TeacherParentsChat;
import cn.com.weixunyun.child.model.pojo.Chat;

public interface ChatMapper {

	@Select("select count(*) from chat where school_id = #{schoolId} and classes_id = #{classesId} and user_id = #{userId} and to_char(time, 'yyyyMMdd hh:mm:ss') >= #{time}")
	public int unreaded(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "userId") Long userId, @Param(value = "time") String time);
	
	@SelectProvider(type = ChatMapperProvider.class, method = "getList")
	public List<TeacherParentsChat> getList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows, 
			@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "time") String time);
	
	@SelectProvider(type = ChatMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId, 
			@Param(value = "time") String time);

	@Insert("insert into chat (id, school_id, classes_id, user_id, description, time, type, voice_length)"
			+ " values(#{id}, #{schoolId}, #{classesId}, #{userId}, #{description}, now(), #{type}, #{voiceLength})")
	public void insert(Chat chat);

	@Delete("delete from chat where id=#{id}")
	public void delete(Long id);

}
