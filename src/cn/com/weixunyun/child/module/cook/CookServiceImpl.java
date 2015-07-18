package cn.com.weixunyun.child.module.cook;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class CookServiceImpl extends AbstractService implements CookService {

	@Override
	public Cook get(int id) {
		return super.getMapper(CookMapper.class).get(id);
	}

	@Override
	public int getListCount(Long schoolId, String con, String term) {
		return super.getMapper(CookMapper.class).getListCount(schoolId, con, term);
	}

	@Override
	public List<Cook> getList(int offset, int rows, String con, Long schoolId, String term) {
		return super.getMapper(CookMapper.class).getList(offset * rows, rows, con, schoolId, term);
	}

	@Override
	public void insert(Cook cook) {
		super.getMapper(CookMapper.class).insert(cook);
	}

	@Override
	public void update(Cook cook) {
		super.getMapper(CookMapper.class).update(cook);
	}

	@Override
	public void delete(int id) {
		super.getMapper(CookMapper.class).delete(id);
	}

	@Override
	public int insertCooks(Long schoolId, String term, Long userId, int del, List<Map<String, Object>> list) {
		CookMapper mapper = super.getMapper(CookMapper.class);
		if (del == 1) {
			mapper.deleteCooks(schoolId, term);
		}
		
		Cook cook = new Cook();
		for (Map<String, Object> m : list) {
			cook.setId(super.getMapper(SequenceMapper.class).sequence());
			cook.setSchoolId(schoolId);
			cook.setTerm(term);
			cook.setCreateTeacherId(userId);
			cook.setCreateTime(new Timestamp(System.currentTimeMillis()));
			cook.setUpdateTeacherId(userId);
			cook.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			cook.setName(m.get("name").toString());
			mapper.insert(cook);
		}
		return list.size();
	}

	@Override
	public void deleteCooks(Long schoolId, String term) {
		super.getMapper(CookMapper.class).deleteCooks(schoolId, term);
	}

}
