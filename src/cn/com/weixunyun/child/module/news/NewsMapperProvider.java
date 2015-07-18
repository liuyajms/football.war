package cn.com.weixunyun.child.module.news;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class NewsMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select school_id, id, title, description_summary as descriptionSummary, update_time as updateTime from news where school_id = #{schoolId}");
		String keyword = (String) paramMap.get("keyword");
		if (keyword != null && !"".equals(keyword)) {
			sb.append(" and title like '%'||#{keyword}||'%' ");
		}
		Long type = (Long) paramMap.get("type");
		if (type != null) {
			sb.append(" and type=#{type}");
		}
		Boolean pic = (Boolean) paramMap.get("pic");
		if (pic != null) {
			sb.append(" and pic=#{pic}");
		}
		return sb.toString();
	}

	public String select(Map<String, Object> paramMap) {
		Long rows = (Long) paramMap.get("rows");
		if (rows == -1) {
			return selectSql(paramMap) + " order by id desc";
		} else {
			return super.page(selectSql(paramMap), "id");
		}
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}
}
