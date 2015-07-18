package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class ClassesTeacherMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select tct.teacher_id, tct.course_id, tct.classes_id, \n");
		sb.append("		  tc.name as classesName, \n");
		sb.append("		  tt.name as teacherName, \n");
		sb.append("		  tco.name as courseName from classes_teacher tct \n");
		sb.append(" left join classes tc on tct.classes_id = tc.id \n");
		sb.append(" left join teacher tt on tct.teacher_id = tt.id \n");
		sb.append(" left join course tco on tct.course_id = tco.id \n");
		sb.append(" where 1 = 1 \n");
		
		if (paramMap.containsKey("classesId")) {
			sb.append("   and tct.classes_id = #{classesId} \n");
		}
		
		String keyword = (String) paramMap.get("keyword");
		
		if(keyword != null && !"".equals(keyword)){
			sb.append(" and (tt.name like '%'||#{keyword}||'%' or tco.name like '%'||#{keyword}||'%')");
		}
		
		sb.append("order by tct.course_id desc limit #{rows} offset #{offset}");

		return sb.toString();
	}

	public String select(Map<String, Object> paramMap) {
		return selectSql(paramMap);
	}

	public String selectCount(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from classes_teacher tct \n");
		sb.append(" left join classes tc on tct.classes_id = tc.id \n");
		sb.append(" left join teacher tt on tct.teacher_id = tt.id \n");
		sb.append(" left join course tco on tct.teacher_id = tco.id \n");
		sb.append(" where tct.classes_id = #{classesId} \n");
		
		String keyword = (String) paramMap.get("keyword");
		
		if(keyword != null && !"".equals(keyword)){
			sb.append(" and (tt.name like '%'||#{keyword}||'%' or tco.name like '%'||#{keyword}||'%')");
		}

		return sb.toString();
	}
	
	public String selectTeacherClasses(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ct.*, c.name as classesName, cs.name as courseName , t.name as teacherName \n");
		sql.append("  from classes_teacher ct \n");
		sql.append("  join classes c \n");
		sql.append("    on c.id = ct.classes_id \n");
		sql.append("  join course cs \n");
		sql.append("    on cs.id = course_id \n");
		sql.append("  join teacher t \n");
		sql.append("    on t.id = ct.teacher_id \n");
		sql.append(" where ct.classes_id = #{classesId} \n");
		sql.append("   and ct.school_id = #{schoolId} \n");
		sql.append("   and ct.teacher_id = #{teacherId} \n");
		
		sql.append("order by ct.course_id desc limit #{rows} offset #{offset}");

		return sql.toString();
	}
	
	public String selectTeacherClassesCount(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from classes_teacher tct \n");
		sb.append(" left join classes tc on tct.classes_id = tc.id \n");
		sb.append(" left join teacher tt on tct.teacher_id = tt.id \n");
		sb.append(" left join course tco on tct.teacher_id = tco.id \n");
		sb.append(" where tct.classes_id = #{classesId} \n");
		sb.append("   and tct.school_id = #{schoolId} \n");
		sb.append("   and tct.teacher_id = #{teacherId} \n");
		
		return sb.toString();
	}
}
