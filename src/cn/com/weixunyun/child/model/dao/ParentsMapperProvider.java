package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class ParentsMapperProvider {
	public String getAllParents(Map<String, Object> paramMap) {

		StringBuffer sb = new StringBuffer();
		sb.append("select p.*, dv.name as typeName, s.name || '的' || dv.name as displayName \n");
		sb.append("  from parents p \n");
		sb.append("  join student_parents sp \n");
		sb.append("    on p.id = sp.parents_id \n");
		sb.append("  join student s \n");
		sb.append("    on s.id = sp.student_id \n");
		sb.append("  left join dictionary_value dv \n");
		sb.append("    on sp.type = dv.code \n");
		sb.append("   and dv.school_id = #{schoolId}  \n");
		sb.append("   and dv.dictionary_table_code = 'parents'  \n");
		sb.append("   and dv.dictionary_field_code = 'type'  \n");
		sb.append("where sp.student_id = #{studentId} ");

		String keyword = (String) paramMap.get("keyword");
		if (keyword != null) {
			sb.append(" and p.name like '%'||#{keyword}||'%'");
		}
		sb.append("order by p.id limit #{rows} offset #{offset}");
		return sb.toString();
	}

	public String getAllParentsCount(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(p.*) from parents p");
		sb.append("  join student_parents sp \n");
		sb.append("    on p.id = sp.parents_id \n");
		sb.append("where sp.student_id = #{studentId}");
		String keyword = (String) paramMap.get("keyword");
		if (keyword != null) {
			sb.append(" and p.name like '%'||#{keyword}||'%'");
		}
		return sb.toString();
	}

	public String getSchoolParents(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select p.* \n");
		sb.append("  from parents p \n");
		sb.append("  join student_parents sp \n");
		sb.append("  join student s \n");
		sb.append("    on s.id = sp.student_id on sp.parents_id = p.id \n");
		sb.append(" where s.school_id = #{schoolId} \n");
		sb.append("   and sp.username = #{username} \n");
		sb.append("   and p.password = #{password} limit 1 \n");
		return sb.toString();
	}

	public String getStudentList(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select s.*, c.name as classes_name, c.year \n");
		sb.append("  from student_parents sp \n");
		sb.append("  join student s \n");
		sb.append("  join classes c \n");
		sb.append("    on c.id = s.classes_id on s.id = sp.student_id \n");
		sb.append(" where s.school_id = #{schoolId} \n");
		sb.append("   and sp.parents_id = #{parentsId} \n");
		sb.append(" order by s.id desc \n");
		return sb.toString();
	}
}
