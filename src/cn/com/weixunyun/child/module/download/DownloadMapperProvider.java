package cn.com.weixunyun.child.module.download;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class DownloadMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.id, a.name, a.size, a.create_time as createTime, a.description, \n");
		sb.append("	b.name classesName, c.name teacherName, a.school_id as schoolId, \n");
		sb.append("	a.content_type as contentType, a.top_days, a.classes_id,a.name_file as nameFile \n");
		sb.append("  from download a \n");
		sb.append("  left join classes b \n");
		sb.append("    on a.classes_id = b.id \n");
		sb.append("  join teacher c \n");
		sb.append("    on a.create_teacher_id = c.id \n");
		sb.append(" where a.school_id = #{schoolId} \n");

		if (paramMap.get("flag") == null) {
			sb.append("   and (a.classes_id = #{classesId} or a.classes_id is null) ");
		} else if (paramMap.get("classesId") != null && !"".equals(paramMap.get("classesId").toString())) {
			sb.append("   and a.classes_id = #{classesId}");
		} else {
			sb.append("   and a.classes_id is null");
		}

		return sb.toString();
	}

	public String select(Map<String, Object> paramMap) {
//		return super.page(selectSql(paramMap), "a.id");
		return super.page(selectSql(paramMap), "a.classes_id desc, a.create_time::timestamp  + (top_days ||' day')::interval  ");
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}
}
