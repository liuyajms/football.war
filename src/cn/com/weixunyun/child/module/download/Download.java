package cn.com.weixunyun.child.module.download;

import java.io.Serializable;

public class Download implements Serializable {
	private Long id;
	private String name;
	private String nameFile;
	private String description;
	private String contentType;
	private Long size;
	private Long createTeacherId;
	private java.sql.Timestamp createTime;
	private Long schoolId;
	private Long classesId;

	private Long topDays;
	
	
	public Long getTopDays() {
		return topDays;
	}

	public void setTopDays(Long topDays) {
		this.topDays = topDays;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getSize() {
		return size;
	}

	public void setCreateTeacherId(Long createTeacherId) {
		this.createTeacherId = createTeacherId;
	}

	public Long getCreateTeacherId() {
		return createTeacherId;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}

	public Long getClassesId() {
		return classesId;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

}