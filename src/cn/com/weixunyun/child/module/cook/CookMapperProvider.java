package cn.com.weixunyun.child.module.cook;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class CookMapperProvider extends AbstractMapperProvider {

	public String getListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * \n");
		sb.append("  from cook \n");
		sb.append(" where school_id = #{schoolId} \n");
		sb.append("   and term = #{term} \n");
		if (paramMap.get("keyword") != null) {
			sb.append("   and name like '%' || #{keyword} || '%' \n");
		}
		return sb.toString();
	}

	public String getList(Map<String, Object> paramMap) {
		return super.page(getListSql(paramMap), "id");
	}

	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(getListSql(paramMap));
	}
}
