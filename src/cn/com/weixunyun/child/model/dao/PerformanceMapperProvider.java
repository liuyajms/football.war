package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class PerformanceMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select p.id, p.description, p.create_time as createTime, s.name as studentName, t.name as teacherName from"
				+ " performance p left join student s on s.id = p.student_id "
				+ " left join teacher t on t.id = p.create_teacher_id "
				+ " where  p.school_id=#{schoolId}");
		String con = (String) paramMap.get("con");
		if (con != null) {
			sb.append(" and (p.description like '%'||#{con}||'%' or s.name like '%'||#{con}||'%' or t.name like '%'||#{con}||'%')");
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
