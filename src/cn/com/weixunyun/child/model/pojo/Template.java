package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class Template implements Serializable {
	private String code;
	private String description;
	private Long schoolId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

}