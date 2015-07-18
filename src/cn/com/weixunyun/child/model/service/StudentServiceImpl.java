package cn.com.weixunyun.child.model.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.annotations.Param;

import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.StudentClasses;
import cn.com.weixunyun.child.model.dao.ParentsMapper;
import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.dao.StudentMapper;
import cn.com.weixunyun.child.model.dao.StudentParentsMapper;
import cn.com.weixunyun.child.model.pojo.Parents;
import cn.com.weixunyun.child.model.pojo.Student;
import cn.com.weixunyun.child.model.pojo.StudentParents;

public class StudentServiceImpl extends AbstractService implements StudentService {

	@Override
	public ClassesStudent select(Long id) {
		return super.getMapper(StudentMapper.class).select(id);
	}

	@Override
	public int selectAllCount(Long schoolId, String keyword) {
		return super.getMapper(StudentMapper.class).selectAllCount(schoolId, keyword);
	}

	@Override
	public List<StudentClasses> selectAll(Long schoolId, Long offset, Long rows, String keyword) {
		return super.getMapper(StudentMapper.class).selectAll(schoolId, offset, rows, keyword);
	}

	@Override
	public void insert(Student student) {
		super.getMapper(StudentMapper.class).insert(student);
	}

	@Override
	public void update(Student student) {
		super.getMapper(StudentMapper.class).update(student);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(StudentMapper.class).delete(id);
	}

	@Override
	public int selectAllStudentCount(Long clsId, Long schoolId) {
		return super.getMapper(StudentMapper.class).selectAllStudentCount(clsId, schoolId);
	}

	@Override
	public List<Student> selectAllStudent(int offset, int rows, Long clsId, Long schoolId) {
		return super.getMapper(StudentMapper.class).selectAllStudent(offset, rows, clsId, schoolId);
	}

	@Override
	public int selectStudentCount(Long teacherId, Long schoolId) {
		return super.getMapper(StudentMapper.class).selectStudentCount(teacherId, schoolId);
	}

	@Override
	public List<Student> selectStudent(Long teacherId, Long schoolId) {
		return super.getMapper(StudentMapper.class).selectStudent(teacherId, schoolId);
	}

	@Override
	public Student selectGet(Long id) {
		return super.getMapper(StudentMapper.class).selectGet(id);

	}

	@Override
	public void updated(Long id) {
		super.getMapper(StudentMapper.class).updated(id);
	}

	@Override
	public int getListCount(Long schoolId, Long classesId, String keyword) {
		return super.getMapper(StudentMapper.class).getListCount(schoolId, classesId, keyword);
	}

	@Override
	public List<ClassesStudent> getList(Long schoolId, Long offset, Long rows, Long classesId, String keyword) {
		return super.getMapper(StudentMapper.class).getList(schoolId, offset, rows, classesId, keyword);
	}

	@Override
	public ClassesStudent getCardStudent(@Param("schoolId") Long schoolId, @Param("card") String card) {
		return super.getMapper(StudentMapper.class).getCardStudent(schoolId, card);
	}

	@Override
	public void updatedCardAvailable(Long id, Boolean available) {
		super.getMapper(StudentMapper.class).updatedCardAvailable(id, available);
	}

	@Override
	public void deleteClassesStudent(Long classesId) {
		super.getMapper(StudentMapper.class).deleteClassesStudent(classesId);
	}

	@Override
	public int insertStudents(Long schoolId, Long classesId, int del, List<Map<String, Object>> list, Boolean flag, Long userId) {
		StudentMapper mapper = super.getMapper(StudentMapper.class);
		StudentParentsMapper mapperSP = super.getMapper(StudentParentsMapper.class);
		ParentsMapper mapperP = super.getMapper(ParentsMapper.class);
		
		if (del == 1) {
			mapper.deleteClassesStudent(classesId);
			
			/*if (flag) {
				RestClient client = new RestClient();
				String path = simiyunUrl + "/groupsEdu/delete?type=deleteGroupAllUser&group_name=" + classesId
						+ "&username=" + userId;
				org.apache.wink.client.Resource resource = client.resource(path);
				resource.get();
			}*/
		}

		Student student = new Student();
		Parents p = new Parents();
		StudentParents sp = new StudentParents();
		for (Map<String, Object> m : list) {
			// 添加学生
			student = new Student();
			student.setId(super.getMapper(SequenceMapper.class).sequence());
			student.setName(m.get("name").toString());
			if (m.get("birthday") != null && !"".equals(m.get("birthday").toString())) {
				try {
					java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(m.get("birthday").toString());
					student.setBirthday(new Date(date.getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			student.setClassesId(classesId);
			student.setCreateTime(new Timestamp(System.currentTimeMillis()));
			student.setGender(m.get("gender") == null ? null : m.get("gender").toString());
			student.setSchoolId(schoolId);
			student.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			student.setAddress(m.get("address") == null ? null : m.get("address").toString());
			student.setCode(m.get("code") == null ? null : m.get("code").toString());
			student.setCard(m.get("card") == null ? null : m.get("card").toString());
			student.setDescription(m.get("description") == null ? null : m.get("description").toString());
			mapper.insert(student);

			if (m.get("parentsName") != null && m.get("parentsUsername") != null) {
				// 添加家长
				p = new Parents();
				p.setPassword(DigestUtils.md5Hex("1234"));
				p.setId(super.getMapper(SequenceMapper.class).sequence());
				// p.setGender("".equals(m.get("p_gender"))==true?"":"男".equals(m.get("p_gender"))==true?"1":"2");
				p.setMobile(m.get("parentsMobile").toString());
				p.setName(m.get("parentsName").toString());
				p.setAvailable(true);
				p.setPta(m.get("parentsPta") == null ? null : (Boolean) m.get("parentsPta"));
				p.setCreateTime(new Timestamp(System.currentTimeMillis()));
				mapperP.insert(p);

				sp = new StudentParents();
				sp.setParentsId(p.getId());
				sp.setSchoolId(schoolId);
				sp.setStudentId(student.getId());
				sp.setType(m.get("parentsType") == null ? "1" :m.get("parentsType").toString());
				sp.setUsername(m.get("parentsUsername").toString());
				mapperSP.insert(sp);
				
				m.put("pId", p.getId());
				m.put("pName", p.getName());
				
			}
		}
		return list.size();
	}

	@Override
	public List<StudentClasses> selectExportList(Long schoolId, Long classesId, String keyword) {
		return super.getMapper(StudentMapper.class).selectExportList(schoolId, classesId, keyword);
	}

}
