package cn.com.weixunyun.child.module.course.evaluation;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class CourseEvaluationMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ck.*, s.name as studentName, cs.name as courseName, c.name as classesName, t.name as createTeacherName  \n");
		sql.append("  from course_evaluation ck \n");
		sql.append("  join student s \n");
		sql.append("    on s.id = ck.student_id \n");
		sql.append("  join classes c \n");
		sql.append("    on c.id = s.classes_id \n");
		sql.append("  join course cs \n");
		sql.append("    on cs.id = ck.course_id \n");
		sql.append("  join teacher t \n");
		sql.append("    on t.id = ck.create_teacher_id \n");
		sql.append(" where ck.school_id = #{schoolId}  \n");
		sql.append("   and ck.course_id = #{courseId}  \n");
		sql.append("   and ck.term = #{term}  \n");
		
		Object keyword = paramMap.get("classesId");
		
		if(keyword != null && !"".equals(keyword.toString())){
			sql.append("   and s.classes_id = #{classesId}  \n");
		}

		return sql.toString();
	}

	public String select(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap), "id");
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}
	
	public String selectTodayTeacherSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ck.*, s.name as studentName, cs.name as courseName, c.name as classesName, t.name as createTeacherName  \n");
		sql.append("  from course_evaluation ck \n");
		sql.append("  join student s \n");
		sql.append("    on s.id = ck.student_id \n");
		sql.append("  join classes c \n");
		sql.append("    on c.id = s.classes_id \n");
		sql.append("  join course cs \n");
		sql.append("    on cs.id = ck.course_id \n");
		sql.append("  join teacher t \n");
		sql.append("    on t.id = ck.create_teacher_id \n");
		sql.append(" where ck.school_id = #{schoolId}  \n");
		sql.append("   and ck.course_id = #{courseId}  \n");
		sql.append("   and ck.term = #{term}  \n");
		sql.append("   and s.classes_id = #{classesId}  \n");
		sql.append("   and to_char(ck.create_time, 'yyyy-MM-dd') = to_char(current_date, 'yyyy-MM-dd')  \n");

		return sql.toString();
	}
	
	public String selectTodayTeacher(Map<String, Object> paramMap) {
		return super.page(selectTodayTeacherSql(paramMap), "ck.id");
	}

	public String selectTodayTeacherCount(Map<String, Object> paramMap) {
		return super.pageCount(selectTodayTeacherSql(paramMap));
	}
	
	public String selectTodayStudentSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ck.*, s.name as studentName, cs.name as courseName, c.name as classesName, t.name as createTeacherName  \n");
		sql.append("  from course_evaluation ck \n");
		sql.append("  join student s \n");
		sql.append("    on s.id = ck.student_id \n");
		sql.append("  join classes c \n");
		sql.append("    on c.id = s.classes_id \n");
		sql.append("  join course cs \n");
		sql.append("    on cs.id = ck.course_id \n");
		sql.append("  join teacher t \n");
		sql.append("    on t.id = ck.create_teacher_id \n");
		sql.append(" where ck.school_id = #{schoolId}  \n");
		
		if (paramMap.get("courseId") != null) {
			sql.append("   and ck.course_id = #{courseId}  \n");
		}
		
		sql.append("   and ck.term = #{term}  \n");
		sql.append("   and ck.student_id = #{studentId}  \n");
		sql.append("   and to_char(ck.create_time, 'yyyy-MM-dd') = to_char(current_date, 'yyyy-MM-dd')  \n");
		
		return sql.toString();
	}
	
	public String selectTodayStudent(Map<String, Object> paramMap) {
		return super.page(selectTodayStudentSql(paramMap), "ck.id");
	}

	public String selectTodayStudentCount(Map<String, Object> paramMap) {
		return super.pageCount(selectTodayStudentSql(paramMap));
	}
	
}
