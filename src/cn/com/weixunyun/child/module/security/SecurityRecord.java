package cn.com.weixunyun.child.module.security;

import java.io.Serializable;
import java.sql.Timestamp;

public class SecurityRecord implements Serializable {
	private Long id;
	private Long schoolId;
	private String term;
	private String device;
	private Timestamp deviceTime;
	private String card;
	private Long userId;
	private Timestamp time;
	private Boolean reach;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Boolean getReach() {
		return reach;
	}

	public void setReach(Boolean reach) {
		this.reach = reach;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public Timestamp getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(Timestamp deviceTime) {
		this.deviceTime = deviceTime;
	}

}