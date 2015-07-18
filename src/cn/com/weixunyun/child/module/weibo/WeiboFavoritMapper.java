package cn.com.weixunyun.child.module.weibo;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface WeiboFavoritMapper {

	@Insert("insert into weibo_favorit (user_id, time, weibo_id, school_id) values(#{userId}, now(), #{weiboId}, #{schoolId})")
	public void insert(WeiboFavorit favorit);

	@Delete("delete from weibo_favorit where weibo_id = #{weiboId} and user_id = #{userId}")
	public void delete(@Param(value = "weiboId") Long weiboId, @Param(value = "userId") Long userId);
}
