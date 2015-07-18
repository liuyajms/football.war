package cn.com.weixunyun.child.module.point;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

public interface PointMapper {

	@Select("select * from point where id = #{id}")
	public Point get(Long id);

	@SelectProvider(type = PointMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId);

	@SelectProvider(type = PointMapperProvider.class, method = "getList")
	public List<Point> getList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId);

	@Insert("insert into point (id, module, point, user_id, time, school_id, description) values (#{id}, #{module}, #{point}, #{userId}, now(), #{schoolId}, #{description})")
	public void insert(Point point);

	@Delete("delete from point where id = #{id}")
	public void delete(Long id);
	
	@Delete("delete from point where school_id = #{schoolId}")
	public void deletePoints(@Param(value = "schoolId") Long schoolId);

}
