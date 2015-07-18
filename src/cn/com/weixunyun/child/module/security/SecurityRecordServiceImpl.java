package cn.com.weixunyun.child.module.security;

import java.util.List;

import cn.com.weixunyun.child.model.service.AbstractService;

public class SecurityRecordServiceImpl extends AbstractService implements SecurityRecordService {

	@Override
	public SecurityRecord select(Long id) {
		return super.getMapper(SecurityRecordMapper.class).select(id);
	}

	@Override
	public int selectAllCount(Long schoolId, String term, Long studentId) {
		return super.getMapper(SecurityRecordMapper.class).selectAllCount(schoolId, term, studentId);
	}

	@Override
	public List<DeviceSecurityRecord> selectAll(Long offset, Long rows, Long schoolId, String term, Long studentId) {
		return super.getMapper(SecurityRecordMapper.class).selectAll(offset * rows, rows, schoolId, term, studentId);
	}

	@Override
	public void insert(SecurityRecord record) {
		super.getMapper(SecurityRecordMapper.class).insert(record);
	}

	@Override
	public void update(SecurityRecord record) {
		super.getMapper(SecurityRecordMapper.class).update(record);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(SecurityRecordMapper.class).delete(id);
	}

}
