package cn.com.weixunyun.child.module.security;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Security implements Serializable {
	private Long id;
	private String term;
	private Long studentId;
	private Date date;
	private Timestamp reachTime;
	private boolean reachOver;
	private Timestamp leaveTime;
	private boolean leaveOver;
	private Long schoolId;

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Timestamp getReachTime() {
		return reachTime;
	}

	public void setReachTime(Timestamp reachTime) {
		this.reachTime = reachTime;
	}

	public boolean isReachOver() {
		return reachOver;
	}

	public void setReachOver(boolean reachOver) {
		this.reachOver = reachOver;
	}

	public Timestamp getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(Timestamp leaveTime) {
		this.leaveTime = leaveTime;
	}

	public boolean isLeaveOver() {
		return leaveOver;
	}

	public void setLeaveOver(boolean leaveOver) {
		this.leaveOver = leaveOver;
	}

}