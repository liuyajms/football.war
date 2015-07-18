package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.StudentSleepMapper;
import cn.com.weixunyun.child.model.pojo.StudentSleep;

public class StudentSleepServiceImpl extends AbstractService implements StudentSleepService {

	@Override
	public void insert(StudentSleep studentSleep) {
		super.getMapper(StudentSleepMapper.class).insert(studentSleep);
		
	}

	@Override
	public void delete(StudentSleep studentSleep) {
		super.getMapper(StudentSleepMapper.class).delete(studentSleep);
		
	}

	@Override
	public void updateData(StudentSleep studentSleep) {
		delete(studentSleep);
		insert(studentSleep);
	}

}
