package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.StudentParents;
import cn.com.weixunyun.child.model.pojo.Log;
import cn.com.weixunyun.child.model.pojo.Parents;

public interface LogMapper {

	// @SelectProvider(type = LogMapperProvider.class, method = "getAllParents")
	// public List<StudentParents> selectAll(@Param(value = "offset") Long
	// offset, @Param(value = "rows") Long rows,
	// @Param(value = "schoolId") Long schoolId, @Param(value = "studentId")
	// Long studentId,
	// @Param(value = "keyword") String keyword);

	@Insert("insert into log (school_id, user_id, time, url, method) values (#{schoolId}, #{userId}, #{time}, #{url}, #{method})")
	public void insert(Log log);

}
