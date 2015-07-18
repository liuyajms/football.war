package cn.com.weixunyun.child.module.homework;

import javax.ws.rs.core.MultivaluedMap;

public interface HomeworkService extends HomeworkMapper {

	public void insertHomeworks(MultivaluedMap<String, String> formData, Long schoolId, String term, Long userId);
}
