package cn.com.weixunyun.child.module.elective;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class ElectiveStudentMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from elective_student where school_id = #{schoolId} and term = #{term} ");
		String keyword = (String) paramMap.get("keyword");
		if (keyword != null && !"".equals(keyword)) {
			sb.append("and score_description like '%'||#{keyword}||'%'");
		}

		return sb.toString();
	}

	public String select(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap), "id");
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}

	public String selectStudent(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select e.*, c.name as electiveName , t.name as teacherName, c.date as electiveDate, e.score as score, e.score_description as scoreDescription \n");
		sql.append("  from elective_student e \n");
		sql.append("  join elective c \n");
		sql.append("    on c.id = e.elective_id \n");
		sql.append("  join teacher t \n");
		sql.append("    on t.id = c.teacher_id \n");
		sql.append(" where e.student_id = #{studentId}  \n");
		sql.append("   and e.term = #{term}  \n");
		return super.page(sql.toString(), "e.id");
	}

	public String selectElective(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select e.*, s.name as studentName , c.name as electiveName, c.date as electiveDate \n");
		sql.append("  from elective_student e \n");
		sql.append("  join elective c \n");
		sql.append("    on c.id = e.elective_id \n");
		sql.append("  join student s \n");
		sql.append("    on s.id = e.student_id \n");
		sql.append(" where e.elective_id = #{electiveId}  \n");
		sql.append("    and e.term = #{term} \n");
		sql.append("    and e.school_id = #{schoolId} \n");
		sql.append("    and (s.name like '%'||#{keyword}||'%' or e.score like '%'||#{keyword}||'%' or e.score_description like '%'||#{keyword}||'%' ) \n");
		sql.append(" order by e.id \n");
		sql.append("  limit #{rows} offset #{offset} \n");

		return sql.toString();
	}

	public String selectElectiveCount(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select count(*) as count \n");
		sql.append("  from elective_student e \n");
		sql.append("  join elective c \n");
		sql.append("    on c.id = e.elective_id \n");
		sql.append("  join student s \n");
		sql.append("    on s.id = e.student_id \n");
		sql.append(" where e.elective_id = #{electiveId}  \n");
		sql.append("    and e.term = #{term} \n");
		sql.append("    and e.school_id = #{schoolId} \n");
		System.out.println(sql.toString());
		System.out.println();
		return sql.toString();
	}
}
