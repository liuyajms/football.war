package cn.com.weixunyun.child.model.service;

import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.StudentMapper;

public interface StudentService extends StudentMapper {
	
	public int insertStudents(Long schoolId, Long classesId, int del, List<Map<String, Object>> list, Boolean flag, Long userId);
}
