package cn.com.weixunyun.child.module.star;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class StarServiceImpl extends AbstractService implements StarService {

	@Override
	public StarClasses get(Long id) {
		return super.getMapper(StarMapper.class).get(id);
	}

	@Override
	public int getListCount(Long schoolId, Long classesId) {
		return super.getMapper(StarMapper.class).getListCount(schoolId, classesId);
	}

	@Override
	public List<StarClasses> getList(Long offset, Long rows, Long schoolId,
			Long classesId) {
		return super.getMapper(StarMapper.class).getList(offset, rows,
				schoolId, classesId);
	}

	@Override
	public void insert(Star star) {
		super.getMapper(StarMapper.class).insert(star);
	}

	@Override
	public void update(Star star) {
		super.getMapper(StarMapper.class).update(star);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(StarMapper.class).delete(id);
	}

	@Override
	public void updated(Long id, Long teacherId) {
		super.getMapper(StarMapper.class).updated(id, teacherId);
	}

	@Override
	public int insertStars(Long schoolId, Long classesId, Long userId, int del, List<Map<String, Object>> list) {
		StarMapper mapper = super.getMapper(StarMapper.class);
		if (del == 1) {
			mapper.deleteStars(classesId);
		}
		
		Star star = new Star();
		for (Map<String, Object> m : list) {
			star.setId(super.getMapper(SequenceMapper.class).sequence());
			star.setSchoolId(schoolId);
			star.setClassesId(classesId);
			star.setCreateTeacherId(userId);
			star.setCreateTime(new Timestamp(System.currentTimeMillis()));
			star.setUpdateTeacherId(userId);
			star.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			star.setName(m.get("name").toString());
			mapper.insert(star);
		}
		return list.size();
	}

	@Override
	public void deleteStars(Long classesId) {
		super.getMapper(StarMapper.class).deleteStars(classesId);
	}

}
