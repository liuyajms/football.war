package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class ChatMapperProvider extends AbstractMapperProvider {

	public String getListSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select a.*, \n");
		sql.append("       b.name as user_name, \n");
		sql.append("       b.type as user_type \n");
		sql.append("  from chat a \n");
		sql.append("  left join v_user_classes b \n");
		sql.append("    on a.user_id = b.id \n");
		sql.append("   and (b.classes_id is null or b.classes_id = a.classes_id) \n");
		sql.append(" where a.school_id = #{schoolId} \n");
		sql.append("      and a.classes_id = #{classesId} \n");
		
		if (paramMap.get("time") != null) {
			sql.append("      and to_char(a.time, 'yyyyMMdd hh:mm:ss') > #{time} \n");
		}
		
		return sql.toString();
	}

	public String getList(Map<String, Object> paramMap) {
		return super.page(getListSql(paramMap), "a.id");
	}

	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(getListSql(paramMap));
	}

}
