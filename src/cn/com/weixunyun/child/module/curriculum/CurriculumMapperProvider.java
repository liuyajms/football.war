package cn.com.weixunyun.child.module.curriculum;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class CurriculumMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.id, a.create_time as createTime, a.type, b.name className, " +
				"	case a.type when '0' then '正常课表' when '1' then '单周课表' else '双周课表' end as typeName " +
				"  from curriculum a, classes b " +
				"where a.classes_id = b.id and a.school_id = #{schoolId} and a.term = #{term}");
		String type = (String) paramMap.get("type");
		if (type != null) {
			sb.append(" and a.type=#{type}");
		}
		Long classesId = (Long) paramMap.get("classesId");
		if (classesId != null && classesId != 0) {
			sb.append(" and a.classes_id=#{classesId}");
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
