package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class DictionaryValueMapperProvider {

	public String queryList(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from dictionary_value " +
				"where school_id = #{schoolId}" +
				"and dictionary_table_code = #{table} " +
				"and dictionary_field_code = #{field}");
		sb.append(" order by ord ");
		int offset = (Integer) paramMap.get("offset");
		int rows = (Integer) paramMap.get("rows");
		if (offset != 0 && rows != 0) {
			sb.append(" limit #{rows} offset #{offset}");
		}
		
		return sb.toString();
	}
	
	public String getValueList(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from dictionary_value where school_id = #{schoolId} ");
		String table = (String) paramMap.get("tableCode");
		String field = (String) paramMap.get("fieldCode");
		if (table != null && !"".equals(table) && field != null && !"".equals(field)) {
			sb.append(" and dictionary_table_code = #{tableCode} ");
			sb.append(" and dictionary_field_code = #{fieldCode} ");
		}
		sb.append(" order by ord ");
		return sb.toString();
	}
	
	public String getValueListCount(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from (");
		sb.append(getValueList(paramMap));
		sb.append(" ) t ");
		return sb.toString();
	}
	
}
