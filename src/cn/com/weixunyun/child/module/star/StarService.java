package cn.com.weixunyun.child.module.star;

import java.util.List;
import java.util.Map;

public interface StarService extends StarMapper {
	
	public int insertStars(Long schoolId, Long classesId, Long userId, int del, List<Map<String, Object>> list);
}
