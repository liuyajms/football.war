package cn.com.weixunyun.child.model.dao;

import java.util.Map;

import cn.com.weixunyun.child.model.pojo.Message;

public class MessageMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select a.*, \n");
		sql.append("       b.name as user_name_from, \n");
		sql.append("       b.type as user_type_from, \n");
		sql.append("       d.name as user_name_to, \n");
		sql.append("       d.type as user_type_to \n");
		sql.append("  from message a \n");
		sql.append("  left join v_user_singleton b \n");
		sql.append("    on a.user_id_from = b.id \n");
		sql.append("  left join v_user_singleton d \n");
		sql.append("    on a.user_id_to = d.id \n");
		sql.append(" where (a.source = true and \n");
		sql.append("       (a.user_id_from = #{id0} ) and \n");
		sql.append("       (a.user_id_to = #{id1})) \n");
		sql.append("    or (a.source = false and \n");
		sql.append("       (a.user_id_from = #{id1}) and \n");
		sql.append("       (a.user_id_to = #{id0})) \n");
		sql.append(" order by a.id desc ");

		return sql.toString();
	}

	public String select(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap));
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}

	public String updateData(Map<String, Object> paramMap) {

		StringBuffer sb = new StringBuffer();
		Message message = (Message) paramMap.get("0");
		sb.append("update message set time_read = '" + message.getTimeRead() + "' where 1 = 1 ");// 把已读状态更新为当前时间
		Long userId = (Long) paramMap.get("userId");
		if (userId != null && userId != 0) {
			sb.append(" and (user_id_to = #{userId} and source = false)");
		}

		return sb.toString();
	}

	public String deleteData(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		Message message = (Message) paramMap.get("0");
		sb.append("delete from message where id = " + message.getId() + " ");
		Long userIdFrom = (Long) paramMap.get("userIdFrom");
		if (userIdFrom != null && userIdFrom != 0) {
			sb.append(" and (user_id_from = #{userIdFrom} or user_id_to = #{userIdFrom}) ");
		}

		return sb.toString();
	}

	public String getSessionList(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select distinct t.user_id, \n");
		sql.append("                u.name as user_name, \n");
		sql.append("                u.type as user_type, \n");
		sql.append("                u.update_time as user_update_time, \n");
		sql.append("                max(t.time_send) as time, \n");
		sql.append("                sum(case \n");
		sql.append("                      when t.time_read is null then \n");
		sql.append("                       1 \n");
		sql.append("                      else \n");
		sql.append("                       0 \n");
		sql.append("                    end) as unreaded \n");
		sql.append("  from (select user_id_to as user_id, time_send, time_read \n");
		sql.append("          from message \n");
		sql.append("         where user_id_from = #{id} \n");
		sql.append("           and source = true \n");
		sql.append("        union all \n");
		sql.append("        select user_id_from, time_send, time_read \n");
		sql.append("          from message \n");
		sql.append("         where user_id_to = #{id} \n");
		sql.append("           and source = false) t \n");
		sql.append("  join v_user_singleton u \n");
		sql.append("    on u.id = t.user_id \n");
		sql.append(" group by t.user_id, u.name, u.type, u.update_time \n");
		sql.append(" order by max(t.time_send) desc \n");

		return sql.toString();
	}

	public static void main(String[] args) {
		System.out.println(new MessageMapperProvider().selectSql(null));
	}
}
