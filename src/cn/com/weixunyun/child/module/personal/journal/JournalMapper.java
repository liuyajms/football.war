package cn.com.weixunyun.child.module.personal.journal;

import org.apache.ibatis.annotations.*;

import java.util.List;

public interface JournalMapper {

	@Select("select * from journal where id = #{id}")
	public UserJournal select(Long id);

	@SelectProvider(type = JournalMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "userId") Long userId,
			@Param(value = "keyword") String keyword);

	@SelectProvider(type = JournalMapperProvider.class, method = "getList")
	public List<UserJournal> getList(@Param(value = "offset") int offset,
			@Param(value = "rows") int rows,
			@Param(value = "schoolId") Long schoolId,
			@Param(value = "userId") Long userId,
			@Param(value = "keyword") String keyword);

	@Insert(" INSERT INTO journal(\n"
			+ "            id, school_id, description, pic, voice_length, create_user_id, \n"
			+ "            create_time, update_user_id, update_time) VALUES (#{id},#{schoolId}, #{description}, #{pic}, #{voiceLength}, #{createUserId}, now(), #{updateUserId}, now()) ")
	public void insert(Journal journal);

	@Update("UPDATE journal\n"
			+ "   SET  description=#{description}, pic=#{pic}, voice_length=#{voiceLength}, update_user_id= #{updateUserId}, update_time=now() \n"
			+ " WHERE id=#{id}")
	public void update(Journal journal);

	@Delete("delete from journal where id=#{id}")
	public void delete(Long id);

	@Update("update journal set pic=#{pic}, update_user_id=#{userId}, update_time = now() where id = #{id}")
	void updateImage(@Param(value = "id") Long id,
			@Param(value = "userId") Long userId, @Param(value = "pic") int pic);
}
