package cn.com.weixunyun.child.model.dao;

import java.util.List;
import java.util.Map;

public class UserMapperProvider extends AbstractMapperProvider {
	public String select(Map<String, Object> paramMap) {
		List<String> nameList = (List<String>) paramMap.get("nameList");

		StringBuilder names = new StringBuilder();
		for (int i = 0; i < nameList.size(); i++) {
			if (i > 0) {
				names.append(",");
			}
			names.append("'" + nameList.get(i).replace("'", "") + "'");
		}

		StringBuilder sql = new StringBuilder(512);
		sql.append("select t.id \n");
		sql.append("  from classes_teacher ct \n");
		sql.append("  join teacher t \n");
		sql.append("    on t.id = ct.teacher_id \n");
		sql.append(" where classes_id = #{classesId} \n");
		sql.append("   and t.name in (" + names.toString() + ") \n");
		sql.append("union all \n");
		sql.append("select p.id \n");
		sql.append("  from parents p \n");
		sql.append("  join student s \n");
		sql.append("    on s.id = p.student_id \n");
		sql.append(" where s.classes_id = #{classesId} \n");
		sql.append("   and (p.name in (" + names.toString() + ") or concat(s.name, '的', p.type) in ("
				+ names.toString() + ")) \n");

		System.out.println(sql.toString());
		return sql.toString();
	}

}
