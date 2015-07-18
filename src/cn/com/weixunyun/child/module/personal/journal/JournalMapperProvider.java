package cn.com.weixunyun.child.module.personal.journal;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

import java.util.Map;

public class JournalMapperProvider extends AbstractMapperProvider {

	private String selectSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(1024);
		sql.append("select t.* , v.name as createUserName" +
                        " from journal t  " +
                " left join v_user_singleton v on v.id=t.create_user_id where t.create_user_id=#{userId} "
                + " and t.school_id=#{schoolId}" );

		Object keyword = paramMap.get("keyword");

		if (keyword != null && !"".equals(keyword)) {
			sql.append(" and t.description like '%'||#{keyword}||'%' ");
		}
		
		return sql.toString();
		
	}

	public String getList(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap), "id");
	}
	
	
	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}
}
