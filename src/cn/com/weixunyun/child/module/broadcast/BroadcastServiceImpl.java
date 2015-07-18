package cn.com.weixunyun.child.module.broadcast;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;
import cn.com.weixunyun.child.util.sensitive.SensitivewordFilter;

public class BroadcastServiceImpl extends AbstractService implements BroadcastService {

	@Override
	public void insert(Broadcast broadcast) {
		super.getMapper(BroadcastMapper.class).insert(broadcast);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(BroadcastMapper.class).delete(id);
	}

	@Override
	public void update(Broadcast broadcast) {
		super.getMapper(BroadcastMapper.class).update(broadcast);
	}

	@Override
	public ClassesBroadcast get(Long id) {
		return super.getMapper(BroadcastMapper.class).get(id);
	}

	@Override
	public List<ClassesBroadcast> getSchoolBroadcastList(Long offset, Long rows, Long schoolId, String keyword, String term) {
		return super.getMapper(BroadcastMapper.class).getSchoolBroadcastList(offset, rows, schoolId, keyword, term);
	}

	@Override
	public int getSchoolBroadcastListCount(Long schoolId, String keyword, String term) {
		return super.getMapper(BroadcastMapper.class).getSchoolBroadcastListCount(schoolId, keyword, term);
	}

	@Override
	public List<ClassesBroadcast> getClassesBroadcast(Long offset, Long rows, Long schoolId, Long keyword, String term) {
		return super.getMapper(BroadcastMapper.class).getClassesBroadcast(offset, rows, schoolId, keyword, term);
	}

	@Override
	public int getClassesBroadcastCount(Long schoolId, Long keyword, String term) {
		return super.getMapper(BroadcastMapper.class).getClassesBroadcastCount(schoolId, keyword, term);
	}

	@Override
	public void updateImage(Long pic, Long id) {
		super.getMapper(BroadcastMapper.class).updateImage(pic, id);
	}

	@Override
	public List<ClassesBroadcast> getList(Long offset, Long rows, Long schoolId, int grade, Long classesId, String term) {
		return super.getMapper(BroadcastMapper.class).getList(offset, rows, schoolId, grade, classesId, term);
	}

	@Override
	public int getListCount(Long schoolId, int grade, Long classesId, Long audit, String term) {
		return super.getMapper(BroadcastMapper.class).getListCount(schoolId, grade, classesId, audit, term);
	}

	@Override
	public List<ClassesBroadcast> getGradeBroadcastList(Long offset, Long rows, Long schoolId, String keyword, String term) {
		return super.getMapper(BroadcastMapper.class).getGradeBroadcastList(offset, rows, schoolId, keyword, term);
	}

	@Override
	public int getGradeBroadcastListCount(Long schoolId, String keyword, String term) {
		return super.getMapper(BroadcastMapper.class).getGradeBroadcastListCount(schoolId, keyword, term);
	}

	@Override
	public void deleteBroadcastGrades(Long schoolId, String term) {
		super.getMapper(BroadcastMapper.class).deleteBroadcastGrades(schoolId, term);
	}

	@Override
	public int insertBroadcasts(Long schoolId, Long classesId, Long userId, String term, int del, List<Map<String, Object>> list) {
		BroadcastMapper mapper = super.getMapper(BroadcastMapper.class);
		if (del == 1) {
			if (classesId == null) {
				mapper.deleteBroadcastGrades(schoolId, term);
			} else {
				mapper.deleteBroadcastClassess(classesId, term);
			}
		}
		
		Broadcast broadcast = new Broadcast();
		for (Map<String, Object> m : list) {
			broadcast.setId(super.getMapper(SequenceMapper.class).sequence());
			broadcast.setSchoolId(schoolId);
			broadcast.setTerm(term);
			broadcast.setClassesId(classesId);
			broadcast.setCreateTeacherId(userId);
			broadcast.setCreateTime(new Timestamp(System.currentTimeMillis()));
			broadcast.setUpdateTeacherId(userId);
			broadcast.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			broadcast.setComment(true);
			if (classesId == null) {
				broadcast.setGrade(m.get("grade")==null?null:gradeParser(m.get("grade").toString()));
			}
			broadcast.setTitle(m.get("title").toString());
//			broadcast.setTitle(SensitivewordFilter.replaceSensitiveWord(m.get("title").toString()));
			
			broadcast.setDescription(m.get("description")==null?null:m.get("description").toString());
//			broadcast.setDescription(m.get("description")==null?null:SensitivewordFilter.replaceSensitiveWord(m.get("description").toString()));
			mapper.insert(broadcast);
		}
		return list.size();
	}

	public int gradeParser(String str) {
		str = str.substring(0, str.length()-2);
		String attr[] = str.split("、");
		int attr2[] = {0, 0, 0, 0, 0, 0, 0, 0};
		
		for (int i = 0; i < attr.length; i++) {
			attr2[Integer.parseInt(attr[i])-1] = 1;
		}
		
		int grade = 0;
		for (int i = 0; i < 8; i++) {
			if (attr2[i] == 1) {
				grade += (1 << i);
			}
		}
		
		return grade;
	}

	@Override
	public void deleteBroadcastClassess(Long classesId, String term) {
		super.getMapper(BroadcastMapper.class).deleteBroadcastClassess(classesId, term);
	}
}
