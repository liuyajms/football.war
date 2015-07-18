package cn.com.weixunyun.child.module.elective;


public class CourseElectiveStudent extends ElectiveStudent {

	private String electiveName;
	private String electiveDate;
	private String studentName;

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getElectiveName() {
		return electiveName;
	}

	public void setElectiveName(String electiveName) {
		this.electiveName = electiveName;
	}

	public String getElectiveDate() {
		return electiveDate;
	}

	public void setElectiveDate(String electiveDate) {
		this.electiveDate = electiveDate;
	}

}
