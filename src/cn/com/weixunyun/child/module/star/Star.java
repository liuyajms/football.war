package cn.com.weixunyun.child.module.star;

import java.io.Serializable;

public class Star implements Serializable {
	private Long id;
	private Long schoolId;
	private Long classesId;
	private String name;
	private String description;
	private Long createTeacherId;
	private java.sql.Timestamp createTime;
	private Long updateTeacherId;
	private java.sql.Timestamp updateTime;
	private Long auditTeacherId;
	private java.sql.Timestamp auditTime;

	public Long getClassesId() {
		return classesId;
	}

	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getSchoolId() {
		return schoolId;
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

	public void setAuditTeacherId(Long auditTeacherId) {
		this.auditTeacherId = auditTeacherId;
	}

	public Long getAuditTeacherId() {
		return auditTeacherId;
	}

	public void setAuditTime(java.sql.Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public java.sql.Timestamp getAuditTime() {
		return auditTime;
	}

}