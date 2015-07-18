package cn.com.weixunyun.child.module.broadcast;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import cn.com.weixunyun.child.model.bean.UserBroadcastComment;

public interface BroadcastCommentMapper {

	@Delete("delete from broadcast_comment where id = #{id}")
	public void delete(Long id);

	@Delete("update broadcast_comment set description = #{description} where id = #{id}")
	public void update(BroadcastComment comment);

	@Select("select * from broadcast_comment where id = #{id}")
	public BroadcastComment select(Long id);

	@Select("select count(*) from broadcast_comment where broadcast_id = #{id}")
	public int getCommentsCount(Long id);

	@Insert("insert into broadcast_comment (id,broadcast_id,description,create_time,create_user_id, school_id) values (#{id}, #{broadcastId}, #{description},now(),#{createUserId}, #{schoolId})")
	public void insert(BroadcastComment comment);

	@SelectProvider(type = BroadcastCommentMapperProvider.class, method = "select")
	public List<UserBroadcastComment> getAllBroadCastComments(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "broadcastId") Long broadcastId,
			@Param(value = "keyword") String keyword);

	@SelectProvider(type = BroadcastCommentMapperProvider.class, method = "selectCount")
	public int getAllBroadCastCommentsCount(@Param(value = "broadcastId") Long broadcastId,
			@Param(value = "keyword") String keyword);

}
