package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class Student implements Serializable {
	private Long id;
	private Long schoolId;
	private Long classesId;
	private String name;
	private String gender;
	private java.sql.Date birthday;
	private String code;
	private String address;
	private String description;
	private java.sql.Timestamp createTime;
	private java.sql.Timestamp updateTime;
	private String card;
	private Boolean cardAvailable;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Boolean getCardAvailable() {
		return cardAvailable;
	}

	public void setCardAvailable(Boolean cardAvailable) {
		this.cardAvailable = cardAvailable;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}

	public Long getClassesId() {
		return classesId;
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

	public void setBirthday(java.sql.Date birthday) {
		this.birthday = birthday;
	}

	public java.sql.Date getBirthday() {
		return birthday;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
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