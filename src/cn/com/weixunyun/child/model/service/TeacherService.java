package cn.com.weixunyun.child.model.service;

import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.TeacherMapper;

public interface TeacherService extends TeacherMapper {

	public int insertTeachers(Long schoolId, int del, List<Map<String, Object>> list, Boolean flag);
}
