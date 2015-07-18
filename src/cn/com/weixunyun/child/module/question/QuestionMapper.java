package cn.com.weixunyun.child.module.question;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;


public interface QuestionMapper {
	@Delete({ "delete from question", "where id = #{id,jdbcType=NUMERIC}" })
	void delete(Long id);

	@Insert({ "insert into question (id, create_user_id, ", "title, question, ", "update_user_id, update_time, ",
			"answer, create_time)", "values (#{id,jdbcType=NUMERIC}, #{createUserId}, ",
			"#{title,jdbcType=VARCHAR}, #{question,jdbcType=VARCHAR}, ",
			"#{updateUserId}, #{updateTime,jdbcType=TIMESTAMP}, ",
			"#{answer,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP})" })
	void insert(Question record);
	/*
	 * @InsertProvider(type=QuestionSqlProvider.class, method="insertSelective")
	 * int insertSelective(Question record);
	 */

	@Select({
			"select s.*,t.name create_user,u.name update_user from question s" +
			" left join v_user t on create_user_id=t.id left join v_user u on update_user_id=u.id",
			"where s.id = #{id,jdbcType=NUMERIC} " })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.NUMERIC, id = true),
			@Result(column = "create_user_id", property = "createUserId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
			@Result(column = "question", property = "question", jdbcType = JdbcType.VARCHAR),
			@Result(column = "update_user_id", property = "updateUserId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "answer", property = "answer", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP) })
	Question select(Long id);

	/*
	 * @UpdateProvider(type=QuestionSqlProvider.class,
	 * method="updateByPrimaryKeySelective") int
	 * updateByPrimaryKeySelective(Question record);
	 */

	@Update({ "update question", "set create_user_id = #{createUserId},",
			"title = #{title,jdbcType=VARCHAR},", "question = #{question,jdbcType=VARCHAR},",
			"update_user_id = #{updateUserId},", "update_time = #{updateTime,jdbcType=TIMESTAMP},",
			"answer = #{answer,jdbcType=VARCHAR}",
			"where id = #{id,jdbcType=NUMERIC}" })
	void update(Question record);
	
	@Update({ "update question", "set ",
		"update_user_id = #{updateUserId},", "update_time = #{updateTime,jdbcType=TIMESTAMP},",
		"answer = #{answer,jdbcType=VARCHAR}",
		"where id = #{id,jdbcType=NUMERIC}" })
	void answer(Question record);

	@SelectProvider(type = QuestionMapperProvider.class, method = "selectCount")
	public int count(Map<String, Object> params);

	@SelectProvider(type = QuestionMapperProvider.class, method = "select")
	public List<Question> list(Map<String, Object> params);
}