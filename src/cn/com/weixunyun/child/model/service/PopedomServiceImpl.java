package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.PopedomMapper;
import cn.com.weixunyun.child.model.pojo.Popedom;

public class PopedomServiceImpl extends AbstractService implements PopedomService {

	@Override
	public List<Popedom> select() {
		return super.getMapper(PopedomMapper.class).select();
	}

}
