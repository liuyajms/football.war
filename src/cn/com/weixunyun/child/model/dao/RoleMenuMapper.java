package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.com.weixunyun.child.model.pojo.RoleMenu;

public interface RoleMenuMapper {

	/*@Select("select * from role_menu where role_id=#{roleId} and menu_id=#{menuId}")
	public RoleMenu select(RoleMenu roleMenu);*/

	@Select("select count(*) from role_menu where role_id=#{roleId}")
	public int selectAllCount(@Param(value = "roleId") Long roleId);

	@Select("select * from role_menu where role_id=#{roleId} order by role_id, menu_id limit #{rows} offset #{offset}")
	public List<RoleMenu> selectAll(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "roleId") Long roleId);

	@Insert("insert into role_menu (role_id, menu_id) values (#{roleId}, #{menuId})")
	public void insert(RoleMenu roleMenu);

	/*@Update("update role_menu set menu_id=#{menuId}} where role_id=#{roleId}")
	public void update(RoleMenu roleMenu);*/

	@Delete("delete from role_menu where role_id=#{roleId} and menu_id=#{menuId}")
	public void delete(RoleMenu roleMenu);

}
