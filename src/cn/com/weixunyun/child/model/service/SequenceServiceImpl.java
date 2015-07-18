package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.SequenceMapper;

public class SequenceServiceImpl extends AbstractService implements SequenceService {

	@Override
	public Long sequence() {
		return super.getMapper(SequenceMapper.class).sequence();
	}

}
