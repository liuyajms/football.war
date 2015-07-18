package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class ClassesTeacher implements Serializable {
	private Long classesId;
	private Long teacherId;
	private Long courseId;
	private String courseName;
	private String teacherName;
	private String classesName;
	private Long schoolId;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getClassesName() {
		return classesName;
	}

	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}

	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}

	public Long getClassesId() {
		return classesId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getCourseId() {
		return courseId;
	}

}