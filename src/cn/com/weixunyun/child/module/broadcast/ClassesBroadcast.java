package cn.com.weixunyun.child.module.broadcast;


public class ClassesBroadcast extends Broadcast {
	private int type;
	private String classesName;
	private String createTeacherName;
	private Long commentCount;

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getClassesName() {
		return classesName;
	}

	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}

	public String getCreateTeacherName() {
		return createTeacherName;
	}

	public void setCreateTeacherName(String createTeacherName) {
		this.createTeacherName = createTeacherName;
	}

}
