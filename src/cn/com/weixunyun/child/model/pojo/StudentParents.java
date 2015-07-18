package cn.com.weixunyun.child.model.pojo;

public class StudentParents {

	private Long studentId;
	private Long parentsId;
	private String type;
	private String username;
	private Long schoolId;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getParentsId() {
		return parentsId;
	}

	public void setParentsId(Long parentsId) {
		this.parentsId = parentsId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
