package cn.com.weixunyun.child.module.broadcast;

import java.sql.Timestamp;

public class BroadcastComment {
	private Long id;
	private String description;
	private Timestamp createTime;
	private Long createUserId;
	private Long broadcastId;
	private Long schoolId;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getBroadcastId() {
		return broadcastId;
	}

	public void setBroadcastId(Long broadcastId) {
		this.broadcastId = broadcastId;
	}

}
