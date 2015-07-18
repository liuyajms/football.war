package cn.com.weixunyun.child.module.homework;

import java.io.Serializable;

public class HomeworkComplete implements Serializable {
	private Long homeworkId;
	private Long childId;
	private Boolean complete;
	private String beginTime;
	private String endTime;
	private String creator;
	private String updator;
	private java.sql.Timestamp updateTime;
	private java.sql.Timestamp createTime;
	private Long schoolId;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public void setHomeworkId(Long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public Long getHomeworkId() {
		return homeworkId;
	}

	public void setChildId(Long childId) {
		this.childId = childId;
	}

	public Long getChildId() {
		return childId;
	}

	public void setComplete(Boolean complete) {
		this.complete = complete;
	}

	public Boolean getComplete() {
		return complete;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreator() {
		return creator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdateTime(java.sql.Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public java.sql.Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

}