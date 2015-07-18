package cn.com.weixunyun.child.model.bean;

import cn.com.weixunyun.child.model.pojo.Student;

public class StudentClasses extends Student {

	private String classesName;
	private String genderName;
	private String parentsName;
	private String parentsMobile;
	private String parentsType;
	private Boolean parentsPta;
	private String parentsUsername;
	
	public String getParentsUsername() {
		return parentsUsername;
	}

	public void setParentsUsername(String parentsUsername) {
		this.parentsUsername = parentsUsername;
	}

	public String getParentsName() {
		return parentsName;
	}

	public void setParentsName(String parentsName) {
		this.parentsName = parentsName;
	}

	public String getParentsMobile() {
		return parentsMobile;
	}

	public void setParentsMobile(String parentsMobile) {
		this.parentsMobile = parentsMobile;
	}

	public String getParentsType() {
		return parentsType;
	}

	public void setParentsType(String parentsType) {
		this.parentsType = parentsType;
	}

	public Boolean getParentsPta() {
		return parentsPta;
	}

	public void setParentsPta(Boolean parentsPta) {
		this.parentsPta = parentsPta;
	}

	public String getClassesName() {
		return classesName;
	}

	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

}
