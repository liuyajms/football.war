package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.GlobalMapper;
import cn.com.weixunyun.child.model.pojo.Global;

public class GlobalServiceImpl extends AbstractService implements GlobalService {

	@Override
	public Global select(Long schoolId, String codeParent, String code) {
		return super.getMapper(GlobalMapper.class).select(schoolId, codeParent, code);
	}

	@Override
	public int selectAllCount(Long schoolId) {
		return super.getMapper(GlobalMapper.class).selectAllCount(schoolId);
	}

	@Override
	public List<Global> selectAll(Long schoolId) {
		return super.getMapper(GlobalMapper.class).selectAll(schoolId);
	}

	@Override
	public void insert(Global global) {
		super.getMapper(GlobalMapper.class).insert(global);
	}

	@Override
	public void update(Global global) {
		super.getMapper(GlobalMapper.class).update(global);
	}

	@Override
	public void delete(Global global) {
		super.getMapper(GlobalMapper.class).delete(global);
	}

	@Override
	public List<Global> getList() {
		return super.getMapper(GlobalMapper.class).getList();
	}

}
