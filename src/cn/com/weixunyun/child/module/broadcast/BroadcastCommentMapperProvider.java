package cn.com.weixunyun.child.module.broadcast;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class BroadcastCommentMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select bc.*, u.name as create_user_name, u.type as create_user_type, u.update_time as create_user_update_time \n");
		sql.append("  from broadcast_comment bc \n");
		sql.append("  join broadcast b \n");
		sql.append("    on b.id = bc.broadcast_id \n");
		sql.append("  left join v_user_classes u \n");
		sql.append("    on u.id = bc.create_user_id \n");
		sql.append("   and (u.classes_id is null or b.classes_id is null or u.classes_id = b.classes_id) \n");
		sql.append(" where bc.broadcast_id = #{broadcastId} \n");
		String keyword = (String) paramMap.get("keyword");
		if (keyword != null) {
			sql.append(" and bc.description like '%'||#{keyword}||'%' \n");
		}
		return sql.toString();
	}

	public String select(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap), "bc.id", true);
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}
}
