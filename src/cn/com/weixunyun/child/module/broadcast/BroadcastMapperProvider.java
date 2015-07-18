package cn.com.weixunyun.child.module.broadcast;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class BroadcastMapperProvider extends AbstractMapperProvider {

	public String getSchoolBroadcastListSql(Map<String, Object> paramMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("select b.*, \n");
		sb.append("       t.name as createTeacherName, \n");
		sb.append("       (select count(id) from broadcast_comment where broadcast_id = b.id) as commentCount \n");
		sb.append("  from broadcast b \n");
		sb.append("  left join teacher t \n");
		sb.append("    on t.id = b.create_teacher_id \n");
		sb.append(" where b.school_id = #{schoolId} \n");
		sb.append("   and b.classes_id is null \n");
		sb.append("   and b.term = #{term} \n");
		String keyword = (String) paramMap.get("keyword");
		if (keyword != null) {
			sb.append("   and (b.title like '%'||#{keyword}||'%' or b.description like '%'||#{keyword}||'%') \n");
		}
		return sb.toString();
	}

	public String getSchoolBroadcastList(Map<String, Object> paramMap) {
		return super.page(getSchoolBroadcastListSql(paramMap), "b.id");
	}

	public String getSchoolBroadcastListCount(Map<String, Object> paramMap) {
		return super.pageCount(getSchoolBroadcastListSql(paramMap));
	}

	public String getGradeBroadcastListSql(Map<String, Object> paramMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("select b.*, \n");
		sb.append("       t.name as createTeacherName, \n");
		sb.append("       (select count(id) from broadcast_comment where broadcast_id = b.id) as commentCount \n");
		sb.append("  from broadcast b \n");
		sb.append("  left join teacher t \n");
		sb.append("    on t.id = b.create_teacher_id \n");
		sb.append(" where b.school_id = #{schoolId} \n");
		sb.append("   and b.classes_id is null \n");
		sb.append("   and b.term = #{term} \n");
		String keyword = (String) paramMap.get("keyword");
		if (keyword != null) {
			sb.append("   and (b.title like '%'||#{keyword}||'%' or b.description like '%'||#{keyword}||'%') \n");
		}
		return sb.toString();
	}

	public String getGradeBroadcastList(Map<String, Object> paramMap) {
		return super.page(getGradeBroadcastListSql(paramMap), "b.id");
	}

	public String getGradeBroadcastListCount(Map<String, Object> paramMap) {
		return super.pageCount(getGradeBroadcastListSql(paramMap));
	}

	public String getClassesBroadcastListSql(Map<String, Object> paramMap) {
		Long classesId = (Long) paramMap.get("classesId");

		StringBuilder sb = new StringBuilder();
		sb.append("select b.*, \n");
		sb.append("       t.name as createTeacherName, \n");
		sb.append("       c.name as classesName, \n");
		sb.append("       (select count(id) from broadcast_comment where broadcast_id = b.id) as commentCount \n");
		sb.append("  from broadcast b \n");
		sb.append("  left join teacher t \n");
		sb.append("    on t.id = b.create_teacher_id \n");
		sb.append("  left join classes c \n");
		sb.append("    on c.id = b.classes_id \n");
		sb.append(" where b.school_id = #{schoolId} \n");
		sb.append("   and b.grade = 0 \n");
		sb.append("   and b.term = #{term} \n");
		if (classesId == null || classesId == 0) {
			sb.append("   and b.classes_id is not null \n");
		} else {
			sb.append("   and b.classes_id = #{classesId} \n");
		}
		return sb.toString();
	}

	public String getClassesBroadcastList(Map<String, Object> paramMap) {
		return super.page(getClassesBroadcastListSql(paramMap), "b.id");
	}

	public String getClassesBroadcastListCount(Map<String, Object> paramMap) {
		return super.pageCount(getClassesBroadcastListSql(paramMap));
	}

	public String getListSql(Map<String, Object> paramMap) {
		Integer grade = (Integer) paramMap.get("grade");

		StringBuilder sb = new StringBuilder(512);
		sb.append("select b.*, \n");
		sb.append("       t.name as createTeacherName, \n");
		sb.append("       c.name as classesName, \n");
		sb.append("       (select count(id) from broadcast_comment where broadcast_id = b.id) as commentCount \n");
		sb.append("  from (select 0 as type,* \n");
		sb.append("          from broadcast \n");
		sb.append("         where school_id = #{schoolId} \n");
		sb.append("           and grade = 0 \n");
		sb.append("           and classes_id is null \n");
		sb.append("           and term = #{term} \n");
		sb.append("        union all \n");
		sb.append("        select 1,* \n");
		sb.append("          from broadcast \n");
		sb.append("         where school_id = #{schoolId} \n");
		sb.append("           and (grade & (1 << " + grade + ")) > 0 \n");
		sb.append("           and classes_id is null \n");
		sb.append("           and term = #{term} \n");
		sb.append("        union all \n");
		sb.append("        select 2,* \n");
		sb.append("          from broadcast \n");
		sb.append("         where school_id = #{schoolId} \n");
		sb.append("           and classes_id = #{classesId}) b \n");
		sb.append("          left join teacher t \n");
		sb.append("            on t.id = b.create_teacher_id \n");
		sb.append("          left join classes c \n");
		sb.append("            on c.id = b.classes_id \n");
		sb.append("           and term = #{term} \n");
		System.out.println(sb.toString());
		return sb.toString();
	}

	public String getList(Map<String, Object> paramMap) {
		return super.page(getListSql(paramMap), "b.id");
	}

	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(getListSql(paramMap));
	}

	public String get(Long id) {
		StringBuilder sql = new StringBuilder(512);
		sql.append("select b.*, \n");
		sql.append("       t.name as create_teacher_name, \n");
		sql.append("       c.name as classes_name, \n");
		sql.append("       case \n");
		sql.append("         when b.grade = 0 and classes_id is null then \n");
		sql.append("          0 \n");
		sql.append("         when b.grade > 0 and classes_id is null then \n");
		sql.append("          1 \n");
		sql.append("         when classes_id is not null then \n");
		sql.append("          2 \n");
		sql.append("       end as type, \n");
		sql.append("       (select count(id) from broadcast_comment where broadcast_id = b.id) as commentCount \n");
		sql.append("  from broadcast b \n");
		sql.append("  left join teacher t \n");
		sql.append("    on t.id = b.create_teacher_id \n");
		sql.append("  left join classes c \n");
		sql.append("    on c.id = b.classes_id \n");
		sql.append(" where b.id = #{id} \n");
		sql.append(" limit 1 \n");

		return sql.toString();
	}

	public String getClassesBroadcastSql(Map<String, Object> paramMap) {

		StringBuilder sb = new StringBuilder();
		sb.append("select b.*, \n");
		sb.append("       t.name as createTeacherName, \n");
		sb.append("       c.name as classesName, \n");
		sb.append("       (select count(id) from broadcast_comment where broadcast_id = b.id) as commentCount \n");
		sb.append("  from broadcast b \n");
		sb.append("  left join teacher t \n");
		sb.append("    on t.id = b.create_teacher_id \n");
		sb.append("  left join classes c \n");
		sb.append("    on c.id = b.classes_id \n");
		sb.append(" where b.school_id = #{schoolId} \n");
		sb.append("     and b.term = #{term} \n");
		Long keyword = (Long) paramMap.get("keyword");

		if (keyword == null || keyword == 0) {
			sb.append("   and b.classes_id is not null \n");
		} else {
			sb.append("   and b.classes_id = #{keyword} \n");
		}

		return sb.toString();
	}

	public String getClassesBroadcast(Map<String, Object> paramMap) {
		return super.page(getClassesBroadcastSql(paramMap), "b.id");
	}

	public String getClassesBroadcastCount(Map<String, Object> paramMap) {
		return super.pageCount(getClassesBroadcastSql(paramMap));
	}
}
