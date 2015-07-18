package cn.com.weixunyun.child.module.course.knowledge;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class CourseKnowledgeServiceImpl extends AbstractService implements CourseKnowledgeService {

	@Override
	public void insert(CourseKnowledge course) {
		super.getMapper(CourseKnowledgeMapper.class).insert(course);
	}

	@Override
	public void update(CourseKnowledge course) {
		super.getMapper(CourseKnowledgeMapper.class).update(course);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(CourseKnowledgeMapper.class).delete(id);
	}

	@Override
	public CourseKnowledge select(Long id) {
		return super.getMapper(CourseKnowledgeMapper.class).select(id);
	}

	@Override
	public int selectAllCount(Long schoolId, Long classesId, Long courseId, String term) {
		return super.getMapper(CourseKnowledgeMapper.class).selectAllCount(schoolId, classesId, courseId, term);
	}

	@Override
	public List<ClassesCourseKnowledge> selectAll(Long offset, Long rows,
			Long schoolId, Long classesId, Long courseId, String term) {
		return super.getMapper(CourseKnowledgeMapper.class).selectAll(offset, rows, schoolId, classesId, courseId, term);
	}

	@Override
	public List<ClassesCourseKnowledge> selectExportList(Long schoolId, Long classesId, Long courseId, String term) {
		return super.getMapper(CourseKnowledgeMapper.class).selectExportList(schoolId, classesId, courseId, term);
	}

	@Override
	public int insertCourseKnowledges(Long schoolId, Long classesId, Long courseId, String term, Long userId, int del, List<Map<String, Object>> list) {
		CourseKnowledgeMapper mapper = super.getMapper(CourseKnowledgeMapper.class);
		
		if (del == 1) {
			mapper.deleteCourseKnowledges(schoolId, classesId, courseId, term);
		}
		
		CourseKnowledge knowledge = new CourseKnowledge();
		for (Map<String, Object> m : list) {
			knowledge.setId(super.getMapper(SequenceMapper.class).sequence());
			knowledge.setClassesId(classesId);
			knowledge.setCourseId(courseId);
			knowledge.setSchoolId(schoolId);
			knowledge.setTerm(term);
			knowledge.setCreateTeacherId(userId);
			knowledge.setCreateTime(new Timestamp(System.currentTimeMillis()));
			knowledge.setName(m.get("name").toString());
			
			mapper.insert(knowledge);
		}
		
		return list.size();
	}

	@Override
	public void deleteCourseKnowledges(Long schoolId, Long classesId, Long courseId, String term) {
		super.getMapper(CourseKnowledgeMapper.class).deleteCourseKnowledges(schoolId, classesId, courseId, term);
	}

}
