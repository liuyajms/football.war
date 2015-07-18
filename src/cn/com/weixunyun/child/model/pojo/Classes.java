package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class Classes implements Serializable {
	private Long id;
	private Long schoolId;
	private Long year;
	private String num;
	private String name;
	private String remark;
	private String description;
	private Long teacherId;

	private String idDisk;
	
	private Long createTeacherId;
	private Timestamp createTime;
	private Long updateTeacherId;
	private Timestamp updateTime;
	
	public Long getCreateTeacherId() {
		return createTeacherId;
	}

	public void setCreateTeacherId(Long createTeacherId) {
		this.createTeacherId = createTeacherId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTeacherId() {
		return updateTeacherId;
	}

	public void setUpdateTeacherId(Long updateTeacherId) {
		this.updateTeacherId = updateTeacherId;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getIdDisk() {
		return idDisk;
	}

	public void setIdDisk(String idDisk) {
		this.idDisk = idDisk;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getNum() {
		return num;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public Long getTeacherId() {
		return teacherId;
	}

}