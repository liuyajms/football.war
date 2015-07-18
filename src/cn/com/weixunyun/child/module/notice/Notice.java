package cn.com.weixunyun.child.module.notice;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notice implements Serializable {
	private Long id;
	private Long schoolId;
	private Long createUserId;
	private Timestamp createTime;
	private Long classesId;
	private String description;
	private Long auditTeacherId;
	private Timestamp auditTime;
	private Long pic;
	private Long commentsId;
	private Long positionX;
	private Long positionY;
	private Long positionName;
	private Long updateUserId;
	private Timestamp updateTime;
	private Boolean pushTeacher;
	private Boolean pushParents;
	private Long voiceLength;

	public Long getVoiceLength() {
		return voiceLength;
	}

	public void setVoiceLength(Long voiceLength) {
		this.voiceLength = voiceLength;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getClassesId() {
		return classesId;
	}

	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getAuditTeacherId() {
		return auditTeacherId;
	}

	public void setAuditTeacherId(Long auditTeacherId) {
		this.auditTeacherId = auditTeacherId;
	}

	public Timestamp getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public Long getPic() {
		return pic;
	}

	public void setPic(Long pic) {
		this.pic = pic;
	}

	public Long getCommentsId() {
		return commentsId;
	}

	public void setCommentsId(Long commentsId) {
		this.commentsId = commentsId;
	}

	public Long getPositionX() {
		return positionX;
	}

	public void setPositionX(Long positionX) {
		this.positionX = positionX;
	}

	public Long getPositionY() {
		return positionY;
	}

	public void setPositionY(Long positionY) {
		this.positionY = positionY;
	}

	public Long getPositionName() {
		return positionName;
	}

	public void setPositionName(Long positionName) {
		this.positionName = positionName;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Boolean getPushTeacher() {
		return pushTeacher;
	}

	public void setPushTeacher(Boolean pushTeacher) {
		this.pushTeacher = pushTeacher;
	}

	public Boolean getPushParents() {
		return pushParents;
	}

	public void setPushParents(Boolean pushParents) {
		this.pushParents = pushParents;
	}

}