package cn.com.weixunyun.child.module.security;

import java.sql.Timestamp;

public interface SecurityService extends SecurityMapper {

	public void updateReach(Long schoolId, String term, Long studentId, Timestamp time);

	public void updateLeave(Long schoolId, String term, Long studentId, Timestamp time);

}
