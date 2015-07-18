package cn.com.weixunyun.child.module.homework.evaluation;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class HomeworkEvaluationMapperProvider extends AbstractMapperProvider {

	public String getListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * \n");
		sb.append("  from homework_evaluation \n");
		sb.append(" where school_id = #{schoolId} \n");
		sb.append("   and term = #{term} \n");
		sb.append("   and classes_id = #{classesId} \n");
		sb.append("   and student_id = #{studentId} \n");
		sb.append("   and to_char(date, 'yyyyMMdd') = #{date} \n");
		if (paramMap.get("keyword") != null) {
			sb.append("   and description like '%' || #{keyword} || '%' \n");
		}
		return sb.toString();
	}

	public String getList(Map<String, Object> paramMap) {
		return super.page(getListSql(paramMap), "id");
	}

	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(getListSql(paramMap));
	}
}
