package cn.com.weixunyun.child.module.personal.photo;

import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PhotoMapper {

	@Select("select * from photo where id = #{id}")
	public Photo select(Long id);

	@SelectProvider(type = PhotoMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "userId") Long userId, @Param(value = "keyword") String keyword);

	@SelectProvider(type = PhotoMapperProvider.class, method = "getList")
	public List<UserPhoto> getList(@Param(value = "offset") int offset,
                                     @Param(value = "rows") int rows,
                                     @Param(value = "userId") Long userId,
                                     @Param(value = "keyword") String keyword);

	@Insert(" insert into photo( id, school_id, description, name, create_user_id, create_time) " +
			"values (#{id}, #{schoolId}, #{description}, #{name}, #{createUserId}, now()) ")
	public void insert(Photo photo);

	@Update("update photo set name=#{name}, description=#{description}, update_user_id= #{updateUserId}, update_time=now() where id=#{id}")
	public void update(Photo photo);
	
	@Update("update photo set update_user_id=#{userId}, update_time = now() where id = #{id}")
	public void updateImage(@Param(value = "id") Long id, @Param(value = "userId") Long userId);

	@Delete("delete from photo where id=#{id}")
	public void delete(Long id);

}
