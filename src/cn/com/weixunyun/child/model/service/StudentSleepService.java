package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.StudentSleepMapper;
import cn.com.weixunyun.child.model.pojo.StudentSleep;

public interface StudentSleepService extends StudentSleepMapper {
	public void updateData(StudentSleep studentSleep);
}
