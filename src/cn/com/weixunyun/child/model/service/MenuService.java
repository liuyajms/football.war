package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.MenuMapper;

public interface MenuService extends MenuMapper {

	public void insertRoleData(Long roleId, String menuList);
	
}
