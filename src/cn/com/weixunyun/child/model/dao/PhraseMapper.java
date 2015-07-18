package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.pojo.Phrase;

public interface PhraseMapper {

	@Select("select * from phrase where id = #{id}")
	public Phrase select(long id);

	@Select("select count(*) from phrase ")
	public int selectAllCount();
	
	@Select("select * from phrase order by id desc limit #{rows} offset #{offset}")
	public List<Phrase> selectAll(@Param(value = "offset") int offset, @Param(value = "rows") int rows);
	
	@Insert("insert into phrase (id, description, create_time, update_time, school_id) values (#{id}, #{description}, now(), now(), #{schoolId})")
	public void insert(Phrase phrase);

	@Update("update phrase set description=#{description}, update_time=now() where id=#{id}")
	public void update(Phrase phrase);

	@Delete("delete from phrase where id=#{id}")
	public void delete(long id);

}
