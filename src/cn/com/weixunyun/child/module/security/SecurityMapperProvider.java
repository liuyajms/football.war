package cn.com.weixunyun.child.module.security;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class SecurityMapperProvider extends AbstractMapperProvider {

	public String getTermSecurityListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select s.name as student_name, se.* \n");
		sb.append("  from security se \n");
		sb.append("  join student s \n");
		sb.append("    on s.id = se.student_id \n");
		sb.append(" where se.school_id = #{schoolId} \n");
		sb.append("   and se.term = #{term} \n");
		
		if (paramMap.get("classesId") !=null && !"".equals(paramMap.get("classesId"))) {
			sb.append(" and classes_id = #{classesId} \n");
		}
		
		sb.append("   and (se.reach_over or se.leave_over) \n");
		return sb.toString();
	}

	public String getTermSecurityList(Map<String, Object> paramMap) {
		return  getTermSecurityListSql(paramMap) + " order by s.name, se.date limit #{rows} offset #{offset} ";
	}

	public String getTermSecurityListCount(Map<String, Object> paramMap) {
		return super.pageCount(getTermSecurityListSql(paramMap));
	}

}
