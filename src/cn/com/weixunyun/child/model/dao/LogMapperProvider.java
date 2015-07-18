package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class LogMapperProvider {

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
