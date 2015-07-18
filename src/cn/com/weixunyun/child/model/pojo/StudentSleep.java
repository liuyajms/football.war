package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class StudentSleep implements Serializable {
	private Long studentId;
	private String sleepTime;
	private String date;

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}

	public String getSleepTime() {
		return sleepTime;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

}