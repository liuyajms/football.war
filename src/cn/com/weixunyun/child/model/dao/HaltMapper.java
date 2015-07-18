package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.pojo.Halt;

public interface HaltMapper {

	@Select("select * from halt where id = #{id}")
	public Halt select(Long id);
	
	@Select("select count(id) from halt ")
	public int selectAllCount();

	@Select("select * from halt order by id desc limit #{rows} offset #{offset} ")
	public List<Halt> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows);
	
	@Select("select * from halt where available=true order by id desc limit 1")
	public Halt selectAllAvailable();

	@Insert("insert into halt (id, time, type, description, available) values (#{id}, #{time}, #{type}, #{description}, #{available})")
	public void insert(Halt halt);

	@Update("update halt set time=#{time}, description=#{description}, type=#{type}, available=#{available} where id=#{id}")
	public void update(Halt halt);
	
	@Update("update halt set readed_time=now() where id=#{id}")
	public void updateReaded(Long id);
	
	@Update("update halt set available=#{available} where id=#{id}")
	public void updateAvailable(Long id, Boolean available);

	@Delete("delete from halt where id=#{id}")
	public void delete(Long id);

}
