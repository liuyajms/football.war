package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class SchoolMapperProvider extends AbstractMapperProvider {

	public String select(Map<String, Object> paramMap) {

		StringBuffer sb = new StringBuffer();
		sb.append("select id, name, phone, email, address from school");
		String keyword = (String) paramMap.get("keyword");
		
		if (keyword != null && !"".equals(keyword)) {
			sb.append(" where name like '%'||#{keyword}||'%' ");
		}
		
		return sb.toString();
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(select(paramMap));
	}

	public String selectPage(Map<String, Object> paramMap) {
		
		return super.page(select(paramMap), "id");
	}

}
