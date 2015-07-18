package cn.com.weixunyun.child.module.course.score;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class CourseScoreMapperProvider extends AbstractMapperProvider {

	private String selectSql(Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder(1024);
		sql.append("select t.* , to_char(t.date,'yyyy-MM-dd') dateStr, s.name studentName, d.name as typeName, c.name courseName, cs.name classesName \n" +
                        " from Course_Score t  \n" +
                " left join dictionary_value d on t.type=d.code and d.dictionary_field_code='type' and dictionary_table_code='course_score' and d.school_id=t.school_id \n" +
                " left join course c on t.course_id=c.id and t.school_id=c.school_id \n");

		if( paramMap.containsKey("classesId")){
			sql.append(" left join classes cs on cs.id=#{classesId} and t.school_id=cs.school_id \n");
		}
//                " left join classes cs on cs.id=#{classesId} and t.school_id=cs.school_id \n" +
		
		sql.append(" left join student s on t.student_id=s.id and s.classes_id=#{classesId} and t.school_id=s.school_id \n"
                + " where t.school_id=#{schoolId}  and t.term=#{term} \n");
		
        if( paramMap.containsKey("courseId") && paramMap.get("courseId")!=null){
			sql.append(" and t.course_id=#{courseId} \n");
		} else if (paramMap.containsKey("studentId")){
			sql.append(" and t.student_id=#{studentId} \n");
		}
		
		if(paramMap.containsKey("keyword")){
			String keyword = (String) paramMap.get("keyword");
			if (keyword != null && !"".equals(keyword)) {
				sql.append(" and t.type like '%'||#{keyword}||'%' ");
			}
		}
		
		//用于教师登陆查看某班级学生成绩列表
		if(paramMap.containsKey("teacherId")){
			sql.append(" and t.create_teacher_id=#{teacherId}");
		}
		
		if(paramMap.containsKey("queryDate")){
			String queryDate = (String) paramMap.get("queryDate");
			if ( "today".equals(queryDate)){//查询当天数据
				sql.append(" and to_char(t.create_time,'yyyy-MM-dd') = to_char(now(),'yyyy-MM-dd') ");
			} else if ( "lastday".equals(queryDate) ){//查询昨日数据
				sql.append(" and to_char(t.create_time ,'yyyy-MM-dd') = to_char(now() -interval '1 day' ,'yyyy-MM-dd')");
			} 
		}
		
		return sql.toString();
	}

	
	public String getCourseList(Map<String, Object> paramMap){
		return selectSql(paramMap);
	}
	
    public String select(Map<String, Object> paramMap) {
        return super.page(selectSql(paramMap), "convert_to(s.name,'gbk') asc,t.create_time", false);
    }

	public String getList(Map<String, Object> paramMap) {
		return select(paramMap);
	}
	
	public String getListCount(Map<String, Object> paramMap) {
		return super.pageCount(selectSql(paramMap));
	}
}
