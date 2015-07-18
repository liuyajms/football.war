package cn.com.weixunyun.child.model.service;

import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SensitiveMapper;


/**
 * Created by Administrator on 14-9-16.
 */
public interface SensitiveService extends SensitiveMapper {
	public int insertSensitives(Long schoolId, int del, List<Map<String, Object>> list);
}
