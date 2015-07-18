package cn.com.weixunyun.child.module.notice;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface NoticeMapper {

	@Select("select n.*, t.name as createUserName from notice n join teacher t on t.id = n.create_user_id where n.id = #{id} limit 1")
	public TeacherNotice get(Long id);

	@SelectProvider(type = NoticeMapperProvider.class, method = "getList")
	public List<TeacherNotice> getList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "teacher") boolean teacher,
			@Param(value = "parents") boolean parents, @Param(value = "keyword") String keyword);

	@SelectProvider(type = NoticeMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "teacher") boolean teacher,
			@Param(value = "parents") boolean parents, @Param(value = "keyword") String keyword);

	@Insert("insert into notice (id, school_id, create_user_id, create_time, description, classes_id, audit_teacher_id, audit_time, pic, comments_id, position_x, position_y, position_name, update_user_id, update_time, push_teacher, push_parents, voice_length) "
			+ "	values (#{id}, #{schoolId}, #{createUserId}, now(), #{description}, #{classesId}, #{auditTeacherId}, now(), #{pic}, #{commentsId}, #{positionX}, #{positionY}, #{positionName}, #{updateUserId}, now(), #{pushTeacher}, #{pushParents}, #{voiceLength})")
	public void insert(Notice notice);

	@Update("update notice set description=#{description}, classes_id=#{classesId}, pic=#{pic}, position_x=#{positionX}, position_y=#{positionY}, position_name=#{positionName}, update_user_id=#{updateUserId}, update_time=now(), push_teacher=#{pushTeacher}, push_parents=#{pushParents}, voice_length=#{voiceLength} where id=#{id}")
	public void update(Notice notice);

	@Update("update notice set pic = #{pic}, update_user_id = #{userId}, update_time = now() where id = #{id}")
	public void updateImage(@Param(value = "id") Long id, @Param(value = "userId") Long userId,
			@Param(value = "pic") int pic);

	@Delete("delete from notice where id=#{id}")
	public void delete(Long id);
	
	@Delete("delete from notice where school_id=#{schoolId}")
	public void deleteNotices(@Param(value = "schoolId") Long schoolId);

}
