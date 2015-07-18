package cn.com.weixunyun.child.module.elective;


public class TeacherElective extends Elective {

	private String teacherName;
	private Long choosedCount;
	private Boolean choosed;

	public Long getChoosedCount() {
		return choosedCount;
	}

	public void setChoosedCount(Long choosedCount) {
		this.choosedCount = choosedCount;
	}

	public Boolean getChoosed() {
		return choosed;
	}

	public void setChoosed(Boolean choosed) {
		this.choosed = choosed;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

}
