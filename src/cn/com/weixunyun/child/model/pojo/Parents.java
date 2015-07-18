package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class Parents implements Serializable {
	private Long id;
	private String mobile;
	private String password;
	private String name;
	private String gender;
	private String email;
	private Boolean pta;
	private Boolean available;
	private java.sql.Timestamp createTime;
	private java.sql.Timestamp updateTime;

	private int point;
	private Long idDisk;

	public Long getIdDisk() {
		return idDisk;
	}

	public void setIdDisk(Long idDisk) {
		this.idDisk = idDisk;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return mobile;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return gender;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setPta(Boolean pta) {
		this.pta = pta;
	}

	public Boolean getPta() {
		return pta;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Boolean getAvailable() {
		return available;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public java.sql.Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.sql.Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}