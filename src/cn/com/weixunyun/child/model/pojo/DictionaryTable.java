package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class DictionaryTable implements Serializable {
	private String code;
	private String name;

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