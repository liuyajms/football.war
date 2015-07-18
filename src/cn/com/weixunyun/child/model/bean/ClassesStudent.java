package cn.com.weixunyun.child.model.bean;

import cn.com.weixunyun.child.model.pojo.Student;

public class ClassesStudent extends Student {

	private Long schoolId;
	private String classesName;
	private int classesYear;
	private String genderName;
	private Long classesTeacherId;

	public Long getClassesTeacherId() {
		return classesTeacherId;
	}

	public void setClassesTeacherId(Long classesTeacherId) {
		this.classesTeacherId = classesTeacherId;
	}

	public int getClassesYear() {
		return classesYear;
	}

	public void setClassesYear(int classesYear) {
		this.classesYear = classesYear;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public String getClassesName() {
		return classesName;
	}

	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

}
