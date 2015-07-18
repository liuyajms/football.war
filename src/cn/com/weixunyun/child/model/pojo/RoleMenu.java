package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class RoleMenu implements Serializable {
	private Long menuId;
	private Long roleId;

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getRoleId() {
		return roleId;
	}

}