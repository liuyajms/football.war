package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.bean.PerformanceStudent;
import cn.com.weixunyun.child.model.dao.PerformanceMapper;
import cn.com.weixunyun.child.model.pojo.Performance;

public class PerformanceServiceImpl extends AbstractService implements PerformanceService {

	@Override
	public Performance select(Long id) {
		return super.getMapper(PerformanceMapper.class).select(id);
	}

	@Override
	public int selectAllCount(Long schoolId, String con) {
		return super.getMapper(PerformanceMapper.class).selectAllCount(schoolId, con);
	}

	@Override
	public List<PerformanceStudent> selectAll(Long offset, Long rows, String con, Long schoolId) {
		return super.getMapper(PerformanceMapper.class).selectAll(offset, rows, con, schoolId);
	}

	@Override
	public void insert(Performance performance) {
		super.getMapper(PerformanceMapper.class).insert(performance);
	}

	@Override
	public void update(Performance performance) {
		super.getMapper(PerformanceMapper.class).update(performance);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(PerformanceMapper.class).delete(id);
	}

}
