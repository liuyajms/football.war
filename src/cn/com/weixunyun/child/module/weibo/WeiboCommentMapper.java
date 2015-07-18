package cn.com.weixunyun.child.module.weibo;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface WeiboCommentMapper {

	@Delete("delete from weibo_comment where id = #{id}")
	public void delete(Long id);

	@Select("select * from weibo_comment where id = #{id}")
	public WeiboComment select(Long id);

	@Update("update weibo_comment set description = #{description} where id = #{id}")
	public void update(WeiboComment weiboComment);

	@Select("select count(*) from weibo_comment where weibo_id = #{weiboId}")
	public int queryListCount(Long weiboId);

	@Insert("insert into weibo_comment (id,weibo_id,description,create_time,create_user_id, position_x, position_y," +
            " position_name, school_id, user_id_to) values (#{id}, #{weiboId}, #{description},now(),#{createUserId}, #{positionX}, #{positionY}," +
            " #{positionName}, #{schoolId},#{userIdTo})")
	public void insert(WeiboComment WeiboComments);

	@SelectProvider(type = WeiboCommentMapperProvider.class, method = "queryList")
	public List<UserWeiboComment> queryList(@Param(value = "weiboId") Long weiboId);

	@SelectProvider(type = WeiboCommentMapperProvider.class, method = "commentList")
	public List<UserWeiboComment> queryComments(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "weiboId") Long weiboId,
			@Param(value = "keyword") String keyword);

	@SelectProvider(type = WeiboCommentMapperProvider.class, method = "commentListCount")
	public int queryCommentsCount(@Param(value = "weiboId") Long weiboId, @Param(value = "keyword") String keyword);

}
