package cn.com.weixunyun.child.module.broadcast;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface BroadcastMapper {

	@SelectProvider(type = BroadcastMapperProvider.class, method = "getList")
	public List<ClassesBroadcast> getList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "grade") int grade,
			@Param(value = "classesId") Long classesId, @Param(value = "term") String term);

	@SelectProvider(type = BroadcastMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "grade") int grade,
			@Param(value = "classesId") Long classesId, @Param(value = "audit") Long audit, @Param(value = "term") String term);

	@Insert("insert into broadcast (id, title,description, create_time, school_id, classes_id, create_teacher_id, comment, pic, voice_length, grade, term) "
			+ "values (#{id}, #{title},#{description}, now(), #{schoolId},#{classesId},#{createTeacherId},#{comment}, #{pic}, #{voiceLength}, #{grade}, #{term})")
	public void insert(Broadcast broadcast);

	@Update("update broadcast set description= #{description}, title=#{title}, pic = #{pic}, grade = #{grade}, update_teacher_id=#{updateTeacherId}, update_time=now() where id = #{id}")
	public void update(Broadcast broadcast);

	@Update("update broadcast set pic = #{pic}, update_time = now() where id = #{id}")
	public void updateImage(@Param(value = "pic") Long pic, @Param(value = "id") Long id);

	@Delete("delete from broadcast where id = #{id}")
	public void delete(Long id);
	
	@Delete("delete from broadcast where school_id = #{schoolId} and term = #{term} and classes_id is null ")
	public void deleteBroadcastGrades(@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term);
	
	@Delete("delete from broadcast where term = #{term} and classes_id = #{classesId} ")
	public void deleteBroadcastClassess(@Param(value = "classesId") Long classesId, @Param(value = "term") String term);

	@SelectProvider(type = BroadcastMapperProvider.class, method = "get")
	public ClassesBroadcast get(Long id);

	@SelectProvider(type = BroadcastMapperProvider.class, method = "getSchoolBroadcastList")
	public List<ClassesBroadcast> getSchoolBroadcastList(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "schoolId") Long schoolId,
			@Param(value = "keyword") String keyword, @Param(value = "term") String term);

	@SelectProvider(type = BroadcastMapperProvider.class, method = "getSchoolBroadcastListCount")
	public int getSchoolBroadcastListCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "keyword") String keyword, @Param(value = "term") String term);

	@SelectProvider(type = BroadcastMapperProvider.class, method = "getGradeBroadcastList")
	public List<ClassesBroadcast> getGradeBroadcastList(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "schoolId") Long schoolId,
			@Param(value = "keyword") String keyword, @Param(value = "term") String term);

	@SelectProvider(type = BroadcastMapperProvider.class, method = "getGradeBroadcastListCount")
	public int getGradeBroadcastListCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "keyword") String keyword, @Param(value = "term") String term);

	@SelectProvider(type = BroadcastMapperProvider.class, method = "getClassesBroadcast")
	public List<ClassesBroadcast> getClassesBroadcast(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "schoolId") Long schoolId,
			@Param(value = "keyword") Long keyword, @Param(value = "term") String term);

	@SelectProvider(type = BroadcastMapperProvider.class, method = "getClassesBroadcastCount")
	public int getClassesBroadcastCount(@Param(value = "schoolId") Long schoolId, @Param(value = "keyword") Long keyword, 
			@Param(value = "term") String term);

}
