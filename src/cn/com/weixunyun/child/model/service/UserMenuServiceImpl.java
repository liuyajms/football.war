package cn.com.weixunyun.child.model.service;

import java.sql.Timestamp;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import cn.com.weixunyun.child.model.dao.UserMenuMapper;
import cn.com.weixunyun.child.model.pojo.UserMenu;


public class UserMenuServiceImpl extends AbstractService implements UserMenuService {

	@Override
	public void delete(Long userId, Long schoolId, Long menuId) {
		super.getMapper(UserMenuMapper.class).delete(userId, schoolId, menuId);
	}
	
	@Override
	public void insert(UserMenu menu) {
		super.getMapper(UserMenuMapper.class).insert(menu);
	}
	
	@Override
	public void inserUserMenu(MultivaluedMap<String, String> formData,
			Long userId, Long schoolId) {
		List<String> menuIdList = formData.get("menuId");

		UserMenuMapper mapper = super.getMapper(UserMenuMapper.class);

		mapper.deleteAll(userId, schoolId);//先删除所有
		if (menuIdList != null && menuIdList.size() > 0) {
			for (String menuId : menuIdList) {
				
				UserMenu userMenu = new UserMenu();
			    userMenu.setSchoolId(schoolId);
			    userMenu.setUserId(userId);
			    userMenu.setMenuId(Long.parseLong(menuId));
			    userMenu.setTime(new Timestamp(System.currentTimeMillis()));
				
			    mapper.insert(userMenu);
				
			}
		}
		
	}
	
	@Override
	public List<UserMenu> selectAll(Long userId, Long schoolId) {
		return super.getMapper(UserMenuMapper.class).selectAll(userId, schoolId);
	}
	
	@Override
	public int selectAllCount(Long userId, Long schoolId) {
		return super.getMapper(UserMenuMapper.class).selectAllCount(userId, schoolId);
	}

	@Override
	public void deleteAll(Long userId, Long schoolId) {
	   super.getMapper(UserMenuMapper.class).deleteAll(userId, schoolId);
	}

}
