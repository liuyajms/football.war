package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.bean.MenuRole;
import cn.com.weixunyun.child.model.pojo.Menu;

public interface MenuMapper {

	@Select("select * from menu where id = #{id}")
	public Menu select(long id);
	
	@Select("select count(*) from menu ")
	public int selectAllCount();
	
	@Select("select distinct m.*, case rm.role_id when #{roleId} then true else false end as check" +
			"  from menu m " +
			"  left join role_menu rm on m.id = rm.menu_id " +
			"   and rm.role_id = #{roleId} " +
			" order by id limit #{rows} offset #{offset}")
	public List<MenuRole> selectMenus(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "roleId") Long roleId);
	
/*	@Select("select * from menu order by id ")
	public List<Menu> selectAll();*/
	@Select("select * from menu  where school_id=#{schoolId} order by id")
	public List<Menu> selectAll(Long schoolId);
	
	@Insert("insert into menu (id, id_parent, url, name, icon, description, ord, code, school_id) "
			+ "values (#{id}, #{idParent}, #{url}, #{name}, #{icon}, #{description}, #{ord}, #{code}, #{schoolId} )")
	public void insert(Menu menu);

	@Update("update menu set id_parent=#{idParent},  url=#{url}, name=#{name}, icon=#{icon}, "
			+ "description=#{description}, ord=#{ord}, code=#{code}, school_id=#{schoolId} where id=#{id}")
	public void update(Menu menu);

	@Delete("delete from menu where id=#{id}")
	public void delete(long id);
	
	@Update("insert into role_menu(menu_id, role_id) values (#{menuId}, #{roleId})")
	public void insertRole(@Param(value = "roleId") Long roleId, @Param(value = "menuId") Long menuId);
	
	@Select("select case count(*) when 0 then false else true end from role_menu where menu_id=#{menuId} and role_id=#{id}")
	public Boolean selectRole(@Param(value = "id") Long id, @Param(value = "menuId") Long menuId);
	
	@Delete("delete from role_menu where role_id=#{id}")
	public void deleteRoleMenu(Long id);

}
