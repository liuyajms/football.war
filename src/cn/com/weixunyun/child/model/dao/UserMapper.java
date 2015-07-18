package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import cn.com.weixunyun.child.model.bean.User;

public interface UserMapper {

	@Select("select * from v_user where school_id = #{schoolId} and card = #{card} limit 1 ")
	public User getCardUser(@Param(value = "schoolId") Long schoolId, @Param(value = "card") String card);

	@Select("select * from v_user where id = #{userId} limit 1 ")
	public User get(@Param(value = "userId") Long userId);

	@SelectProvider(type = UserMapperProvider.class, method = "select")
	public List<Long> select(@Param(value = "classesId") Long classesId,
			@Param(value = "nameList") List<String> nameList);

	@Select("select s.id as school_id, u.username, u.password, s.name as school_name, u.type, array_to_string(array_agg(u.name),',') as name from v_user u join school s on s.id = u.school_id where u.username = #{username} and u.password = #{password} group by s.id, u.username, u.password, s.name, u.type order by s.id asc, u.type asc")
	public List<User> login(@Param(value = "username") String username, @Param(value = "password") String password);

	@Select("select u.*, s.name as school_name from v_user u join school s on s.id = u.school_id where u.school_id = #{schoolId} and u.type = #{type} and u.username = #{username} and u.password = #{password} limit 1")
	public User schoolLogin(@Param(value = "schoolId") Long schoolId, @Param(value = "type") int type,
			@Param(value = "username") String username, @Param(value = "password") String password);

}
