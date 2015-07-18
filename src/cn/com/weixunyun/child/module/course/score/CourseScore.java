package cn.com.weixunyun.child.module.course.score;

import java.io.Serializable;
import java.util.Date;

public class CourseScore implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long studentId;
	private Long courseId;
	private Long schoolId;
	private String type;
	private Double score;
	private Date date;
	private java.sql.Timestamp createTime;
	private java.sql.Timestamp updateTime;
	private Long createTeacherId;
	private Long updateTeacherId;
	private String term;
	
	private String studentName;
	private String dateStr;//date格式化字符串
    private String typeName; //类型名称
    private String courseName;
    private String classesName;


    public String getClassesName() {
        return classesName;
    }

    public void setClassesName(String classesName) {
        this.classesName = classesName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public java.sql.Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.sql.Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Long getCreateTeacherId() {
		return createTeacherId;
	}
	public void setCreateTeacherId(Long createTeacherId) {
		this.createTeacherId = createTeacherId;
	}
	public Long getUpdateTeacherId() {
		return updateTeacherId;
	}
	public void setUpdateTeacherId(Long updateTeacherId) {
		this.updateTeacherId = updateTeacherId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
