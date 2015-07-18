package cn.com.weixunyun.child.module.star;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class StarMapperProvider extends AbstractMapperProvider {

	public String getListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select s.*, c.name as classesName ");
		sb.append("  from star s");
		sb.append("  left join classes c");
		sb.append("    on s.classes_id = c.id");
		sb.append(" where s.school_id = #{schoolId} ");
		sb.append("   and s.classes_id = #{classesId} ");
		return sb.toString();
	}

	public String getList(Map<String, Object> paramMap) {
		Long rows = (Long) paramMap.get("rows");
		if (rows == -1) {
			return getListSql(paramMap);
		} else {
			return super.page(getListSql(paramMap), "s.id");
		}
	}

	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(getListSql(paramMap));
	}
	
	/*public String get(Long id) {
		System.out.println("-------------sql-------------");
		StringBuffer sb = new StringBuffer();
		sb.append("select s.*, c.name as classesName, t.name as createTeacherName ");
		sb.append("  from star s");
		sb.append("   join classes c");
		sb.append("    on s.classes_id = c.id");
		sb.append("   join teacher t");
		sb.append("    on s.create_teacher_id = c.id");
		sb.append(" where s.id = #{id} ");
		return sb.toString();
	}*/
}
