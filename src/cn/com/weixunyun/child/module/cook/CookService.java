package cn.com.weixunyun.child.module.cook;

import java.util.List;
import java.util.Map;

public interface CookService extends CookMapper {
	
	public int insertCooks(Long schoolId, String term, Long userId, int del, List<Map<String, Object>> list);
}
