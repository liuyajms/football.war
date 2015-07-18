package cn.com.weixunyun.child.module.weibo;


import java.util.List;

public class UserWeibo extends Weibo {
	private String classesName;
	private Long commentCount;
	private String createUserName;
	private int createUserType;
	private java.sql.Timestamp createUserUpdateTime;
	private Boolean favorit;

    private List<WeiboZanVO> zanList;

    private List<UserWeiboComment> commentList;

    public List<WeiboZanVO> getZanList() {
        return zanList;
    }

    public void setZanList(List<WeiboZanVO> zanList) {
        this.zanList = zanList;
    }

    public List<UserWeiboComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<UserWeiboComment> commentList) {
        this.commentList = commentList;
    }

    public Boolean getFavorit() {
		return favorit;
	}

	public void setFavorit(Boolean favorit) {
		this.favorit = favorit;
	}

	public String getClassesName() {
		return classesName;
	}

	public void setClassesName(String classesName) {
		this.classesName = classesName;
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

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public java.sql.Timestamp getCreateUserUpdateTime() {
		return createUserUpdateTime;
	}

	public void setCreateUserUpdateTime(java.sql.Timestamp createUserUpdateTime) {
		this.createUserUpdateTime = createUserUpdateTime;
	}


}
