package cn.com.weixunyun.child.module.security;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class SecurityRecordMapperProvider extends AbstractMapperProvider {

	public String selectAllSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select sr.*, dv.name as device_name, u.name as userName, u.type as userType \n");
		sb.append("  from security_record sr \n");
		sb.append("  left join dictionary_value dv \n");
		sb.append("    on dv.school_id = #{schoolId} \n");
		sb.append("   and dv.dictionary_table_code = 'security' \n");
		sb.append("   and dv.dictionary_field_code = 'device' \n");
		sb.append("   and dv.code = sr.device \n");
		sb.append("  left join v_user_singleton u \n");
		sb.append("    on u.id = sr.user_id \n");
		sb.append(" where sr.school_id = #{schoolId} \n");
		sb.append("   and sr.term = #{term} \n");
		if (paramMap.get("userId") != null) {
			sb.append("   and sr.user_id = #{userId} \n");
		}
		return sb.toString();
	}

	public String selectAll(Map<String, Object> paramMap) {
		return super.page(selectAllSql(paramMap), "sr.id");
	}

	public String selectAllCount(Map<String, Object> paramMap) {
		return super.pageCount(selectAllSql(paramMap));
	}

}
