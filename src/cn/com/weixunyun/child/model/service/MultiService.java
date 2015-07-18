package cn.com.weixunyun.child.model.service;

import java.util.Set;

import cn.com.weixunyun.child.model.dao.MultiMapper;

public interface MultiService extends MultiMapper {

	public void updateStudent(Long schoolId, Set<Long> idSet);

	public void updateParents(Long schoolId, Set<Long> idSet);
}
