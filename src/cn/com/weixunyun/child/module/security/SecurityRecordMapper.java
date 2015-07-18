package cn.com.weixunyun.child.module.security;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface SecurityRecordMapper {

	@Select("select * from security_record where id = #{id}")
	public SecurityRecord select(@Param(value = "id") Long id);

	@SelectProvider(type = SecurityRecordMapperProvider.class, method = "selectAllCount")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term,
			@Param(value = "userId") Long userId);

	@SelectProvider(type = SecurityRecordMapperProvider.class, method = "selectAll")
	public List<DeviceSecurityRecord> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term,
			@Param(value = "userId") Long userId);

	@Insert("insert into security_record (id, user_id, card, time, reach, device, device_time, school_id, term) values (#{id}, #{userId}, #{card}, now(), #{reach}, #{device}, #{deviceTime}, #{schoolId}, #{term})")
	public void insert(SecurityRecord record);

	@Update("update security_record set reach = #{reach}, code = #{code} where id = #{id}")
	public void update(SecurityRecord record);

	@Delete("delete from security_record where id = #{id}")
	public void delete(Long id);

}
