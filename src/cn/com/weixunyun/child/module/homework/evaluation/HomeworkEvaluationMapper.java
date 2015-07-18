package cn.com.weixunyun.child.module.homework.evaluation;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface HomeworkEvaluationMapper {

	@Select("select * from homework_evaluation where id = #{id}")
	public HomeworkEvaluation get(Long id);

	@SelectProvider(type = HomeworkEvaluationMapperProvider.class, method = "getListCount")
	public int getListCount(@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term, 
			@Param(value = "classesId") Long classesId, @Param(value = "studentId") Long studentId, 
			@Param(value = "date") String date, @Param(value = "keyword") String keyword);

	@SelectProvider(type = HomeworkEvaluationMapperProvider.class, method = "getList")
	public List<HomeworkEvaluation> getList(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "schoolId") Long schoolId, @Param(value = "term") String term, 
			@Param(value = "classesId") Long classesId, @Param(value = "studentId") Long studentId, 
			@Param(value = "date") String date, @Param(value = "keyword") String keyword);

	@Insert("insert into homework_evaluation (id, classes_id, student_id, date, description, create_user_id, create_time, update_user_id, update_time, school_id, term) " +
			"values (#{id}, #{classesId}, #{studentId}, #{date}, #{description}, #{createUserId}, #{createTime}, #{updateUserId}, #{updateTime}, #{schoolId}, #{term})")
	public void insert(HomeworkEvaluation evaluation);

	@Update("update homework_evaluation set description = #{description}, update_user_id = #{updateUserId}, update_time = #{updateTime} where id = #{id}")
	public void update(HomeworkEvaluation evaluation);

	@Delete("delete from homework_evaluation where id = #{id}")
	public void delete(Long id);
	
}
