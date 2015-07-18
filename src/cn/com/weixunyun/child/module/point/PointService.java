package cn.com.weixunyun.child.module.point;

import javax.ws.rs.core.MultivaluedMap;

public interface PointService extends PointMapper {
	
	public void insertPoint(Long schoolId, Long userId, String module, Boolean plus);
	public void exchange(Long teacherId, MultivaluedMap<String, String> form);
	
}
