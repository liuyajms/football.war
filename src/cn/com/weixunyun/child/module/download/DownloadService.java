package cn.com.weixunyun.child.module.download;

import java.util.List;
import java.util.Map;

public interface DownloadService extends DownloadMapper {
	
	public int insertDownloads(Long schoolId, Long classesId, Long userId, int del, List<Map<String, Object>> list);
}
