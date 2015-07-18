package cn.com.weixunyun.child.module.security;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface SecurityMapper {

	@Select("select * from security where id = #{id}")
	public Security select(@Param(value = "id") Long id);

	@Select("select * from security where date = #{date} limit 1")
	public Security selectDate(@Param(value = "date") Date date);

	@Select("select count(*) from security where school_id = #{schoolId} and term = #{term} and student_id = #{studentId}")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term,
			@Param(value = "studentId") Long studentId);

	@Select("select s.*, st.name as studentName from security s join student st on st.id = s.student_id where s.school_id = #{schoolId} and s.term = #{term} and s.student_id = #{studentId} order by s.id desc limit #{rows} offset #{offset}")
	public List<StudentSecurity> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term,
			@Param(value = "studentId") Long studentId);

	@SelectProvider(type = SecurityMapperProvider.class, method = "getTermSecurityList")
	public List<StudentSecurity> getTermSecurityList(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "schoolId") Long schoolId,
			@Param(value = "term") String term, @Param(value = "classesId") Long classesId);
	
	@SelectProvider(type = SecurityMapperProvider.class, method = "getTermSecurityListCount")
	public int getTermSecurityListCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "term") String term, @Param(value = "classesId") Long classesId);

	@Insert("insert into security (id, term, student_id, date, reach_time, reach_over, leave_time, leave_over, school_id) values (#{id}, #{term}, #{studentId}, #{date}, #{reachTime}, #{reachOver}, #{leaveTime}, #{leaveOver}, #{schoolId})")
	public void insert(Security term);

	@Update("update security set term=#{term}, student_id=#{studentId}, date=#{date}, reach_time=#{reachTime}, reach_over = #{reachOver}, leave_time = #{leaveTime}, leave_over = #{leaveOver} where id=#{id}")
	public void update(Security term);

	@Delete("delete from security where id = #{id}")
	public void delete(Long id);

	@Select("select * from security where school_id = #{schoolId} and term = #{term} and student_id = #{studentId} and date = #{date} limit 1")
	public Security getDateSecurity(@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term,
			@Param(value = "studentId") Long studentId, @Param(value = "date") Date date);

}
