package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class MultiMapperProvider {

	public String getStudentSummaryList(Map<String, Object> paramMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("select s.name, count(s.id) as count ");
		sb.append("  from student s ");
		sb.append(" where s.school_id = #{schoolId} ");
		sb.append(" group by s.name ");
		sb.append("having count(s.id) > 1 ");
		return sb.toString();
	}

	public String getStudentList(Map<String, Object> paramMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("select s.*, c.name as classes_name, v.name as gender_name ");
		sb.append("  from student s ");
		sb.append("  join classes c \n");
		sb.append("    on c.id = s.classes_id \n");
		sb.append("  left join dictionary_value v \n");
		sb.append("    on v.code = s.gender \n");
		sb.append("   and v.school_id = #{schoolId} \n");
		sb.append("   and v.dictionary_field_code = 'gender' \n");
		sb.append("   and v.dictionary_table_code = 'student' \n");
		sb.append(" where s.school_id = #{schoolId} and s.name = #{name} ");
		System.out.println(sb.toString());
		return sb.toString();
	}

	public String getParentsSummaryList(Map<String, Object> paramMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("select p.name, count(p.id) as count ");
		sb.append("  from parents p ");
		sb.append("  join student_parents sp ");
		sb.append("    on sp.parents_id = p.id and sp.school_id = #{schoolId} ");
		sb.append(" group by p.name ");
		sb.append("having count(distinct p.id) > 1 ");
		return sb.toString();
	}

	public String getParentsList(Map<String, Object> paramMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("select p.id, p.name, sp.username, ");
		sb.append("       s.name as student_name, s.birthday, c.name as classes_name, v.name as gender_name ");
		sb.append("   from parents p ");
		sb.append("  join student_parents sp ");
		sb.append("  join student s ");
		sb.append("  join classes c \n");
		sb.append("    on c.id = s.classes_id \n");
		sb.append("  left join dictionary_value v \n");
		sb.append("    on v.code = s.gender \n");
		sb.append("   and v.school_id = #{schoolId} \n");
		sb.append("   and v.dictionary_field_code = 'gender' \n");
		sb.append("   and v.dictionary_table_code = 'student' \n");
		sb.append("    on s.id = sp.student_id ");
		sb.append("    on sp.parents_id = p.id and sp.school_id = #{schoolId} ");
		sb.append(" where p.name = #{name} ");
		return sb.toString();
	}

}
