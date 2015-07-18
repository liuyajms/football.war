package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class Concern implements Serializable {
	private Long userIdConcern;
	private Long userIdConcerned;
	private Long id;
	private Long schoolId;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public void setUserIdConcern(Long userIdConcern) {
		this.userIdConcern = userIdConcern;
	}

	public Long getUserIdConcern() {
		return userIdConcern;
	}

	public void setUserIdConcerned(Long userIdConcerned) {
		this.userIdConcerned = userIdConcerned;
	}

	public Long getUserIdConcerned() {
		return userIdConcerned;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}