package cn.com.weixunyun.child.module.elective;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import cn.com.weixunyun.child.model.pojo.School;

public interface ElectiveStudentService extends ElectiveStudentMapper {
	
	public void inserElectiveStudent(MultivaluedMap<String, String> formData, School school, String term);
	
	public int updateElectiveStudents(Long schoolId, String term, Long electiveId, Long userId, List<Map<String, Object>> list);
	
}
