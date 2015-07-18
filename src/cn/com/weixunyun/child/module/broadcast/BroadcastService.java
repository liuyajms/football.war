package cn.com.weixunyun.child.module.broadcast;

import java.util.List;
import java.util.Map;

public interface BroadcastService extends BroadcastMapper {
	
	public int insertBroadcasts(Long schoolId, Long classesId, Long userId, String term, int del, List<Map<String, Object>> list);
}
