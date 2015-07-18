package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.StudentParentsMapper;
import cn.com.weixunyun.child.model.pojo.StudentParents;

public class StudentParentsServiceImpl extends AbstractService implements StudentParentsService {

	@Override
	public StudentParents select(Long studentId, Long parentsId) {
		return super.getMapper(StudentParentsMapper.class).select(studentId, parentsId);
	}

	@Override
	public void insert(StudentParents parents) {
		super.getMapper(StudentParentsMapper.class).insert(parents);
	}

	@Override
	public void update(StudentParents parents) {
		super.getMapper(StudentParentsMapper.class).update(parents);
	}

	@Override
	public void delete(Long studentId, Long parentsId) {
		super.getMapper(StudentParentsMapper.class).delete(studentId, parentsId);
	}

	@Override
	public void updateUsername(Long parentsId, String username) {
		super.getMapper(StudentParentsMapper.class).updateUsername(parentsId, username);
	}

}
