package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.com.weixunyun.child.model.pojo.Feedback;

public interface FeedbackMapper {

	@Select("select * from feedback where id = #{id}")
	public Feedback select(Long id);

	@Select("select count(id) from feedback ")
	public int selectAllCount();

	@Select("select * from feedback order by id desc limit #{rows} offset #{offset} ")
	public List<Feedback> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows);

	@Insert("insert into feedback (id, user_id, date, module, module_id, description, school_id) values (#{id}, #{userId}, #{date}, #{module}, #{moduleId}, #{description}, #{schoolId})")
	public void insert(Feedback feedback);

	@Update("update feedback set description = #{description}, module = #{module}, module_id = #{moduleId} where id = #{id}")
	public void update(Feedback feedback);

	@Delete("delete from feedback where id = #{id}")
	public void delete(Long id);

}
