package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class StudentMapperProvider extends AbstractMapperProvider {

	public String getListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select s.*, c.name as classesName, v.name as genderName \n");
		sb.append("  from student s \n");
		sb.append("  join classes c \n");
		sb.append("    on c.id = s.classes_id \n");
		sb.append("  left join dictionary_value v \n");
		sb.append("    on v.code = s.gender \n");
		sb.append("   and v.school_id = #{schoolId} \n");
		sb.append("   and v.dictionary_field_code = 'gender' \n");
		sb.append("   and v.dictionary_table_code = 'student' \n");
		sb.append(" where c.school_id = #{schoolId} \n");

		if (paramMap.containsKey("classesId")) {
			sb.append("   and s.classes_id = #{classesId} \n");
		}

		String keyword = (String) paramMap.get("keyword");
		if (keyword != null && !"".equals(keyword)) {
			sb.append("   and (s.name like '%'||#{keyword}||'%' or s.code like '%'||#{keyword}||'%' or s.address like '%'||#{keyword}||'%' or s.description like '%'||#{keyword}||'%') ");
		}
		return sb.toString();
	}

	public String getList(Map<String, Object> paramMap) {
		return super.page(getListSql(paramMap), "convert_to(s.name, 'gbk')", true);
	}

	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(getListSql(paramMap));
	}
	
	public String getExportList(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select s.*, c.name as classesName, v.name as genderName , sp.username as parentsUsername, \n");
		sql.append("	p.name as parentsName, p.mobile as parentsMobile, dv.name as parentsType, p.pta as parentsPta \n");
		sql.append("  from student s  \n");
		sql.append("  left join student_parents sp \n");
		sql.append("    on s.id = sp.student_id \n");
		sql.append("  left join parents p \n");
		sql.append("    on p.id = sp.parents_id \n");
		sql.append("  join classes c  \n");
		sql.append("    on c.id = s.classes_id  \n");
		sql.append("  left join dictionary_value v  \n");
		sql.append("    on v.code = s.gender  \n");
		sql.append("   and v.school_id = #{schoolId} \n");
		sql.append("   and v.dictionary_field_code = 'gender'  \n");
		sql.append("   and v.dictionary_table_code = 'student'  \n");
		sql.append("  left join dictionary_value dv  \n");
		sql.append("    on dv.code = sp.type  \n");
		sql.append("   and dv.school_id = #{schoolId} \n");
		sql.append("   and dv.dictionary_field_code = 'type'  \n");
		sql.append("   and dv.dictionary_table_code = 'parents'  \n");
		sql.append(" where c.school_id = #{schoolId} \n");

		if (paramMap.containsKey("classesId")) {
			sql.append("   and s.classes_id = #{classesId} \n");
		}

		String keyword = (String) paramMap.get("keyword");
		if (keyword != null && !"".equals(keyword)) {
			sql.append("   and (s.name like '%'||#{keyword}||'%' or s.code like '%'||#{keyword}||'%' or s.address like '%'||#{keyword}||'%' or s.description like '%'||#{keyword}||'%') \n");
		}
		
		sql.append(" order by convert_to(s.name,'gbk') ");
		return sql.toString();
	}
}
