package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class School implements Serializable {
	private Long id;
	private int type;
	private String name;
	private String phone;
	private String email;
	private String address;
	private String description;
	private Boolean available;
	private Double vx;
	private Double vy;
	private String region;
	private java.sql.Timestamp createTime;
	private java.sql.Timestamp updateTime;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setVx(Double vx) {
		this.vx = vx;
	}

	public Double getVx() {
		return vx;
	}

	public void setVy(Double vy) {
		this.vy = vy;
	}

	public Double getVy() {
		return vy;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegion() {
		return region;
	}

}