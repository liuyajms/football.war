package cn.com.weixunyun.child.module.homework;

import java.sql.Timestamp;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class HomeworkServiceImpl extends AbstractService implements HomeworkService {

	@Override
	public Homework select(Long id) {
		return super.getMapper(HomeworkMapper.class).select(id);
	}

	@Override
	public void insert(Homework homework) {
		super.getMapper(HomeworkMapper.class).insert(homework);
	}

	@Override
	public void update(Homework homework) {
		super.getMapper(HomeworkMapper.class).update(homework);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(HomeworkMapper.class).delete(id);
	}

	@Override
	public List<Homework> getOtherList(Long offset, Long rows, Long schoolId,
			Long classesId, Long courseId, String term, String keyword, String queryDate) {
		return super.getMapper(HomeworkMapper.class).getOtherList(offset, rows,
				schoolId, classesId, courseId, term, keyword, queryDate);
	}

	@Override
	public List<Homework> getList(Long offset, Long rows, Long schoolId,
			Long classesId, Long courseId, Long teacherId, String term, String keyword,
			String queryDate) {
		return super.getMapper(HomeworkMapper.class).getList(offset,
				rows, schoolId, classesId, courseId, teacherId, term, keyword, queryDate);
	}

	@Override
	public int getCount(Long schoolId, Long teacherId, Long classesId, Long courseId,
			String term, String keyword, String queryDate) {
		return super.getMapper(HomeworkMapper.class).getCount(
				schoolId, teacherId, classesId, courseId, term, keyword, queryDate);
	}

	@Override
	public int getOtherCount(Long schoolId, Long classesId, Long courseId, String term,
			String keyword, String queryDate) {
		return super.getMapper(HomeworkMapper.class).getOtherCount(schoolId,
				classesId, courseId, term, keyword, queryDate);
	}

	@Override
	public List<StudentHomework> getStudentHomeworkList(Long offset, Long rows,
			Long schoolId, Long classesId, Long studentId, String term, String date) {
		return super.getMapper(HomeworkMapper.class).getStudentHomeworkList(offset, 
				rows, schoolId, classesId, studentId, term, date);
	}

	@Override
	public int getStudentHomeworkListCount(Long schoolId, Long classesId, Long studentId, String term, String date) {
		return super.getMapper(HomeworkMapper.class).getStudentHomeworkListCount(schoolId, classesId, studentId, term, date);
	}

	@Override
	public void insertHomeworks(MultivaluedMap<String, String> formData, Long schoolId, String term, Long userId) {
		HomeworkMapper mapper = super.getMapper(HomeworkMapper.class);
		
		List<String> classesListStr = formData.get("classesId");
		List<String> courseListStr = formData.get("courseId");
		List<String> descListStr = formData.get("description");
		
		int total = classesListStr.size();
		
		for (int i = 0; i < total; i++) {
			Homework e = new Homework();
			e.setId(super.getMapper(SequenceMapper.class).sequence());
			e.setClassesId(Long.parseLong(classesListStr.get(i)));
			e.setCourseId(Long.parseLong(courseListStr.get(i)));
			e.setSchoolId(schoolId);
			e.setTerm(term);
			e.setDescription(descListStr.get(i));
			e.setCreateTeacherId(userId);
			e.setCreateTime(new Timestamp(System.currentTimeMillis()));
			e.setUpdateTeacherId(userId);
			e.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			if (descListStr.get(i) != null && !"".equals(descListStr.get(i))) {
				mapper.insert(e);
			}
		}
	}

	@Override
	public List<StudentHomework> getHomeworkList(Long page, Long rows, Long schoolId, Long teacherId,
			String term, String date) {
		return super.getMapper(HomeworkMapper.class).getHomeworkList(page, rows, schoolId, teacherId, term, date);
	}

}
