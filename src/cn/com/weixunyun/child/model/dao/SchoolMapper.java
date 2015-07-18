package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.pojo.School;

public interface SchoolMapper {

	@Select("select * from school where id = #{id}")
	public School select(long id);

	@SelectProvider(type = SchoolMapperProvider.class, method = "selectCount")
	public int selectAllCount(@Param(value = "keyword") String keyword);

	@SelectProvider(type = SchoolMapperProvider.class, method = "selectPage")
	public List<School> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows, @Param(value = "keyword") String keyword);

	@Insert("insert into school (id, name, phone, email, address, available, vx, vy, region, create_time, description) values (#{id}, #{name}, #{phone}, #{email}, #{address}, #{available}, #{vx}, #{vy}, #{region}, now(), #{description})")
	public void insert(School school);

	@Update("update school set name=#{name}, phone=#{phone}, email=#{email}, address=#{address}, available=#{available}, vx=#{vx}, vy=#{vy}, region=#{region}, description=#{description}, update_time = now() where id=#{id}")
	public void update(School school);

	@Delete("delete from school where id=#{id}")
	public void delete(Long id);
	
	@Select("select * from school where region = #{countyCode}")
	public List<School> getAllSchools(String countyCode);
}
