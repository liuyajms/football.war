package cn.com.weixunyun.child.module.elective;

import java.util.List;
import java.util.Map;

public interface ElectiveService extends ElectiveMapper {
	
	public int insertElectives(Long schoolId, String term, Long userId, int del, List<Map<String, Object>> list);
}
