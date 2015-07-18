package cn.com.weixunyun.child.module.personal.photo;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

import java.util.Map;

public class PhotoMapperProvider extends AbstractMapperProvider {

	private String selectSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select p.*, u.name as createUserName \n");
		sb.append("  from photo p \n");
		sb.append("  join v_user_singleton u \n");
		sb.append("    on u.id = p.create_user_id \n");
		sb.append(" where p.create_user_id = #{userId} \n");
		
		if (paramMap.get("keyword") != null && !"".equals(paramMap.get("keyword"))) {
			sb.append("   and (p.name like '%' || #{keyword} || '%' or p.description like '%' || #{description} || '%') \n");
		}
		
		return sb.toString();
		
	}

	public String getList(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap), "p.id");
	}
	
	
	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}
}
