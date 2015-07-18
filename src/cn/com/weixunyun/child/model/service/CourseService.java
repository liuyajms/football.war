package cn.com.weixunyun.child.model.service;

import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.CourseMapper;

public interface CourseService extends CourseMapper {
	
	public int insertCourses(Long schoolId, int del, List<Map<String, Object>> list);
}
