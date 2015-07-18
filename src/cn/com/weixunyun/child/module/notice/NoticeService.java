package cn.com.weixunyun.child.module.notice;

import java.util.List;
import java.util.Map;

public interface NoticeService extends NoticeMapper {
	
	public int insertNotices(Long schoolId, Long userId, int del, List<Map<String, Object>> list);
}
