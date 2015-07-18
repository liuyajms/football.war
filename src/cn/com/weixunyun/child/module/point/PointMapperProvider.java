package cn.com.weixunyun.child.module.point;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class PointMapperProvider extends AbstractMapperProvider {

	public String getListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * \n");
		sb.append("  from point \n");
		sb.append(" where school_id = #{schoolId} \n");
		
		return sb.toString();
	}

	public String getList(Map<String, Object> paramMap) {
		return super.page(getListSql(paramMap), "id");
	}

	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(getListSql(paramMap));
	}
}
