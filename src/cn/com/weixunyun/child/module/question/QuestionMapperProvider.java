package cn.com.weixunyun.child.module.question;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

import java.util.Map;

public class QuestionMapperProvider extends AbstractMapperProvider{

	public String selectSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select s.*,t.name create_user,u.name update_user from question s left join v_user t on create_user_id=t.id left join v_user u on update_user_id=u.id where t.school_id = #{schoolId}");
		
		String keyword = (String) paramMap.get("keyword");
		
		if (keyword != null && !"".equals(keyword)) {
			sb.append(" and s.title like '%'||#{keyword}||'%'");
		}

		return sb.toString();
	}

	public String select(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap), "id");
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}
	
	
}