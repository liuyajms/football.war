package cn.com.weixunyun.child.module.homework;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class HomeworkMapperProvider extends AbstractMapperProvider {

	
	public String getList(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap, "teacher"),"id");
	}
	
	public String getOtherList(Map<String, Object> paramMap) {
		return super.page(selectSql(paramMap, null),"id");
	}
	
	public String getCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap, "teacher"));
	}
	
	public String getOtherCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap, null));
	}
	
	private String selectSql(Map<String, Object> paramMap, String type) {
		StringBuilder sql = new StringBuilder(1024);
		sql.append("select t.*, c.name as course_name from homework t " +
				"left join course c on t.course_id = c.id " +
				"where t.school_id=#{schoolId} and t.classes_id=#{classesId} and t.term=#{term} and t.course_id=#{courseId} \n");
		
		if( "teacher".equals(type)){
			sql.append(" and t.create_teacher_id=#{teacherId}");
		}
		
		String keyword = (String) paramMap.get("keyword");
		String queryDate = (String) paramMap.get("queryDate");
		if (keyword != null && !"".equals(keyword)) {
			sql.append(" and t.description like '%'||#{keyword}||'%' ");
		}
		
		if ( "today".equals(queryDate)){//查询当天数据
			sql.append(" and to_char(t.create_time,'yyyy-MM-dd') = to_char(now(),'yyyy-MM-dd') ");
		} else if ( "lastday".equals(queryDate) ){//查询昨日数据
			sql.append(" and to_char(t.create_time ,'yyyy-MM-dd') = to_char(now() -interval '1 day' ,'yyyy-MM-dd')");
		} 
		return sql.toString();
		
	}
	
	public String getStudentHomeworkList(Map<String, Object> paramMap) {
		return super.page(getStudentHomeworkListSql(paramMap), "id");
	}
	
	public String getStudentHomeworkListCount(Map<String, Object> paramMap) {
		return super.pageCount(getStudentHomeworkListSql(paramMap));
	}
	
	private String getStudentHomeworkListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		
		Object studentIdObject = paramMap.get("studentId");
		if (studentIdObject != null && !"".equals(studentIdObject)) {
			sb.append("select c.id as courseId, c.name as courseName, h.id, h.classes_id, h.description, h.create_teacher_id, h.create_time, h.update_teacher_id, h.update_time, hc.checked \n");
		} else {
			sb.append("select c.id as courseId, c.name as courseName, h.id, h.classes_id, h.description, h.create_teacher_id, h.create_time, h.update_teacher_id, h.update_time, null as checked \n");
		}
		
		sb.append("  from course c \n");
		sb.append("  left join homework h \n");
		sb.append("    on c.id = h.course_id \n");
		
		if (studentIdObject != null && !"".equals(studentIdObject)) {
			sb.append("  left join homework_check hc \n");
			sb.append("    on h.id = hc.homework_id \n");
			sb.append("   and hc.student_id = #{studentId} \n");
			sb.append("   and to_char(hc.create_time,'yyyyMMdd') = #{date} \n");
		}
		
		sb.append(" where h.school_id = #{schoolId} \n");
		sb.append("   and h.term = #{term} \n");
		sb.append("   and h.classes_id = #{classesId} \n");

        if(paramMap.get("date") != null && paramMap.get("date").equals("")){
            sb.append("   and to_char(h.create_time,'yyyyMMdd') = #{date} \n");
        }

		return sb.toString();
	}
	
	public String getHomeworkList(Map<String, Object> paramMap) {
		return super.page(getHomeworkListSql(paramMap), "cls.name");
	}
	
	private String getHomeworkListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("select cls.name as classesName, c.name as courseName, h.description \n");
		sb.append("  from classes_teacher ct \n");
		sb.append("  join classes cls \n");
		sb.append("    on cls.id = ct.classes_id \n");
		sb.append("  join course c \n");
		sb.append("    on c.id = ct.course_id \n");
		sb.append("  left join homework h \n");
		sb.append("    on ct.teacher_id = h.create_teacher_id \n");
		sb.append("   and h.course_id = c.id \n");
		sb.append("   and h.classes_id = cls.id \n");
		sb.append("   and to_char(h.create_time ,'yyyyMMdd') = #{date} \n");
		sb.append("   and h.term = #{term} \n");
		sb.append(" where ct.teacher_id = #{teacherId} \n");
		sb.append("   and ct.school_id = #{schoolId} \n");
		
		return sb.toString();
	}
	
}
