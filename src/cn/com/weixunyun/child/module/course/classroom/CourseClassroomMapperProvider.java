package cn.com.weixunyun.child.module.course.classroom;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class CourseClassroomMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("select cc.*, c.name as classesName, cs.name as courseName, t.name as createTeacherName \n");
		sql.append("  from course_classroom cc \n");
		sql.append("  join classes c \n");
		sql.append("    on c.id = cc.classes_id \n");
		sql.append("  join course cs \n");
		sql.append("    on cs.id = cc.course_id \n");
		sql.append("  join teacher t \n");
		sql.append("    on t.id = cc.create_teacher_id \n");
		sql.append(" where cc.school_id = #{schoolId} \n");
		sql.append("   and cc.classes_id = #{classesId} \n");
		sql.append("   and cc.term = #{term} \n");
		
		if (paramMap.get("courseId") != null) {
			sql.append("   and cc.course_id = #{courseId} \n");
		}
		
		return sql.toString();
	}

	public String select(Map<String, Object> paramMap) {
		return selectSql(paramMap) + " order by  cs.name desc, cc.id desc limit #{rows} offset #{offset} ";
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}
}
