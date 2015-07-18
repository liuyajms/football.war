package cn.com.weixunyun.child.model.service;

import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.DictionaryTeacher;
import cn.com.weixunyun.child.model.bean.TeacherClasses;
import cn.com.weixunyun.child.model.dao.ClassesMapper;
import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.pojo.Classes;

public class ClassesServiceImpl extends AbstractService implements ClassesService {

	@Override
	public Classes select(Long id) {
		return super.getMapper(ClassesMapper.class).select(id);
	}

	@Override
	public int selectAllCount(Long schoolId) {
		return super.getMapper(ClassesMapper.class).selectAllCount(schoolId);
	}

	@Override
	public List<Classes> selectAll(Long schoolId) {
		return super.getMapper(ClassesMapper.class).selectAll(schoolId);
	}

	@Override
	public void insert(Classes classes) {
		super.getMapper(ClassesMapper.class).insert(classes);
	}

	@Override
	public void update(Classes classes) {
		super.getMapper(ClassesMapper.class).update(classes);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(ClassesMapper.class).delete(id);
	}

	@Override
	public List<Classes> selectAllClasses(Long schoolId) {
		return super.getMapper(ClassesMapper.class).selectAllClasses(schoolId);
	}

	@Override
	public int selectAllClassCount(String classesName, Long schoolId) {
		return super.getMapper(ClassesMapper.class).selectAllClassCount(classesName, schoolId);
	}

	@Override
	public List<TeacherClasses> selectAllClass(int offset, int rows, String classesName, Long schoolId) {
		return super.getMapper(ClassesMapper.class).selectAllClass(offset, rows, classesName, schoolId);
	}

	@Override
	public List<DictionaryTeacher> getTeacherList(Long classesId) {
		return super.getMapper(ClassesMapper.class).getTeacherList(classesId);
	}

	@Override
	public List<ClassesStudent> getStudentList(Long offset, Long rows, Long classesId) {
		return super.getMapper(ClassesMapper.class).getStudentList(offset, rows, classesId);
	}

	@Override
	public int getStudentListCount(Long classesId) {
		return super.getMapper(ClassesMapper.class).getStudentListCount(classesId);
	}

	@Override
	public List<Long> getYearClassesList(Long schoolId, int year) {
		return super.getMapper(ClassesMapper.class).getYearClassesList(schoolId, year);
	}

	@Override
	public int insertClasses(Long schoolId, int del, List<Map<String, Object>> list, Boolean flag, Long userId) {
		ClassesMapper mapper = super.getMapper(ClassesMapper.class);
		
		Long teacherId = null;
		
		if (del == 1) {
			mapper.deleteClasses(schoolId);
		}
		
		Classes classes = new Classes();
		for (Map<String, Object> m : list) {
			classes = new Classes();
			classes.setId(super.getMapper(SequenceMapper.class).sequence());
			classes.setYear(Long.parseLong(m.get("year").toString()));
			classes.setNum(m.get("num").toString());
			classes.setName(m.get("name").toString());
			classes.setSchoolId(schoolId);
			classes.setCreateTeacherId(userId);
			if (m.get("teacherId") != null && !"".equals(m.get("teacherId"))) {
				teacherId = Long.parseLong(m.get("teacherId").toString());
				classes.setTeacherId(teacherId);
			}
			if (m.get("remark") != null && !"".equals(m.get("remark"))) {
				classes.setRemark(m.get("remark").toString());
			}
			if (m.get("description") != null && !"".equals(m.get("description"))) {
				classes.setDescription(m.get("description").toString());
			}
			mapper.insert(classes);
			
			m.put("cId", classes.getId());
			m.put("cName", classes.getName());
			m.put("cTeacherId", teacherId);
		}
		
		return list.size();
	}

	@Override
	public void deleteClasses(Long schoolId) {
		super.getMapper(ClassesMapper.class).deleteClasses(schoolId);
	}

	@Override
	public void idDisk(Long id, String idDisk) {
		super.getMapper(ClassesMapper.class).idDisk(id, idDisk);
	}

	@Override
	public void updateCreateTeacher(Long id, Long createTeacherId) {
		super.getMapper(ClassesMapper.class).updateCreateTeacher(id, createTeacherId);
	}

}
