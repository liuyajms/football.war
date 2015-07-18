package cn.com.weixunyun.child.module.personal.journal;

import java.io.Serializable;
import java.util.Date;

public class Journal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long schoolId;
	private String description;
	private Long pic;
	private Long voiceLength;
	private Long createUserId;
	private Date createTime;
	private Long updateUserId;
	private Date updateTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPic() {
		return pic;
	}

	public void setPic(Long pic) {
		this.pic = pic;
	}

	public Long getVoiceLength() {
		return voiceLength;
	}

	public void setVoiceLength(Long voiceLength) {
		this.voiceLength = voiceLength;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
