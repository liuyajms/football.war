package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class ClassesMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.*, t.name as teacherName \n");
		sb.append("  from classes c \n");
		sb.append("  left join teacher t \n");
		sb.append("    on c.teacher_id = t.id \n");
		sb.append(" where c.school_id = #{schoolId} \n");
		String classesName = (String) paramMap.get("classesName");
		if (classesName != null) {
			sb.append("   and (c.name like '%' || #{classesName} || '%' or c.description like '%' || #{classesName} || '%') \n");
		}

		return sb.toString();
	}

	public String selectClass(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select s.*, \n");
		sb.append("  case s.gender when '1' then '男' when '2' then '女' end as genderName, \n");
		sb.append("       c.school_id, c.name as classesName \n");
		sb.append("  from student s \n");
		sb.append("  join classes c on c.id = s.classes_id \n");
		sb.append(" where 1 = 1 \n");
		Long classesId = (Long) paramMap.get("classesId");
		if (classesId.compareTo(0L) != 0) {
			sb.append("   and s.classes_id = #{classesId} \n");
		}
		sb.append(" order by c.year asc, c.num asc limit #{rows} offset #{offset} \n");

		return sb.toString();
	}

	public String selectClassCount(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) \n");
		sb.append("  from student s \n");
		sb.append("  join classes c on c.id = s.classes_id \n");
		sb.append(" where 1 = 1 \n");
		Long classesId = (Long) paramMap.get("classesId");
		if (classesId.compareTo(0L) != 0) {
			sb.append("   and s.classes_id = #{classesId} \n");
		}

		return sb.toString();
	}

	public String select(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap) + " order by c.year asc, c.num asc ");
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}

}
