package cn.com.weixunyun.child.module.course.knowledge;

import java.util.List;
import java.util.Map;

public interface CourseKnowledgeService extends CourseKnowledgeMapper {
	
	public int insertCourseKnowledges(Long schoolId, Long classesId, Long courseId, String term, Long userId, int del, List<Map<String, Object>> list);
}
