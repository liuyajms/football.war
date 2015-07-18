package cn.com.weixunyun.child.module.course.evaluation;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class CourseEvaluationServiceImpl extends AbstractService implements CourseEvaluationService {

	@Override
	public void insert(CourseEvaluation course) {
		super.getMapper(CourseEvaluationMapper.class).insert(course);
	}

	@Override
	public void update(CourseEvaluation course) {
		super.getMapper(CourseEvaluationMapper.class).update(course);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(CourseEvaluationMapper.class).delete(id);
	}

	@Override
	public CourseEvaluation select(Long id) {
		return super.getMapper(CourseEvaluationMapper.class).select(id);
	}

	@Override
	public int selectAllCount(Long schoolId, Long classesId, Long courseId, String term) {
		return super.getMapper(CourseEvaluationMapper.class).selectAllCount(schoolId, classesId, courseId, term);
	}

	@Override
	public List<ClassesCourseEvaluation> selectAll(Long offset, Long rows,
			Long schoolId, Long classesId, Long courseId, String term) {
		return super.getMapper(CourseEvaluationMapper.class).selectAll(offset, rows, schoolId, classesId, courseId, term);
	}

	@Override
	public List<ClassesCourseEvaluation> selectTodayTeacher(Long offset,
			Long rows, Long schoolId, Long classesId, Long courseId, String term) {
		return super.getMapper(CourseEvaluationMapper.class).selectTodayTeacher(offset, rows, schoolId, classesId, courseId, term);
	}

	@Override
	public List<ClassesCourseEvaluation> selectTodayStudent(Long offset,
			Long rows, Long schoolId, Long studentId, Long courseId, String term) {
		return super.getMapper(CourseEvaluationMapper.class).selectTodayStudent(offset, rows, schoolId, studentId, courseId, term);
	}

	@Override
	public int selectTodayTeacherCount(Long schoolId, Long classesId, Long courseId, String term) {
		return super.getMapper(CourseEvaluationMapper.class).selectTodayTeacherCount(schoolId, classesId, courseId, term);
	}

	@Override
	public int selectTodayStudentCount(Long schoolId, Long studentId, Long courseId, String term) {
		return super.getMapper(CourseEvaluationMapper.class).selectTodayStudentCount(schoolId, studentId, courseId, term);
	}

	@Override
	public void deleteCourseEvaluations(Long schoolId, Long classesId, Long courseId, String term) {
		super.getMapper(CourseEvaluationMapper.class).deleteCourseEvaluations(schoolId, classesId, courseId, term);
	}

	@Override
	public int insertCourseEvaluations(Long schoolId, Long classesId, Long courseId, String term, Long userId, int del,
			List<Map<String, Object>> list) {
		CourseEvaluationMapper mapper = super.getMapper(CourseEvaluationMapper.class);
		
		if (del == 1) {
			mapper.deleteCourseEvaluations(schoolId, classesId, courseId, term);
		}
		
		ClassesCourseEvaluation evaluation = new ClassesCourseEvaluation();
		for (Map<String, Object> m : list) {
			evaluation.setId(super.getMapper(SequenceMapper.class).sequence());
			evaluation.setStudentName(m.get("studentName").toString());
			evaluation.setCourseId(courseId);
			evaluation.setSchoolId(schoolId);
			evaluation.setTerm(term);
			evaluation.setCreateTeacherId(userId);
			evaluation.setCreateTime(new Timestamp(System.currentTimeMillis()));
			evaluation.setAccuracy(m.get("accuracy") == null?null:Double.parseDouble(m.get("accuracy").toString()));
			evaluation.setDescription(m.get("description") == null?null:m.get("description").toString());
			evaluation.setPraise(m.get("praise") == null?null:Long.parseLong(m.get("praise").toString()));
			
			mapper.insertMulti(evaluation);
		}
		
		return list.size();
	}

	@Override
	public void insertMulti(ClassesCourseEvaluation e) {
		super.getMapper(CourseEvaluationMapper.class).insertMulti(e);
	}

}
