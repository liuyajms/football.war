package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class Chat implements Serializable {
	private Long id;
	private Long schoolId;
	private Long classesId;
	private Long userId;
	private String description;
	private Timestamp time;
	private Long type;
	private Long voiceLength;

	public Long getClassesId() {
		return classesId;
	}

	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getVoiceLength() {
		return voiceLength;
	}

	public void setVoiceLength(Long voiceLength) {
		this.voiceLength = voiceLength;
	}

}