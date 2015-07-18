package cn.com.weixunyun.child.module.news;

import java.util.List;
import java.util.Map;

public interface NewsService extends NewsMapper {
	
	public int insertNews(Long schoolId, Long type, Long userId, int del, List<Map<String, Object>> list);
}
