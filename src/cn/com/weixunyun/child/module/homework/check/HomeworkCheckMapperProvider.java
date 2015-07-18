package cn.com.weixunyun.child.module.homework.check;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class HomeworkCheckMapperProvider extends AbstractMapperProvider {

	public String getListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * \n");
		sb.append("  from homework_check \n");
		sb.append(" where school_id = #{schoolId} \n");
		sb.append("   and term = #{term} \n");
		
		return sb.toString();
	}

	public String getList(Map<String, Object> paramMap) {
		return super.page(getListSql(paramMap), "id");
	}

	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(getListSql(paramMap));
	}
	
	public String getStudentCheckList(Map<String, Object> paramMap) {
		return super.page(getStudentCheckListSql(paramMap), "convert_to(s.name,'gbk')", true);
	}

	public String getStudentCheckListCount(Map<String, Object> paramMap) {
		return super.pageCount(getStudentCheckListSql(paramMap));
	}
	
	public String getStudentCheckListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select s.name as studentName , hc.checked \n");
		sb.append("  from student s \n");
		sb.append("  left join homework_check hc \n");
		sb.append("    on s.id = hc.student_id \n");
		sb.append("   and s.classes_id = #{classesId} \n");
		sb.append("   and hc.homework_id = #{homeworkId} \n");
		sb.append("  left join homework h \n");
		sb.append("    on h.id = hc.homework_id \n");
		sb.append("   and h.id = #{homeworkId} \n");
		sb.append("   and hc.classes_id = #{classesId} \n");
		sb.append("   and to_char(hc.create_time, 'yyyyMMdd') = #{date} \n");
		sb.append(" where s.classes_id = #{classesId} \n");
		
		return sb.toString();
	}
	
	public String getClassesCheckList(Map<String, Object> paramMap) {
		return super.page(getClassesCheckListSql(paramMap), "convert_to(s.name, 'gbk')", true);
	}
	
	public String getClassesCheckListCount(Map<String, Object> paramMap) {
		return super.pageCount(getClassesCheckListSql(paramMap));
	}
	
	public String getClassesCheckListSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.name as courseName, cls.name as classesName, s.name as studentName , hc.checked , hc.description, hc.create_time \n");
		sb.append("  from student s \n");
		sb.append("  join classes cls \n");
		sb.append("    on cls.id = s.classes_id \n");
		sb.append("  left join homework_check hc \n");
		sb.append("    on s.id = hc.student_id \n");
		sb.append("   and hc.term = #{term} \n");
		sb.append("   and hc.school_id = #{schoolId} \n");
		
		Object date1Object = paramMap.get("dateBegin");
		Object date2Object = paramMap.get("dateEnd");
		if (date1Object != null && !"".equals(date1Object)) {
			sb.append("   and to_char(hc.create_time, 'yyyyMMdd') >= #{dateBegin} \n");
		}
		if (date2Object != null && !"".equals(date2Object)) {
			sb.append("   and to_char(hc.create_time, 'yyyyMMdd') <= #{dateEnd} \n");
		}
		
		sb.append("  left join course c \n");
		sb.append("    on c.id = hc.course_id \n");
		sb.append(" where s.classes_id = #{classesId} \n");
		return sb.toString();
	}
	
}
