package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.SchoolMapper;
import cn.com.weixunyun.child.model.pojo.School;

public interface SchoolService extends SchoolMapper {
	
	public void insertSchool(School school);
}
