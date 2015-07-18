package cn.com.weixunyun.child.model.service;

import javax.ws.rs.core.MultivaluedMap;

import cn.com.weixunyun.child.model.dao.UserMenuMapper;


public interface UserMenuService extends UserMenuMapper {

	void inserUserMenu(MultivaluedMap<String, String> formData, Long userId,
			Long schoolId);


}
