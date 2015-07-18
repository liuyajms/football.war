package cn.com.weixunyun.child.module.weibo;


import java.security.Timestamp;

public class UserWeiboComment extends WeiboComment {
	private String createUserName;
	private int createUserType;
	private java.sql.Timestamp createUserUpdateTime;
	private Long schoolId;

    private String userNameTo;
    private int userTypeTo;
    private Timestamp userUpdateTimeTo;

    public String getUserNameTo() {
        return userNameTo;
    }

    public void setUserNameTo(String userNameTo) {
        this.userNameTo = userNameTo;
    }

    public int getUserTypeTo() {
        return userTypeTo;
    }

    public void setUserTypeTo(int userTypeTo) {
        this.userTypeTo = userTypeTo;
    }

    public Timestamp getUserUpdateTimeTo() {
        return userUpdateTimeTo;
    }

    public void setUserUpdateTimeTo(Timestamp userUpdateTimeTo) {
        this.userUpdateTimeTo = userUpdateTimeTo;
    }

    public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public int getCreateUserType() {
		return createUserType;
	}

	public void setCreateUserType(int createUserType) {
		this.createUserType = createUserType;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public java.sql.Timestamp getCreateUserUpdateTime() {
		return createUserUpdateTime;
	}

	public void setCreateUserUpdateTime(java.sql.Timestamp createUserUpdateTime) {
		this.createUserUpdateTime = createUserUpdateTime;
	}

}
