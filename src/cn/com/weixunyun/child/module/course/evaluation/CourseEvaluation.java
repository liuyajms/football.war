package cn.com.weixunyun.child.module.course.evaluation;

import java.io.Serializable;
import java.sql.Timestamp;

public class CourseEvaluation implements Serializable {
	private Long id;
	private Long schoolId;
	private Long courseId;
	private Long studentId;
	private Long praise;
	private Double accuracy;
	private Long createTeacherId;
	private Timestamp createTime;
	private Long updateTeacherId;
	private Timestamp updateTime;
	private String description;
	private String term;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getPraise() {
		return praise;
	}

	public void setPraise(Long praise) {
		this.praise = praise;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getCreateTeacherId() {
		return createTeacherId;
	}

	public void setCreateTeacherId(Long createTeacherId) {
		this.createTeacherId = createTeacherId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTeacherId() {
		return updateTeacherId;
	}

	public void setUpdateTeacherId(Long updateTeacherId) {
		this.updateTeacherId = updateTeacherId;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
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

}