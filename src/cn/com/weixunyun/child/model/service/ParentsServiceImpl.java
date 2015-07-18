package cn.com.weixunyun.child.model.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.control.AbstractResource.PartField;
import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.StudentParents;
import cn.com.weixunyun.child.model.dao.ParentsMapper;
import cn.com.weixunyun.child.model.dao.ParentsMapperProvider;
import cn.com.weixunyun.child.model.dao.StudentParentsMapper;
import cn.com.weixunyun.child.model.pojo.Parents;

public class ParentsServiceImpl extends AbstractService implements ParentsService {

	@Override
	public StudentParents select(Long id) {
		return super.getMapper(ParentsMapper.class).select(id);
	}

	@Override
	public int selectAllCount(Long schoolId, Long studentId, String keyword) {
		return super.getMapper(ParentsMapper.class).selectAllCount(schoolId, studentId, keyword);
	}

	@Override
	public List<StudentParents> selectAll(Long offset, Long rows, Long schoolId, Long studentId, String keyword) {
		return super.getMapper(ParentsMapper.class).selectAll(offset, rows, schoolId, studentId, keyword);
	}

	@Override
	public void insert(Parents parent) {
		super.getMapper(ParentsMapper.class).insert(parent);
	}

	@Override
	public void update(Parents parent) {
		super.getMapper(ParentsMapper.class).update(parent);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(ParentsMapper.class).delete(id);
	}

	@Override
	public Parents selectMobile(@Param("studentId") long studentId, @Param("mobile") String mobile) {
		return super.getMapper(ParentsMapper.class).selectMobile(studentId, mobile);
	}

	@Override
	public void password(Long id, String password) {
		super.getMapper(ParentsMapper.class).password(id, password);
	}

	@Override
	public Parents selectParentsMobile(String mobile) {
		return super.getMapper(ParentsMapper.class).selectParentsMobile(mobile);
	}

	@Override
	public void username(@Param("id") Long id, @Param("username") String username) {
		super.getMapper(ParentsMapper.class).username(id, username);
		super.getMapper(StudentParentsMapper.class).updateUsername(id, username);
	}

	@Override
	public void updated(Long id) {
		super.getMapper(ParentsMapper.class).updated(id);
	}

	@Override
	@Select("select * from parents where student_id = #{studentId} and type = #{type} limit 1")
	public Parents selectCode(@Param("studentId") long studentId, @Param("type") String type) {
		return super.getMapper(ParentsMapper.class).selectCode(studentId, type);
	}

	@Override
	public List<Parents> selectParentsInSchool(Long schoolId) {
		return super.getMapper(ParentsMapper.class).selectParentsInSchool(schoolId);
	}

	@Override
	@SelectProvider(type = ParentsMapperProvider.class, method = "getStudentList")
	public List<ClassesStudent> getStudentList(@Param("schoolId") Long schoolId, @Param("parentsId") Long parentsId) {
		return super.getMapper(ParentsMapper.class).getStudentList(schoolId, parentsId);
	}

	@Override
	@SelectProvider(type = ParentsMapperProvider.class, method = "getSchoolParents")
	public Parents getSchoolParents(@Param("schoolId") Long schoolId, @Param("username") String username,
			@Param("password") String password) {
		return super.getMapper(ParentsMapper.class).getSchoolParents(schoolId, username, password);
	}

	@Override
	public void insertParents(Map<String, PartField> map, Parents p, Long schoolId) {

		// p.setStudentId(Long.parseLong(map.get("studentId").getValue()));
		p.setPassword(DigestUtils.md5Hex(map.get("password").getValue()));
		// p.setGender("1".equals(map.get("type").getValue()) == true ? "1" :
		// "2");
		p.setAvailable(true);
		if (map.get("pta") != null) {
			p.setPta("1".equals(map.get("pta").getValue()) == true ? true : false);
		}
		p.setCreateTime(new Timestamp(System.currentTimeMillis()));

		ParentsMapper mapper = super.getMapper(ParentsMapper.class);
		mapper.insert(p);

		cn.com.weixunyun.child.model.pojo.StudentParents sp = new cn.com.weixunyun.child.model.pojo.StudentParents();
		sp.setParentsId(p.getId());
		sp.setStudentId(Long.parseLong(map.get("studentId").getValue()));
		sp.setType(map.get("type").getValue());
		sp.setUsername(map.get("username").getValue());
		sp.setSchoolId(schoolId);

		StudentParentsMapper mapperSP = super.getMapper(StudentParentsMapper.class);
		mapperSP.insert(sp);
	}

	@Override
	public void updateParents(Map<String, PartField> map, Parents p, Long schoolId) {

		p.setPassword(DigestUtils.md5Hex(map.get("password").getValue()));
		p.setAvailable(true);
		if (map.get("pta") != null) {
			p.setPta("1".equals(map.get("pta").getValue()) == true ? true : false);
		}

		ParentsMapper mapper = super.getMapper(ParentsMapper.class);
		mapper.update(p);

		cn.com.weixunyun.child.model.pojo.StudentParents sp = new cn.com.weixunyun.child.model.pojo.StudentParents();
		sp.setParentsId(p.getId());
		sp.setStudentId(Long.parseLong(map.get("studentId").getValue()));
		sp.setType(map.get("type").getValue());
		sp.setUsername(map.get("username").getValue());
		sp.setSchoolId(schoolId);

		StudentParentsMapper mapperSP = super.getMapper(StudentParentsMapper.class);
		mapperSP.update(sp);
		mapperSP.updateUsername(p.getId(), sp.getUsername());
	}

	@Override
	public void deleteParents(Long id, Long studentId) {
		ParentsMapper mapper = super.getMapper(ParentsMapper.class);
		mapper.delete(id);

		StudentParentsMapper mapperSP = super.getMapper(StudentParentsMapper.class);
		mapperSP.delete(studentId, id);
	}

	@Override
	public void updatePoint(Long id, int point) {
		super.getMapper(ParentsMapper.class).updatePoint(id, point);
	}

	@Override
	public void idDisk(Long id, Long idDisk) {
		super.getMapper(ParentsMapper.class).idDisk(id, idDisk);
	}

}
