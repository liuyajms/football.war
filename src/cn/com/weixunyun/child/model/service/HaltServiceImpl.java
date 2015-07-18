package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.HaltMapper;
import cn.com.weixunyun.child.model.pojo.Halt;

public class HaltServiceImpl extends AbstractService implements HaltService {

	@Override
	public Halt select(Long id) {
		return super.getMapper(HaltMapper.class).select(id);
	}

	@Override
	public int selectAllCount() {
		return super.getMapper(HaltMapper.class).selectAllCount();
	}

	@Override
	public List<Halt> selectAll(Long offset, Long rows) {
		return super.getMapper(HaltMapper.class).selectAll(offset, rows);
	}

	@Override
	public void insert(Halt halt) {
		super.getMapper(HaltMapper.class).insert(halt);
	}

	@Override
	public void update(Halt halt) {
		super.getMapper(HaltMapper.class).update(halt);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(HaltMapper.class).delete(id);
	}

	@Override
	public void updateReaded(Long id) {
		super.getMapper(HaltMapper.class).updateReaded(id);
	}

	@Override
	public void updateAvailable(Long id, Boolean available) {
		super.getMapper(HaltMapper.class).updateAvailable(id, available);
	}

	@Override
	public Halt selectAllAvailable() {
		return super.getMapper(HaltMapper.class).selectAllAvailable();
	}

}
