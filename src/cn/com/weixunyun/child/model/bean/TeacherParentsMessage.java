package cn.com.weixunyun.child.model.bean;

import cn.com.weixunyun.child.model.pojo.Message;

public class TeacherParentsMessage extends Message {

	private String userNameFrom;
	private int userTypeFrom;
	private String userNameTo;
	private int userTypeTo;

	public String getUserNameFrom() {
		return userNameFrom;
	}

	public void setUserNameFrom(String userNameFrom) {
		this.userNameFrom = userNameFrom;
	}

	public String getUserNameTo() {
		return userNameTo;
	}

	public void setUserNameTo(String userNameTo) {
		this.userNameTo = userNameTo;
	}

	public int getUserTypeFrom() {
		return userTypeFrom;
	}

	public void setUserTypeFrom(int userTypeFrom) {
		this.userTypeFrom = userTypeFrom;
	}

	public int getUserTypeTo() {
		return userTypeTo;
	}

	public void setUserTypeTo(int userTypeTo) {
		this.userTypeTo = userTypeTo;
	}

}
