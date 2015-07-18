package cn.com.weixunyun.child.model.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import cn.com.weixunyun.child.model.bean.CourseClasses;
import cn.com.weixunyun.child.model.bean.DictionaryTeacher;
import cn.com.weixunyun.child.model.dao.GlobalMapper;
import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.dao.TeacherMapper;
import cn.com.weixunyun.child.model.dao.TeacherMapperProvider;
import cn.com.weixunyun.child.model.pojo.Menu;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;

public class TeacherServiceImpl extends AbstractService implements TeacherService {

	@Override
	public Teacher select(Long id) {
		return super.getMapper(TeacherMapper.class).select(id);
	}

	@Override
	public int selectAllCount(Long schoolId, String keyword) {
		return super.getMapper(TeacherMapper.class).selectAllCount(schoolId, keyword);
	}

	@Override
	public List<DictionaryTeacher> selectAll(Long offset, Long rows, Long schoolId, String keyword) {
		return super.getMapper(TeacherMapper.class).selectAll(offset, rows, schoolId, keyword);
	}

	@Override
	public void insert(Teacher teacher) {
		super.getMapper(TeacherMapper.class).insert(teacher);
	}

	@Override
	public void update(Teacher teacher) {
		super.getMapper(TeacherMapper.class).update(teacher);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(TeacherMapper.class).delete(id);
	}

	@Override
	public List<School> getSchoolList(String mobile, String password) {
		return super.getMapper(TeacherMapper.class).getSchoolList(mobile, password);
	}

	@Override
	public Teacher selectMobile(@Param("classesId") Long classesId, @Param("mobile") String mobile) {
		return super.getMapper(TeacherMapper.class).selectMobile(classesId, mobile);
	}

	@Override
	public List<Menu> getMenuList(@Param("id") long id) {
		return super.getMapper(TeacherMapper.class).getMenuList(id);
	}

	@Override
	public List<DictionaryTeacher> getAdminList(Long offset, Long rows, Long schoolId, String keyword) {
		return super.getMapper(TeacherMapper.class).getAdminList(offset, rows, schoolId, keyword);
	}

	@Override
	public int getAdminListCount(Long schoolId, String keyword) {
		return super.getMapper(TeacherMapper.class).getAdminListCount(schoolId, keyword);
	}

	@Override
	public List<Teacher> selectTeacher(Long schoolId) {
		return super.getMapper(TeacherMapper.class).selectTeacher(schoolId);
	}

	@Override
	public int selectAllTeacherCount(@Param("clsId") Long clsId, @Param("schoolId") Long schoolId) {
		return super.getMapper(TeacherMapper.class).selectAllTeacherCount(clsId, schoolId);
	}

	@Override
	public List<Teacher> selectAllTeacher(@Param("offset") Long offset, @Param("rows") Long rows,
			@Param("clsId") Long clsId, @Param("schoolId") Long schoolId) {
		return super.getMapper(TeacherMapper.class).selectAllTeacher(offset, rows, clsId, schoolId);
	}

	@Override
	public void password(Long id, String password) {
		super.getMapper(TeacherMapper.class).password(id, password);
	}

	@Override
	public Teacher selectTeacherMobile(Long schoolId, String mobile) {
		return getMapper(TeacherMapper.class).selectTeacherMobile(schoolId, mobile);
	}

	@Override
	public void username(Long id, String username) {
		super.getMapper(TeacherMapper.class).username(id, username);
	}

	@Override
	public void updated(Long id) {
		super.getMapper(TeacherMapper.class).updated(id);
	}

	@Override
	public void cardAvailable(Long id, Boolean cardAvailable) {
		super.getMapper(TeacherMapper.class).cardAvailable(id, cardAvailable);
	}

	@Override
	@Select("select t.* from teacher t where t.school_id = #{schoolId} and t.username = #{username} and t.password = #{password} limit 1")
	public Teacher getSchoolTeacher(@Param("schoolId") Long schoolId, @Param("username") String username,
			@Param("password") String password) {
		return super.getMapper(TeacherMapper.class).getSchoolTeacher(schoolId, username, password);
	}

	@Override
	@SelectProvider(type = TeacherMapperProvider.class, method = "getClassesList")
	public List<CourseClasses> getClassesList(@Param("schoolId") Long schoolId, 
			@Param("teacherId") Long teacherId) {
		return super.getMapper(TeacherMapper.class).getClassesList(schoolId, teacherId);
	}

	@Override
	public int insertTeachers(Long schoolId, int del, List<Map<String, Object>> list, Boolean flag) {
		TeacherMapper mapper = super.getMapper(TeacherMapper.class);
		
		if (del == 1) {
			mapper.deleteTeachers(schoolId);
		}
		
		GlobalMapper globalMapper= super.getMapper(GlobalMapper.class);
		String password = globalMapper.select(schoolId, "parents", "password").getValue();
		
		Teacher teacher = new Teacher();
		for (Map<String, Object> m : list) {
			teacher.setId(super.getMapper(SequenceMapper.class).sequence());
			teacher.setName(m.get("name").toString());
			teacher.setMobile(m.get("mobile").toString());
			teacher.setUsername(m.get("username").toString());
			teacher.setPassword(DigestUtils.md5Hex(password));
			teacher.setAvailable(true);
			teacher.setSchoolId(schoolId);
			teacher.setCard(m.get("card") == null?null:m.get("card").toString());
			teacher.setCardAvailable(true);
			teacher.setCode(m.get("code")==null?null:m.get("code").toString());
			teacher.setEmail(m.get("email")==null?null:m.get("email").toString());
			teacher.setGender(m.get("gender")==null?null:m.get("gender").toString());
			teacher.setRemark(m.get("remark")==null?null:m.get("remark").toString());
			if (m.get("type") != null) {
				teacher.setType((Boolean)m.get("type")==true?6L:0L);
			} else {
				teacher.setType(0L);
			}
 			teacher.setDescription(m.get("description")==null?null:m.get("description").toString());
			if (m.get("title") != null) {
				teacher.setTitle("".equals(m.get("title"))==true?"":m.get("title").toString());
			}
			
			mapper.insert(teacher);
			
			m.put("tId", teacher.getId());
			m.put("tName", teacher.getName());
			
		}
		
		return list.size();
	}

	@Override
	public void deleteTeachers(Long schoolId) {
		super.getMapper(TeacherMapper.class).deleteTeachers(schoolId);
	}

	@Override
	public DictionaryTeacher selectDictionaryTeacher(Long id) {
		return super.getMapper(TeacherMapper.class).selectDictionaryTeacher(id);
	}

	@Override
	public List<DictionaryTeacher> getNotAdminList( Long schoolId) {
		return super.getMapper(TeacherMapper.class).getNotAdminList(schoolId);
	}

	@Override
	public void admin(Long id, Long type) {
		super.getMapper(TeacherMapper.class).admin(id, type);
	}

	@Override
	public void updatePoint(Long id, int point) {
		super.getMapper(TeacherMapper.class).updatePoint(id, point);
	}

	@Override
	public void idDisk(Long id, Long idDisk) {
		super.getMapper(TeacherMapper.class).idDisk(id, idDisk);
	}

}
