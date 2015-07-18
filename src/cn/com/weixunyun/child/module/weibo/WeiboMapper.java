package cn.com.weixunyun.child.module.weibo;

import java.util.List;

import org.apache.ibatis.annotations.*;

public interface WeiboMapper {

	@SelectProvider(type = WeiboMapperProvider.class, method = "get")
    @ResultMap("getList")
    public UserWeibo get(@Param(value = "id") Long id, @Param(value = "userId") Long userId);

	@SelectProvider(type = WeiboMapperProvider.class, method = "getSchoolWeiboListCount")
	public int getSchoolWeiboListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "userId") Long userId,
			@Param(value = "keyword") String keyword);

	@SelectProvider(type = WeiboMapperProvider.class, method = "getSchoolWeiboList")
    @ResultMap("getList")
    public List<UserWeibo> getSchoolWeiboList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "userId") Long userId,
			@Param(value = "keyword") String keyword);

	@Insert("insert into weibo (id, description, create_time, school_id, classes_id, create_user_id, pic, position_x, position_y, position_name) values (#{id}, #{description}, now(), #{schoolId},#{classesId},#{createUserId},#{pic}, #{positionX}, #{positionY}, #{positionName})")
	public void insert(Weibo weibo);

	@Delete("delete from weibo where id = #{id}")
	public void delete(Long id);
	
	@Delete("delete from weibo where classes_id = #{classesId}")
	public void deleteWeiboClasses(@Param(value = "classesId") Long classesId);
	
	@Update("update weibo set pic = #{pic}, update_time = now() where id = #{id}")
	public void updateImage(@Param(value = "pic") Long pic, @Param(value = "id") Long id);

	@Update("update weibo set audit_teacher_id = #{auditTeacherId}, description = #{description}, update_time = now(), update_user_id = #{updateUserId}, pic = #{pic} where id = #{id}")
	public void update(Weibo weibo);



	@SelectProvider(type = WeiboMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "userId") Long userId,
			@Param(value = "classesId") Long classesId, @Param(value = "studentId") Long studentId,
			@Param(value = "teacherId") Long teacherId);

	@SelectProvider(type = WeiboMapperProvider.class, method = "getList")
    @ResultMap("getList")
	public List<UserWeibo> getList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "userId") Long userId,
			@Param(value = "classesId") Long classesId, @Param(value = "studentId") Long studentId,
			@Param(value = "teacherId") Long teacherId);



    @SelectProvider(type = WeiboMapperProvider.class, method = "getClassesList")
    @ResultMap("getList")
    public List<UserWeibo> getClassesWeiboList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
                                               @Param(value = "schoolId") Long schoolId, @Param(value = "classesId") Long classesId);

	@SelectProvider(type = WeiboMapperProvider.class, method = "getClassesListCount")
	public int getClassesWeiboListCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "classesId") Long classesId);



    @Insert("insert into weibo_zan(weibo_id,user_id,time) values(#{weiboId},#{userId},#{time})")
    void insertZan(WeiboZan zan);

    @Delete("delete from weibo_zan where weibo_id=#{weiboId} and user_id=#{userId}")
    void deleteZan(@Param("weiboId") Long weiboId, @Param("userId") Long userId);
}
