package cn.com.weixunyun.child.module.homework;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class HomewokCompleteMapperProvider extends AbstractMapperProvider {
	public String selectStudentSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select b.* from student b, classes a "
				+ "where a.id = b.classes_id and a.teacher_id = #{teacherId}");
		return sb.toString();
	}

	public String selectHomeworkSql(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select b.id as studentId,"
				+ " b.name as studentName,"
				+ " c.description as desHomework,"
				+ " c.id as homeworkId,"
				+ " c.course  as course,"
				+ " d.begin_time as beginTime,"
				+ " d.end_time as endTime,"
				+ " d.complete as complate from classes a, student b "
				+ " left join homework c on b.classes_id = c.class_id"
				+ " left join homework_complete d on c.id = d.homework_id"
				+ " where a.id = b.classes_id and a.teacher_id = #{teacherId} and b.id = #{studentId}");
		return sb.toString();
	}

	public String select(Map<String, Object> paramMap) {
		Long studentId = (Long) paramMap.get("studentId");
		if (studentId != null) {
			return super.page(selectHomeworkSql(paramMap));
		} else {
			return super.page(selectStudentSql(paramMap));
		}
	}

	public String selectCount(Map<String, Object> paramMap) {
		Long studentId = (Long) paramMap.get("studentId");
		if (studentId != null) {
			return super.pageCount(selectHomeworkSql(paramMap));
		} else {
			return super.pageCount(selectStudentSql(paramMap));
		}
	}

}
