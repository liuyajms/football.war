package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class DictionaryField implements Serializable {
	private String dictionaryTableCode;
	private String code;
	private String name;

	public void setDictionaryTableCode(String dictionaryTableCode) {
		this.dictionaryTableCode = dictionaryTableCode;
	}

	public String getDictionaryTableCode() {
		return dictionaryTableCode;
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

}