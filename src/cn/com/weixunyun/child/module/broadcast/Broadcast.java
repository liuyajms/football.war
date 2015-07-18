package cn.com.weixunyun.child.module.broadcast;

import java.io.Serializable;

public class Broadcast implements Serializable {
	private Long id;
	private Long schoolId;
	private String title;
	private String description;
	private Long classesId;
	private java.sql.Timestamp createTime;
	private Long createTeacherId;
	private java.sql.Timestamp updateTime;
	private Long updateTeacherId;
	private Long auditTeacherId;
	private Boolean comment;
	private Long pic;
	private Long voiceLength;
	private int grade;
	private String term;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Boolean getComment() {
		return comment;
	}

	public void setComment(Boolean comment) {
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getClassesId() {
		return classesId;
	}

	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getCreateTeacherId() {
		return createTeacherId;
	}

	public void setCreateTeacherId(Long createTeacherId) {
		this.createTeacherId = createTeacherId;
	}

	public java.sql.Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.sql.Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateTeacherId() {
		return updateTeacherId;
	}

	public void setUpdateTeacherId(Long updateTeacherId) {
		this.updateTeacherId = updateTeacherId;
	}

	public Long getAuditTeacherId() {
		return auditTeacherId;
	}

	public void setAuditTeacherId(Long auditTeacherId) {
		this.auditTeacherId = auditTeacherId;
	}

	public Long getPic() {
		return pic;
	}

	public void setPic(Long pic) {
		this.pic = pic;
	}

	public Long getVoiceLength() {
		return voiceLength;
	}

	public void setVoiceLength(Long voiceLength) {
		this.voiceLength = voiceLength;
	}

}