package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class RolePopedom implements Serializable {
	private Long roleId;
	private Long popedomId;

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setPopedomId(Long popedomId) {
		this.popedomId = popedomId;
	}

	public Long getPopedomId() {
		return popedomId;
	}

}