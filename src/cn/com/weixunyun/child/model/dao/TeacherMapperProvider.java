package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class TeacherMapperProvider extends AbstractMapperProvider {

	public String selectSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select t.*, dv.name as titleName, v.name as genderName  \n");
		sql.append("  from teacher t  \n");
		sql.append("  left join dictionary_value dv  \n");
		sql.append("    on dv.dictionary_table_code = 'teacher'  \n");
		sql.append("   and dv.dictionary_field_code = 'title'  \n");
		sql.append("   and t.title = dv.code \n");
		sql.append("   and t.school_id = dv.school_id \n");
		sql.append("  left join dictionary_value v  \n");
		sql.append("    on v.dictionary_table_code = 'student'  \n");
		sql.append("   and v.dictionary_field_code = 'gender'  \n");
		sql.append("   and t.gender = v.code \n");
		sql.append("   and t.school_id = v.school_id \n");
		sql.append(" where t.school_id = #{schoolId} \n");
		
		String keyword = (String) paramMap.get("keyword");
		if (keyword != null) {
			sql.append(" and (t.mobile like '%'||#{keyword}||'%' or t.name like '%'||#{keyword}||'%')");
		}
		return sql.toString();
	}

	public String select(Map<String, Object> paramMap) {
		System.out.println(super.page(selectSql(paramMap), "t.name", true));
		System.out.println();
		return super.page(selectSql(paramMap), "t.name", true);
	}

	public String selectCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}

	public String selectAdminSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select t.* , dv.name as titleName \n");
		sql.append("  from teacher t \n");
		sql.append("  left join dictionary_value dv  \n");
		sql.append("    on dictionary_table_code = 'teacher'  \n");
		sql.append("   and dictionary_field_code = 'title'  \n");
		sql.append("   and t.title = dv.code \n");
		sql.append("   and t.school_id = dv.school_id \n");
		sql.append(" where t.school_id=#{schoolId}  \n");
		sql.append("   and t.type=6  \n");
		
		String keyword = (String) paramMap.get("keyword");
		if (keyword != null) {
			sql.append("   and (t.mobile like '%'||#{keyword}||'%' or t.name like '%'||#{keyword}||'%')  \n");
		}
		return sql.toString();
	}

	public String selectAdmin(Map<String, Object> paramMap) {
		return super.page(selectAdminSql(paramMap), "t.name", true);
	}

	public String selectAdminCount(Map<String, Object> paramMap) {
		return super.pageCount(selectAdminSql(paramMap));
	}
	
	public String selectNotAdmin(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select t.* , dv.name as titleName \n");
		sql.append("  from teacher t \n");
		sql.append("  left join dictionary_value dv  \n");
		sql.append("    on dictionary_table_code = 'teacher'  \n");
		sql.append("   and dictionary_field_code = 'title'  \n");
		sql.append("   and t.title = dv.code \n");
		sql.append("   and t.school_id = dv.school_id \n");
		sql.append(" where t.school_id=#{schoolId}  \n");
		sql.append("   and t.type = 0 \n");
		sql.append(" order by t.name \n");
		return sql.toString();
	}

	public String getClassesList(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.* \n");
		sb.append("  from classes c \n");
		sb.append(" where c.school_id = #{schoolId} \n");
		sb.append("   and c.teacher_id = #{teacherId} \n");
		sb.append("union \n");
		sb.append("select c.* \n");
		sb.append("  from classes_teacher ct \n");
		sb.append("  join classes c \n");
		sb.append("    on c.id = ct.classes_id \n");
		sb.append(" where c.school_id = #{schoolId} \n");
		sb.append("   and ct.teacher_id = #{teacherId} \n");
		return "select * from (" + sb.toString() + ") t order by year asc, num asc";
	}
	
	public String selectDictionaryTeacher(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.*, dv.name as titleName, v.name as genderName  \n");
		sql.append("  from teacher t  \n");
		sql.append("  left join dictionary_value dv  \n");
		sql.append("    on dv.dictionary_table_code = 'teacher'  \n");
		sql.append("   and dv.dictionary_field_code = 'title'  \n");
		sql.append("   and t.title = dv.code \n");
		sql.append("   and t.school_id = dv.school_id \n");
		sql.append("  left join dictionary_value v  \n");
		sql.append("    on v.dictionary_table_code = 'student'  \n");
		sql.append("   and v.dictionary_field_code = 'gender'  \n");
		sql.append("   and t.gender = v.code \n");
		sql.append("   and t.school_id = v.school_id \n");
		sql.append(" where t.id = #{id} \n");
		
		return sql.toString();
	}
}
