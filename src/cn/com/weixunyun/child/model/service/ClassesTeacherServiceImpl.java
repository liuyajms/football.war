package cn.com.weixunyun.child.model.service;

import java.util.List;
import java.util.Map;

import org.apache.wink.client.RestClient;

import cn.com.weixunyun.child.PropertiesListener;
import cn.com.weixunyun.child.model.dao.ClassesMapper;
import cn.com.weixunyun.child.model.dao.ClassesTeacherMapper;
import cn.com.weixunyun.child.model.pojo.Classes;
import cn.com.weixunyun.child.model.pojo.ClassesTeacher;

public class ClassesTeacherServiceImpl extends AbstractService implements ClassesTeacherService {

	@Override
	public void insert(ClassesTeacher classesTeacher) {
		super.getMapper(ClassesTeacherMapper.class).insert(classesTeacher);
	}

	@Override
	public int selectAllCount(Long classesId, String keyword, Long schoolId) {
		return getMapper(ClassesTeacherMapper.class).selectAllCount(classesId, keyword, schoolId);
	}

	@Override
	public List<ClassesTeacher> selectAll(Long classesId, Long offset, Long rows, String keyword, Long schoolId) {
		return getMapper(ClassesTeacherMapper.class).selectAll(classesId, offset, rows, keyword, schoolId);
	}

	@Override
	public void delete(Long classesId, Long courseId) {
		super.getMapper(ClassesTeacherMapper.class).delete(classesId, courseId);
		
	}

	@Override
	public List<Map<String, ?>> selectResidue(Long schoolId, Long classesId) {
		return super.getMapper(ClassesTeacherMapper.class).selectResidue(schoolId, classesId);
	}

	@Override
	public int selectTeacherClassesCount(Long classesId, Long teacherId, Long schoolId) {
		return super.getMapper(ClassesTeacherMapper.class).selectTeacherClassesCount(classesId, teacherId, schoolId);
	}

	@Override
	public List<ClassesTeacher> selectTeacherClasses(Long classesId, Long offset, Long rows, Long teacherId, Long schoolId) {
		return super.getMapper(ClassesTeacherMapper.class).selectTeacherClasses(classesId, offset, rows, teacherId, schoolId);
	}

	@Override
	public ClassesTeacher select(Long classesId, Long courseId) {
		return super.getMapper(ClassesTeacherMapper.class).select(classesId, courseId);
	}

	@Override
	public int insertClassesTeachers(Long schoolId, Long classesId, int del, List<Map<String, Object>> list, Boolean flag, Long userId) {
		ClassesTeacherMapper mapper = super.getMapper(ClassesTeacherMapper.class);
		String simiyunUrl = PropertiesListener.getProperty("simiyun.url", null);
		Classes cls = super.getMapper(ClassesMapper.class).select(classesId);
		long ownId = cls.getCreateTeacherId() == null ? -1 : cls.getCreateTeacherId().longValue();
		
		if (del == 1) {
			mapper.deleteMulti(schoolId, classesId);
		}
		
		String uNameStr = null;
		Long teacherId = null;
		ClassesTeacher ct = new ClassesTeacher();
		for (Map<String, Object> m : list) {
			ct = new ClassesTeacher();
			ct.setSchoolId(schoolId);
			ct.setClassesId(classesId);
			
			if (m.get("teacherId") != null && !"".equals(m.get("teacherId"))) {
				teacherId = Long.parseLong(m.get("teacherId").toString());
				ct.setTeacherId(teacherId);
			}
			if (m.get("courseId") != null && !"".equals(m.get("courseId"))) {
				ct.setCourseId(Long.parseLong(m.get("courseId").toString()));
			}
			mapper.insert(ct);
			
			//不是群组拥有者，否则会改变群组拥有者的角色（变成管理员）
			if (ownId != teacherId.longValue()) {
				uNameStr = uNameStr + "," + teacherId;
			}
		}
		
		if (flag) {
			RestClient client = new RestClient();
			String path = simiyunUrl + "/groupsEdu/users?user_name=" + uNameStr.substring(5)+ "&username=" + userId
					+ "&type=inviteUser" + "&group_name=" + classesId + "&role_id=5";
			System.out.println("-------path: " + path);
			System.out.println();
			org.apache.wink.client.Resource resource = client.resource(path);
			resource.get();
		}
		
		return list.size();
	}

	@Override
	public void deleteMulti(Long schoolId, Long classesId) {
		super.getMapper(ClassesTeacherMapper.class).deleteMulti(schoolId, classesId);
	}

	@Override
	public int selectTeacherCount(Long classesId, Long teacherId) {
		return super.getMapper(ClassesTeacherMapper.class).selectTeacherCount(classesId, teacherId);
	}

}
