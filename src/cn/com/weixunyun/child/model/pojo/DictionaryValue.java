package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class DictionaryValue implements Serializable {
	private Long schoolId;
	private String dictionaryTableCode;
	private String dictionaryFieldCode;
	private String code;
	private String name;
	private Long ord;

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setDictionaryTableCode(String dictionaryTableCode) {
		this.dictionaryTableCode = dictionaryTableCode;
	}

	public String getDictionaryTableCode() {
		return dictionaryTableCode;
	}

	public void setDictionaryFieldCode(String dictionaryFieldCode) {
		this.dictionaryFieldCode = dictionaryFieldCode;
	}

	public String getDictionaryFieldCode() {
		return dictionaryFieldCode;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setOrd(Long ord) {
		this.ord = ord;
	}

	public Long getOrd() {
		return ord;
	}

}