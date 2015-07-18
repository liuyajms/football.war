package cn.com.weixunyun.child.module.curriculum;

import java.io.Serializable;

public class Curriculum implements Serializable {
	private Long id;
	private Long classesId;
	private String type;
	private Boolean defaultc;
	private String description;
	private Long createTeacherId;
	private java.sql.Timestamp createTime;
	private Long updateTeacherId;
	private java.sql.Timestamp updateTime;
	private Long schoolId;
	private String term;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}

	public Long getClassesId() {
		return classesId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setDefaultc(Boolean defaultc) {
		this.defaultc = defaultc;
	}

	public Boolean getDefaultc() {
		return defaultc;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setCreateTeacherId(Long createTeacherId) {
		this.createTeacherId = createTeacherId;
	}

	public Long getCreateTeacherId() {
		return createTeacherId;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setUpdateTeacherId(Long updateTeacherId) {
		this.updateTeacherId = updateTeacherId;
	}

	public Long getUpdateTeacherId() {
		return updateTeacherId;
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

}