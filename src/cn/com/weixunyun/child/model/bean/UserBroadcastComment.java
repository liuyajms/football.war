package cn.com.weixunyun.child.model.bean;

import cn.com.weixunyun.child.module.broadcast.BroadcastComment;

public class UserBroadcastComment extends BroadcastComment {

	private String createUserName;
	private int createUserType;
	private java.sql.Timestamp createUserUpdateTime;

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public int getCreateUserType() {
		return createUserType;
	}

	public void setCreateUserType(int createUserType) {
		this.createUserType = createUserType;
	}

	public java.sql.Timestamp getCreateUserUpdateTime() {
		return createUserUpdateTime;
	}

	public void setCreateUserUpdateTime(java.sql.Timestamp createUserUpdateTime) {
		this.createUserUpdateTime = createUserUpdateTime;
	}

}
