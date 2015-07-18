package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.bean.MenuRole;
import cn.com.weixunyun.child.model.dao.MenuMapper;
import cn.com.weixunyun.child.model.pojo.Menu;

public class MenuServiceImpl extends AbstractService implements MenuService {

	@Override
	public Menu select(long id) {
		return super.getMapper(MenuMapper.class).select(id);
	}

	@Override
	public int selectAllCount() {
		return super.getMapper(MenuMapper.class).selectAllCount();
	}

	@Override
	public List<Menu> selectAll(Long schoolId) {
		return super.getMapper(MenuMapper.class).selectAll(schoolId);
	}

	@Override
	public void insert(Menu menu) {
		super.getMapper(MenuMapper.class).insert(menu);
	}

	@Override
	public void update(Menu menu) {
		super.getMapper(MenuMapper.class).update(menu);
	}

	@Override
	public void delete(long id) {
		super.getMapper(MenuMapper.class).delete(id);
	}

	@Override
	public List<MenuRole> selectMenus(Long offset, Long rows, Long roleId) {
		return super.getMapper(MenuMapper.class).selectMenus(offset, rows, roleId);
	}

	@Override
	public void insertRoleData(Long roleId, String menuList) {
		
		if(menuList == null || "".equals(menuList)){
			
			System.out.println("*************************");
			
			deleteRoleMenu(roleId);
		}else{
			String[] menus = menuList.split(",");
			Boolean flag = true;
			
			for(String m : menus){
				if(Integer.valueOf(m) % 10 != 0){
					System.out.println("m = " + m + " : " + Integer.valueOf(m) % 10);
					flag = selectRole(roleId, Long.valueOf(m));
					
					System.out.println(flag);
					
				}else{
					flag = true;
				}
				if(flag == false){
					insertRole(roleId, Long.valueOf(m));
				}
			}
		}
					
	}

	@Override
	public void insertRole(Long roleId, Long menu) {
		super.getMapper(MenuMapper.class).insertRole(roleId, menu);	
	}

	@Override
	public Boolean selectRole(Long id, Long menuId) {
		
		return super.getMapper(MenuMapper.class).selectRole(id, menuId);
	}
	
	@Override
	public void deleteRoleMenu(Long id) {
		
		super.getMapper(MenuMapper.class).deleteRoleMenu(id);
	}

}
