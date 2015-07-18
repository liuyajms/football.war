package cn.com.weixunyun.child.model.service;

import java.util.Map;

import cn.com.weixunyun.child.control.AbstractResource.PartField;
import cn.com.weixunyun.child.model.dao.ParentsMapper;
import cn.com.weixunyun.child.model.pojo.Parents;

public interface ParentsService extends ParentsMapper {
	
	public void insertParents(Map<String, PartField> map, Parents parents, Long schoolId);
	
	public void updateParents(Map<String, PartField> map, Parents parents, Long schoolId);
	
	public void deleteParents(Long id, Long studentId);
	
}
