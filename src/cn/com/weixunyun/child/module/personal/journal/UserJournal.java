package cn.com.weixunyun.child.module.personal.journal;

public class UserJournal extends Journal {
	private static final long serialVersionUID = 1L;
	private String createUserName;
	private String updateUserName;

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUser(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUser(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
