package cn.com.weixunyun.child.module.notice;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class NoticeMapperProvider extends AbstractMapperProvider {

	public String getListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select n.*, t.name as createUserName \n");
		sb.append("  from notice n \n");
		sb.append("  join teacher t \n");
		sb.append("    on t.id = n.create_user_id \n");
		sb.append(" where n.school_id = #{schoolId} \n");
		boolean teacher = (Boolean) paramMap.get("teacher");
		boolean parents = (Boolean) paramMap.get("parents");
		if (!(teacher && parents)) {
			if (teacher || parents) {
				if (teacher) {
					sb.append("   and n.push_teacher \n");
				}
				if (parents) {
					sb.append("   and n.push_parents \n");
				}
			} else {
				sb.append("   and n.push_teacher and n.push_parents \n");
			}
		}
		if (paramMap.get("keyword") != null) {
			sb.append("   and n.description like '%' || #{keyword} || '%' \n");
		}
		return sb.toString();
	}

	public String getList(Map<String, Object> paramMap) {
		return super.page(getListSql(paramMap), "n.id");
	}

	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(getListSql(paramMap));
	}
}
