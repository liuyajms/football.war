package cn.com.weixunyun.child.module.weibo;

import java.io.Serializable;

public class Weibo implements Serializable {
	private Long id;
	private Long schoolId;
	private Long createUserId;
	private java.sql.Timestamp createTime;
	private String description;
	private Long classesId;
	private Long auditTeacherId;
	private Long pic;
	private Long commentsId;
	private java.sql.Timestamp auditTime;
	private Double positionX;
	private Double positionY;
	private String positionName;
	private long updateUserId;
	private java.sql.Timestamp updateTime;

	public long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public java.sql.Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.sql.Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Double getPositionX() {
		return positionX;
	}

	public void setPositionX(Double positionX) {
		this.positionX = positionX;
	}

	public Double getPositionY() {
		return positionY;
	}

	public void setPositionY(Double positionY) {
		this.positionY = positionY;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
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

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}

	public Long getClassesId() {
		return classesId;
	}

	public void setAuditTeacherId(Long auditTeacherId) {
		this.auditTeacherId = auditTeacherId;
	}

	public Long getAuditTeacherId() {
		return auditTeacherId;
	}

	public void setPic(Long pic) {
		this.pic = pic;
	}

	public Long getPic() {
		return pic;
	}

	public void setCommentsId(Long commentsId) {
		this.commentsId = commentsId;
	}

	public Long getCommentsId() {
		return commentsId;
	}

	public void setAuditTime(java.sql.Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public java.sql.Timestamp getAuditTime() {
		return auditTime;
	}

}