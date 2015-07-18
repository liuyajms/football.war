package cn.com.weixunyun.child.module.elective;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class ElectiveMapperProvider extends AbstractMapperProvider {

	public String selectStudentSql(Map<String, Object> paramMap) {
		StringBuilder sb = new StringBuilder(512);

		sb.append("select e.*, \n");
		sb.append("       (select count(*) from elective_student es where es.elective_id = e.id) as choosedCount, \n");
		sb.append("       t.name as teacherName, \n");
		sb.append("       es.id is not null as choosed \n");
		sb.append("  from elective e \n");
		sb.append("  left join elective_student es \n");
		sb.append("    on es.elective_id = e.id \n");
		sb.append("   and es.student_id = #{studentId} \n");
		sb.append("  left join teacher t \n");
		sb.append("    on t.id = e.teacher_id \n");
		sb.append(" where e.school_id = #{schoolId} \n");
		sb.append("   and e.term = #{term} \n");

		Integer grade = (Integer) paramMap.get("grade");
		if (grade != null) {
			sb.append("   and (e.grade & (1 << " + grade + ")) > 0 \n");
		}

		return sb.toString();
	}

	public String selectSql(Map<String, Object> paramMap) {
		StringBuilder sb = new StringBuilder(512);

		sb.append("select distinct e.id, e.name, e.date, e.num, e.time_begin, e.time_end, e.term, e.grade, \n");
		sb.append("       (select count(*) from elective_student es where es.elective_id = e.id) as choosedCount, \n");
		sb.append("       t.name as teacherName \n");
		sb.append("  from elective e \n");
		sb.append("  left join teacher t \n");
		sb.append("    on t.id = e.teacher_id \n");
		sb.append(" where e.school_id = #{schoolId} \n");
		sb.append("   and e.term = #{term} \n");

		return sb.toString();
	}

	public String select(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap), "e.id");
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}

	public String selectStudent(Map<String, Object> paramMap) {
		return super.page(selectStudentSql(paramMap), "e.id");
	}

}
