package cn.com.weixunyun.child.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface MultiMapper {
	@SelectProvider(type = MultiMapperProvider.class, method = "getStudentSummaryList")
	public List<Map<String, ?>> getStudentSummaryList(@Param(value = "schoolId") Long schoolId);

	@SelectProvider(type = MultiMapperProvider.class, method = "getStudentList")
	public List<Map<String, ?>> getStudentList(@Param(value = "schoolId") Long schoolId,
			@Param(value = "name") String name);

	@SelectProvider(type = MultiMapperProvider.class, method = "getParentsSummaryList")
	public List<Map<String, ?>> getParentsSummaryList(@Param(value = "schoolId") Long schoolId);

	@SelectProvider(type = MultiMapperProvider.class, method = "getParentsList")
	public List<Map<String, ?>> getParentsList(@Param(value = "schoolId") Long schoolId,
			@Param(value = "name") String name);

	@Update("update student_parents set student_id = #{newId} where school_id = #{schoolId} and student_id = #{oldId} ")
	public void updateStudent(@Param(value = "schoolId") Long schoolId, @Param(value = "oldId") Long oldId,
			@Param(value = "newId") Long newId);

	@Update("update student_parents set parents_id = #{newId} where school_id = #{schoolId} and parents_id = #{oldId} ")
	public void updateParents(@Param(value = "schoolId") Long schoolId, @Param(value = "oldId") Long oldId,
			@Param(value = "newId") Long newId);

}
