package cn.com.weixunyun.child.module.course.classroom;

import java.util.List;
import java.util.Map;

public interface CourseClassroomService extends CourseClassroomMapper {
	
	public int insertCourseClassrooms(Long schoolId, Long classesId, Long courseId, String term, Long userId, int del, List<Map<String, Object>> list);
}
