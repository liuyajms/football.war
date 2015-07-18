package cn.com.weixunyun.child.module.course.evaluation;

import java.util.List;
import java.util.Map;

public interface CourseEvaluationService extends CourseEvaluationMapper {
	
	public int insertCourseEvaluations(Long schoolId, Long classesId, Long courseId, String term, Long userId, int del, List<Map<String, Object>> list);
}
