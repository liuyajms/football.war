package cn.com.weixunyun.child.model.service;

import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.CourseMapper;
import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.pojo.Course;

public class CourseServiceImpl extends AbstractService implements CourseService {

	@Override
	public int selectAllCount(Long schoolId, String keyword) {
		return super.getMapper(CourseMapper.class).selectAllCount(schoolId, keyword);
	}

	@Override
	public void insert(Course course) {
		super.getMapper(CourseMapper.class).insert(course);
		
	}

	@Override
	public void update(Course course) {
		super.getMapper(CourseMapper.class).update(course);
		
	}

	@Override
	public void delete(Long id) {
		super.getMapper(CourseMapper.class).delete(id);	
	}

	@Override
	public Course select(Long id) {
		return getMapper(CourseMapper.class).select(id);
	}

	@Override
	public List<Course> selectAll(Long schoolId, Long offset, Long rows, String keyword) {
		return getMapper(CourseMapper.class).selectAll(schoolId, offset, rows, keyword);
	}

	@Override
	public int insertCourses(Long schoolId, int del, List<Map<String, Object>> list) {
		CourseMapper mapper = super.getMapper(CourseMapper.class);
		
		if (del == 1) {
			mapper.deleteCourses(schoolId);
		}
		
		Course course = new Course();
		for (Map<String, Object> m : list) {
			course = new Course();
			course.setId(super.getMapper(SequenceMapper.class).sequence());
			course.setName(m.get("name").toString());
			course.setSchoolId(schoolId);
			mapper.insert(course);
		}
		
		return list.size();
	}

	@Override
	public void deleteCourses(Long schoolId) {
		getMapper(CourseMapper.class).deleteCourses(schoolId);
	}

}
