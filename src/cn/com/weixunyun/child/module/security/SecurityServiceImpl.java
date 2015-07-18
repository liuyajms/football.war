package cn.com.weixunyun.child.module.security;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import cn.com.weixunyun.child.model.dao.GlobalMapper;
import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class SecurityServiceImpl extends AbstractService implements SecurityService {

	@Override
	public void insert(Security Security) {
		super.getMapper(SecurityMapper.class).insert(Security);
	}

	@Override
	public void update(Security Security) {
		super.getMapper(SecurityMapper.class).update(Security);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(SecurityMapper.class).delete(id);
	}

	@Override
	public Security select(Long id) {
		return super.getMapper(SecurityMapper.class).select(id);
	}

	@Override
	public int selectAllCount(Long schoolId, String term, Long studentId) {
		return super.getMapper(SecurityMapper.class).selectAllCount(schoolId, term, studentId);
	}

	@Override
	public Security selectDate(Date date) {
		return super.getMapper(SecurityMapper.class).selectDate(date);
	}

	@Override
	public List<StudentSecurity> selectAll(Long offset, Long rows, Long schoolId, String term, Long studentId) {
		return super.getMapper(SecurityMapper.class).selectAll(offset, rows, schoolId, term, studentId);
	}

	@Override
	@SelectProvider(type = SecurityMapperProvider.class, method = "getTermSecurityList")
	public List<StudentSecurity> getTermSecurityList(@Param("offset") Long offset, @Param("rows") Long rows,
			@Param("schoolId") Long schoolId, @Param("term") String term, @Param("classesId") Long classesId) {
		return super.getMapper(SecurityMapper.class).getTermSecurityList(offset, rows, schoolId, term, classesId);
	}

	@Override
	@Select("select * from security where school_id = #{schoolId} and term = #{term} and student_id = #{studentId} and date = #{date}")
	public Security getDateSecurity(Long schoolId, String term, Long studentId, Date date) {
		return super.getMapper(SecurityMapper.class).getDateSecurity(schoolId, term, studentId, date);
	}

	@Override
	public void updateReach(Long schoolId, String term, Long studentId, Timestamp time) {
		Date date = new java.sql.Date(time.getTime());

		GlobalMapper globalMapper = super.getMapper(GlobalMapper.class);
		String reachTime = globalMapper.select(schoolId, "reach", "time").getValue();
		String reachBegin = globalMapper.select(schoolId, "reach", "begin").getValue();
		String reachEnd = globalMapper.select(schoolId, "reach", "end").getValue();

		String reach = new SimpleDateFormat("HH:mm").format(time);

		if (reach.compareTo(reachBegin) > 0 && reach.compareTo(reachEnd) < 0) {
			SecurityMapper securityMapper = super.getMapper(SecurityMapper.class);
			Security security = securityMapper.getDateSecurity(schoolId, term, studentId, date);
			if (security == null) {
				SequenceMapper sequenceMapper = super.getMapper(SequenceMapper.class);

				security = new Security();
				security.setId(sequenceMapper.sequence());
				security.setSchoolId(schoolId);
				security.setTerm(term);
				security.setStudentId(studentId);
				security.setDate(date);
				security.setReachTime(time);
				security.setReachOver(reach.compareTo(reachTime) > 0);
				securityMapper.insert(security);
			} else if (security.getReachTime() == null) {
				security.setReachTime(time);
				security.setReachOver(reach.compareTo(reachTime) > 0);
				securityMapper.update(security);
			}
		}

	}

	@Override
	public void updateLeave(Long schoolId, String term, Long studentId, Timestamp time) {
		Date date = new java.sql.Date(time.getTime());

		GlobalMapper globalMapper = super.getMapper(GlobalMapper.class);
		String leaveTime = globalMapper.select(schoolId, "leave", "time").getValue();
		String leaveBegin = globalMapper.select(schoolId, "leave", "begin").getValue();
		String leaveEnd = globalMapper.select(schoolId, "leave", "end").getValue();

		String leave = new SimpleDateFormat("HH:mm").format(time);

		if (leave.compareTo(leaveBegin) > 0 && leave.compareTo(leaveEnd) < 0) {
			SecurityMapper securityMapper = super.getMapper(SecurityMapper.class);
			Security security = securityMapper.getDateSecurity(schoolId, term, studentId, date);
			if (security == null) {
				SequenceMapper sequenceMapper = super.getMapper(SequenceMapper.class);

				security = new Security();
				security.setId(sequenceMapper.sequence());
				security.setSchoolId(schoolId);
				security.setTerm(term);
				security.setStudentId(studentId);
				security.setDate(date);
				security.setLeaveTime(time);
				security.setLeaveOver(leave.compareTo(leaveTime) < 0);
				securityMapper.insert(security);
			} else {
				security.setLeaveTime(time);
				security.setLeaveOver(leave.compareTo(leaveTime) < 0);
				securityMapper.update(security);
			}
		}

	}

	@Override
	public int getTermSecurityListCount(Long schoolId, String term, Long classesId) {
		return super.getMapper(SecurityMapper.class).getTermSecurityListCount(schoolId, term, classesId);
	}
}
