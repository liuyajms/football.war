package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class Region implements Serializable {
	private String code;
	private String name;
	private String level;

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

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLevel() {
		return level;
	}

}