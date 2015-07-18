package cn.com.weixunyun.child.module.weibo;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class WeiboCommentMapperProvider extends AbstractMapperProvider {

	public String queryList(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select wc.*, ");
		sb.append("       u.name      as create_user_name, ");
		sb.append("       u.type      as create_user_type, ");
		sb.append("       u.update_time      as create_user_update_time, ");
		sb.append("       w.school_id ");
		sb.append("  from weibo_comment wc ");
		sb.append("  join weibo w ");
		sb.append("    on w.id = wc.weibo_id ");
		sb.append("  left join v_user_classes u ");
		sb.append("    on wc.create_user_id = u.id ");
		sb.append("   and (u.classes_id is null or u.classes_id = w.classes_id) \n");
		sb.append(" where wc.weibo_id = #{weiboId} ");
		sb.append(" order by wc.id asc ");
		return sb.toString();
	}

	public String commentListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select wc.*, ");
		sb.append("       u.name      as create_user_name, ");
		sb.append("       u.type      as create_user_type, ");
		sb.append("       u.update_time      as create_user_update_time, ");
		sb.append("       w.school_id ");
		sb.append("  from weibo_comment wc ");
		sb.append("  join weibo w ");
		sb.append("    on w.id = wc.weibo_id ");
		sb.append("  left join v_user_classes u ");
		sb.append("    on wc.create_user_id = u.id ");
		sb.append("   and (u.classes_id is null or u.classes_id = w.classes_id) \n");
		sb.append(" where wc.weibo_id = #{weiboId} ");
		String keyword = (String) paramMap.get("keyword");
		if (keyword != null) {
			sb.append(" and wc.description like '%'||#{keyword}||'%'");
		}
		sb.append(" order by wc.id asc ");
		return sb.toString();
	}

	public String commentList(Map<String, Object> paramMap) {
		return commentListSql(paramMap);
	}

	public String commentListCount(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*)");
		sb.append("  from weibo_comment wc ");
		sb.append(" where wc.weibo_id = #{weiboId} ");
		String keyword = (String) paramMap.get("keyword");
		if (keyword != null) {
			sb.append(" and wc.description like '%'||#{keyword}||'%'");
		}

		return sb.toString();
	}

}
