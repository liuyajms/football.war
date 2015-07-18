package cn.com.weixunyun.child.module.elective;


public class StudentElectiveStudent extends Elective {

	private String electiveName;
	private String teacherName;
	private String electiveDate;
	private String score;
	private String scoreDescription;

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getScoreDescription() {
		return scoreDescription;
	}

	public void setScoreDescription(String scoreDescription) {
		this.scoreDescription = scoreDescription;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
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
