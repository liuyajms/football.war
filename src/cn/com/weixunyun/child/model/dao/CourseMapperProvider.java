package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class CourseMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select id, name, description from course where school_id=#{schoolId} ");
		String keyword = (String) paramMap.get("keyword");
		if (keyword != null && !"".equals(keyword)) {
			sb.append("and name like '%'||#{keyword}||'%'");
		}

		return sb.toString();
	}

	public String select(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap), "id");
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}
}
