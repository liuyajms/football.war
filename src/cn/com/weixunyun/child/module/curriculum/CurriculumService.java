package cn.com.weixunyun.child.module.curriculum;

import java.util.List;
import java.util.Map;

public interface CurriculumService extends CurriculumMapper {
	
	public int insertMulti(Long schoolId, String term, Long classesId, Long userId, int del, List<Map<String, Object>> list);
}
