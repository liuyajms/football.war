package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.ConcernMapper;
import cn.com.weixunyun.child.model.pojo.Concern;

public class ConcernServiceImpl extends AbstractService implements ConcernService {

	@Override
	public List<Concern> selectConcern(Long userIdConcern, Long userIdConcerned) {
		
		return super.getMapper(ConcernMapper.class).selectConcern(userIdConcern, userIdConcerned);
	}

	@Override
	public void insert(Concern concern) {
		super.getMapper(ConcernMapper.class).insert(concern);
		
	}

	@Override
	public void delete(Long id) {
		super.getMapper(ConcernMapper.class).delete(id);
		
	}

	
}
