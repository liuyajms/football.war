package cn.com.weixunyun.child.module.weibo;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WeiboService extends WeiboMapper {
	
	public int insertWeibos(Long schoolId, Long classesId, Long userId, String term, int del, List<Map<String, Object>> list);

}
