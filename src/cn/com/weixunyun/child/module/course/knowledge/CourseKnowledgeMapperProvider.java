package cn.com.weixunyun.child.module.course.knowledge;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class CourseKnowledgeMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ck.*, c.name as classesName, cs.name as courseName, t.name as createTeacherName \n");
		sql.append("  from course_knowledge ck \n");
		sql.append("  join classes c \n");
		sql.append("    on c.id = ck.classes_id \n");
		sql.append("  join course cs \n");
		sql.append("    on cs.id = ck.course_id \n");
		sql.append("  join teacher t \n");
		sql.append("    on t.id = ck.create_teacher_id \n");
		sql.append(" where ck.school_id = #{schoolId} \n");
		sql.append("   and ck.classes_id = #{classesId} \n");
		sql.append("   and ck.term = #{term} \n");
		
		if (paramMap.get("courseId") != null) {
			sql.append("   and ck.course_id = #{courseId} \n");
		}
		
		return sql.toString();
	}

	public String select(Map<String, Object> paramMap) {
		return selectSql(paramMap) + " order by  cs.name desc, ck.id desc limit #{rows} offset #{offset} ";
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}
	
	public String selectExport(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ck.*, c.name as classesName, cs.name as courseName, t.name as createTeacherName  \n");
		sql.append("  from course_knowledge ck \n");
		sql.append("  join classes c \n");
		sql.append("    on c.id = ck.classes_id \n");
		sql.append("  join course cs \n");
		sql.append("    on cs.id = ck.course_id \n");
		sql.append("  join teacher t \n");
		sql.append("    on t.id = ck.create_teacher_id \n");
		sql.append(" where ck.school_id = #{schoolId}  \n");
		sql.append("   and ck.classes_id = #{classesId}  \n");
		sql.append("   and ck.course_id = #{courseId}  \n");
		sql.append("   and ck.term = #{term}  \n");
		sql.append(" order by ck.id desc \n");

		return sql.toString();
	}
}
