package cn.com.weixunyun.child.module.homework.check;

import javax.ws.rs.core.MultivaluedMap;

public interface HomeworkCheckService extends HomeworkCheckMapper {

	void insertChecks(MultivaluedMap<String, String> form, Long schoolId, String term, Long userId);
	
}
