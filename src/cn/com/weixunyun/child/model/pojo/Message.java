package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
	private Long id;
	private Long schoolId;
	private Long userIdFrom;
	private Long userIdTo;
	private String description;
	private Timestamp timeSend;
	private Timestamp timeRead;
	private Long type;
	private Boolean source;
	private Long voiceLength;

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

	public Long getUserIdFrom() {
		return userIdFrom;
	}

	public void setUserIdFrom(Long userIdFrom) {
		this.userIdFrom = userIdFrom;
	}

	public Long getUserIdTo() {
		return userIdTo;
	}

	public void setUserIdTo(Long userIdTo) {
		this.userIdTo = userIdTo;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Timestamp getTimeSend() {
		return timeSend;
	}

	public void setTimeSend(Timestamp timeSend) {
		this.timeSend = timeSend;
	}

	public Timestamp getTimeRead() {
		return timeRead;
	}

	public void setTimeRead(Timestamp timeRead) {
		this.timeRead = timeRead;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Boolean getSource() {
		return source;
	}

	public void setSource(Boolean source) {
		this.source = source;
	}

	public Long getVoiceLength() {
		return voiceLength;
	}

	public void setVoiceLength(Long voiceLength) {
		this.voiceLength = voiceLength;
	}

}