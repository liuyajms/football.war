package cn.com.weixunyun.child.model.bean;

import cn.com.weixunyun.child.model.pojo.Performance;

public class PerformanceStudent extends Performance {

	private String studentName;
	private String teacherName;

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

}
