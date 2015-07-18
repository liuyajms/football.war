package cn.com.weixunyun.child.model.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.com.weixunyun.child.model.bean.User;
import cn.com.weixunyun.child.model.dao.UserMapper;

public class UserServiceImpl extends AbstractService implements UserService {

	@Override
	public List<Long> select(@Param("classesId") Long classesId, @Param("nameList") List<String> nameList) {
		return super.getMapper(UserMapper.class).select(classesId, nameList);
	}

	@Override
	public User getCardUser(@Param("schoolId") Long schoolId, @Param("card") String card) {
		return super.getMapper(UserMapper.class).getCardUser(schoolId, card);
	}

	@Override
	@Select("select * from v_user where username = #{username} and password = #{password}")
	public List<User> login(@Param("username") String username, @Param("password") String password) {
		return super.getMapper(UserMapper.class).login(username, password);
	}

	@Override
	public User schoolLogin(@Param("schoolId") Long schoolId, @Param("type") int type,
			@Param("username") String username, @Param("password") String password) {
		return super.getMapper(UserMapper.class).schoolLogin(schoolId, type, username, password);
	}

	@Override
	public User get(Long userId) {
		return super.getMapper(UserMapper.class).get(userId);
	}

}
