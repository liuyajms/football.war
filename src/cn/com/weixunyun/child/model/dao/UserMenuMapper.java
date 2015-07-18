package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.com.weixunyun.child.model.pojo.UserMenu;

public interface UserMenuMapper {

	
	@Select("select count(*) from user_menu where user_id=#{userId} and school_id=#{schoolId}")
	public int selectAllCount(@Param(value = "userId") Long userId,  @Param(value = "schoolId") Long schoolId);
	
	@Select("select * from user_menu  where user_id=#{userId} and school_id=#{schoolId} order by menu_id")
	public List<UserMenu> selectAll(@Param(value = "userId") Long userId,  @Param(value = "schoolId") Long schoolId);
	
	@Insert("insert into user_menu (user_id, school_id, menu_id, time) "
			+ "values (#{userId}, #{schoolId}, #{menuId}, #{time} )")
	public void insert(UserMenu menu);


	@Delete("delete from user_menu where user_id=#{userId} and school_id=#{schoolId} and  menu_id=#{menuId}")
	public void delete(@Param(value = "userId") Long userId, @Param(value = "schoolId") Long schoolId, 
			@Param(value = "menuId") Long menuId);
	
	@Delete("delete from user_menu where user_id=#{userId}  and school_id=#{schoolId}")
	public void deleteAll(@Param(value = "userId") Long userId,@Param(value = "schoolId") Long schoolId);
	
}
