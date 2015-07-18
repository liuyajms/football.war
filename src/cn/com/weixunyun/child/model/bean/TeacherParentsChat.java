package cn.com.weixunyun.child.model.bean;

import cn.com.weixunyun.child.model.pojo.Chat;

public class TeacherParentsChat extends Chat {

	private String userName;
	private int userType;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

}
