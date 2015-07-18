package cn.com.weixunyun.child.model.bean;

import cn.com.weixunyun.child.model.pojo.Classes;

public class CourseClasses extends Classes {

	private Long courseId;
	private String courseName;

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

}
