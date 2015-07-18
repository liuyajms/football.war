package cn.com.weixunyun.child.module.course.score;


import java.util.List;
import java.util.Map;

public interface CourseScoreService extends CourseScoreMapper{

    int insertMulti(Long schoolId, Long classesId, Long teacherId, String term, List<Map<String, Object>> list);
}
