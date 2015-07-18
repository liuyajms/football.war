package cn.com.weixunyun.child.model.service;

import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.ClassesMapper;

public interface ClassesService extends ClassesMapper {
	
	public int insertClasses(Long schoolId, int del, List<Map<String, Object>> list, Boolean flag, Long userId);
	
}
