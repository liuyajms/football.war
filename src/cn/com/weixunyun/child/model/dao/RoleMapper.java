package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.bean.RolePopedom;
import cn.com.weixunyun.child.model.pojo.Role;

public interface RoleMapper {

	@Select("select * from role where id = #{id}")
	public Role select(Long id);
	
	@Select("select id from role where name like '%管理员%' limit 1")
	public Long getAdmin();

	@Select("select count(*) from role where school_id = #{schoolId} ")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId);

	@Select("select id, name, description from role where school_id=#{schoolId} order by id desc limit #{rows} offset #{offset}")
	public List<Role> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId);

	@Insert("insert into role (id, school_id, name, description) values (#{id}, #{schoolId}, #{name}, #{description})")
	public void insert(Role role);

	@Update("update role set school_id=#{schoolId}, name=#{name}, description=#{description} where id=#{id}")
	public void update(Role role);

	@Delete("delete from role where id=#{id}")
	public void delete(Long id);

	@Select("select p.*, rp.action from role_popedom rp join popedom p on p.id = rp.popedom_id where rp.role_id = #{id} ")
	public List<RolePopedom> getPopedomList(Long id);

	@Delete("delete from role_popedom where role_id = #{id}")
	public void clearPopedom(Long id);

	@Insert("insert into role_popedom (role_id, popedom_id, action) values (#{roleId}, #{popedomId}, #{action}) ")
	public void insertPopedom(@Param(value = "roleId") Long roleId, @Param(value = "popedomId") Long popedomId,
			@Param(value = "action") Long action);

}
