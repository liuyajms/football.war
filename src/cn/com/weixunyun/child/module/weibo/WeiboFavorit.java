package cn.com.weixunyun.child.module.weibo;

import java.io.Serializable;
import java.sql.Timestamp;

public class WeiboFavorit implements Serializable {
	private Long userId;
	private Long weiboId;
	private Timestamp time;
	private Long schoolId;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
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

	public void setWeiboId(Long weiboId) {
		this.weiboId = weiboId;
	}

	public Long getWeiboId() {
		return weiboId;
	}

}