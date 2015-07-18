package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;
import java.util.Date;

public class UserMenu implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	private Long menuId;
	private Date time;
	private Long schoolId;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}


}