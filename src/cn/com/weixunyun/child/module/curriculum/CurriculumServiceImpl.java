package cn.com.weixunyun.child.module.curriculum;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class CurriculumServiceImpl extends AbstractService implements CurriculumService {

	@Override
	public Curriculum select(Long id) {
		return super.getMapper(CurriculumMapper.class).select(id);
	}

	@Override
	public void insert(Curriculum curriculum) {
		super.getMapper(CurriculumMapper.class).insert(curriculum);
	}

	@Override
	public void update(Curriculum curriculum) {
		super.getMapper(CurriculumMapper.class).update(curriculum);
	}

	@Override
	public void delete(int id) {
		super.getMapper(CurriculumMapper.class).delete(id);
	}

	@Override
	public int selectAllCount(String type, Long classesId, Long schoolId, String term) {
		return super.getMapper(CurriculumMapper.class).selectAllCount(type, classesId, schoolId, term);
	}

	@Override
	public List<ClassesCurriculum> selectAll(int offset, int rows, String type,
			Long classesId, Long schoolId, String term) {
		return super.getMapper(CurriculumMapper.class).selectAll(offset, rows, type, classesId, schoolId, term);
	}

	@Override
	public int insertMulti(Long schoolId, String term, Long classesId, Long userId, int del, List<Map<String, Object>> list) {
		CurriculumMapper mapper = super.getMapper(CurriculumMapper.class);
		if (del == 1) {
			mapper.deleteMulti(classesId, schoolId, term);
		}
		
		Curriculum curriculum = new Curriculum();
		for (Map<String, Object> m : list) {
			curriculum.setId(super.getMapper(SequenceMapper.class).sequence());
			curriculum.setSchoolId(schoolId);
			curriculum.setClassesId(classesId);
			curriculum.setTerm(term);
			curriculum.setCreateTeacherId(userId);
			curriculum.setCreateTime(new Timestamp(System.currentTimeMillis()));
			curriculum.setType(m.get("type")==null?null:"正常课表".equals(m.get("type").toString())==true?"0":"单周课表".equals(m.get("type").toString())==true?"1":"2");
			mapper.insert(curriculum);
		}
		return list.size();
	}

	@Override
	public void deleteMulti(Long classesId, Long schoolId, String term) {
		super.getMapper(CurriculumMapper.class).deleteMulti(classesId, schoolId, term);
	}
	

	@Override
	public void updateImage(Long id) {
		super.getMapper(CurriculumMapper.class).updateImage(id);
	}
}
