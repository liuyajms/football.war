package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class Global implements Serializable {
	private String codeParent;
	private String code;
	private String value;
	private Long schoolId;

	public void setCodeParent(String codeParent) {
		this.codeParent = codeParent;
	}

	public String getCodeParent() {
		return codeParent;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

}