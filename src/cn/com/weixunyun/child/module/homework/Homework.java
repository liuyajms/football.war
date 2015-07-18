package cn.com.weixunyun.child.module.homework;

import java.io.Serializable;

public class Homework implements Serializable {
	
	private Long id;
	private Long classesId;
	private Long courseId;
	private String description;
	private java.sql.Timestamp createTime;
	private java.sql.Timestamp updateTime;
	private Long schoolId;
	private Long createTeacherId;
	private Long updateTeacherId;
	private String term;
	
	private String courseName;
	
	

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Long getClassesId() {
		return classesId;
	}

	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setUpdateTime(java.sql.Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public java.sql.Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setCreateTeacherId(Long createTeacherId) {
		this.createTeacherId = createTeacherId;
	}

	public Long getCreateTeacherId() {
		return createTeacherId;
	}

	public void setUpdateTeacherId(Long updateTeacherId) {
		this.updateTeacherId = updateTeacherId;
	}

	public Long getUpdateTeacherId() {
		return updateTeacherId;
	}

}