package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.RoleMenuMapper;
import cn.com.weixunyun.child.model.pojo.RoleMenu;

public class RoleMenuServiceImpl extends AbstractService implements RoleMenuService {

	/*@Override
	public RoleMenu select(RoleMenu roleMenu) {
		return super.getMapper(RoleMenuMapper.class).select(roleMenu);
	}*/

	@Override
	public int selectAllCount(Long roleId) {
		return super.getMapper(RoleMenuMapper.class).selectAllCount(roleId);
	}

	@Override
	public List<RoleMenu> selectAll(Long offset, Long rows, Long roleId) {
		return super.getMapper(RoleMenuMapper.class).selectAll(offset, rows, roleId);
	}

	@Override
	public void insert(RoleMenu roleMenu) {
		super.getMapper(RoleMenuMapper.class).insert(roleMenu);
	}

	/*@Override
	public void update(RoleMenu roleMenu) {
		super.getMapper(RoleMenuMapper.class).update(roleMenu);
	}*/

	@Override
	public void delete(RoleMenu roleMenu) {
		super.getMapper(RoleMenuMapper.class).delete(roleMenu);
	}
	
}
