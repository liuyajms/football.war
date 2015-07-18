package cn.com.weixunyun.child.module.elective;

import java.io.Serializable;
import java.sql.Timestamp;

public class ElectiveStudent implements Serializable {
	private Long id;
	private String term;
	private Long schoolId;
	private Long studentId;
	private Long electiveId;
	private Timestamp time;
	private String score;
	private Timestamp scoreTime;
	private String scoreDescription;

	public Long getElectiveId() {
		return electiveId;
	}

	public void setElectiveId(Long electiveId) {
		this.electiveId = electiveId;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Timestamp getScoreTime() {
		return scoreTime;
	}

	public void setScoreTime(Timestamp scoreTime) {
		this.scoreTime = scoreTime;
	}

	public String getScoreDescription() {
		return scoreDescription;
	}

	public void setScoreDescription(String scoreDescription) {
		this.scoreDescription = scoreDescription;
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

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

}