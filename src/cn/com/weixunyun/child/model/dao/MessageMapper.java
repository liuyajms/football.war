package cn.com.weixunyun.child.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.com.weixunyun.child.model.bean.TeacherParentsMessage;
import cn.com.weixunyun.child.model.pojo.Message;

public interface MessageMapper {

	@Select("select count(*) from message where user_id_to = #{id} and time_read is null and source = false ")
	public int unreaded(@Param(value = "id") Long id);
	
	@SelectProvider(type = MessageMapperProvider.class, method = "selectCount")
	public int queryListCount(@Param(value = "id0") Long id0, @Param(value = "id1") Long id1);

	@SelectProvider(type = MessageMapperProvider.class, method = "select")
	public List<TeacherParentsMessage> queryList(@Param(value = "offset") int offset, @Param(value = "rows") int rows,
			@Param(value = "id0") Long id0, @Param(value = "id1") Long id1);

	@Insert("insert into message (id, school_id, user_id_from, user_id_to, description, time_send, time_read, type, source, voice_length)"
			+ " values(#{id}, #{schoolId}, #{userIdFrom}, #{userIdTo}, #{description}, #{timeSend}, #{timeRead},#{type}, #{source}, #{voiceLength})")
	public void insert(Message message);

	@UpdateProvider(type = MessageMapperProvider.class, method = "updateData")
	public void update(Message message, @Param(value = "userId") Long userId);

	@Delete("delete from message where id=#{id}")
	public void delete(Long id);

	@SelectProvider(type = MessageMapperProvider.class, method = "getSessionList")
	public List<Map<String, ?>> getSessionList(@Param(value = "id") Long id);
}
