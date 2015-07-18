package cn.com.weixunyun.child.module.course.classroom;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class CourseClassroomServiceImpl extends AbstractService implements CourseClassroomService {

	@Override
	public CourseClassroom select(Long id) {
		return super.getMapper(CourseClassroomMapper.class).select(id);
	}

	@Override
	public void insert(CourseClassroom classroom) {
		super.getMapper(CourseClassroomMapper.class).insert(classroom);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(CourseClassroomMapper.class).delete(id);
	}

	@Override
	public int selectCount(Long schoolId, Long classesId, Long courseId, String term) {
		return super.getMapper(CourseClassroomMapper.class).selectCount(schoolId, classesId, courseId, term);
	}

	@Override
	public List<ClassesCourseClassroom> selectAll(Long offset, Long rows, Long schoolId, Long classesId, Long courseId, String term) {
		return super.getMapper(CourseClassroomMapper.class).selectAll(offset, rows, schoolId, classesId, courseId, term);
	}

	@Override
	public void deleteCourseClassrooms(Long schoolId, Long classesId, Long courseId, String term) {
		super.getMapper(CourseClassroomMapper.class).deleteCourseClassrooms(schoolId, classesId, courseId, term);
	}

	@Override
	public int insertCourseClassrooms(Long schoolId, Long classesId, Long courseId, String term, Long userId, int del,
			List<Map<String, Object>> list) {
		CourseClassroomMapper mapper = super.getMapper(CourseClassroomMapper.class);
		
		if (del == 1) {
			mapper.deleteCourseClassrooms(schoolId, classesId, courseId, term);
		}
		
		CourseClassroom classroom = new CourseClassroom();
		for (Map<String, Object> m : list) {
			classroom.setId(super.getMapper(SequenceMapper.class).sequence());
			classroom.setClassesId(classesId);
			classroom.setCourseId(courseId);
			classroom.setSchoolId(schoolId);
			classroom.setTerm(term);
			classroom.setCreateTeacherId(userId);
			classroom.setCreateTime(new Timestamp(System.currentTimeMillis()));
			classroom.setName(m.get("name").toString());
			
			mapper.insert(classroom);
		}
		
		return list.size();
	}

}
