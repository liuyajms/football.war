package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class StudentGrowMapperProvider extends AbstractMapperProvider {

    private String selectSql(Map<String, Object> paramMap) {
        StringBuilder sql = new StringBuilder(1024);
        sql.append("select t.*, d.name as typeName, v.classesName,  v.studentName from student_grow t  " +
                " left join dictionary_value d on t.type=d.code and d.dictionary_field_code='type' and dictionary_table_code='student_grow'" +
//                " left join classes cs on cs.id=#{classesId}" +
                " left join (select s.id, s.name as studentName, c.name as classesName from student s  left join classes c on s.id=#{studentId} and s.classes_id=c.id) v on v.id=t.student_id" +
                " where t.school_id=#{schoolId} and t.student_id=#{studentId} and t.term=#{term}");

        String keyword = (String) paramMap.get("keyword");
        String queryDate = (String) paramMap.get("queryDate");

        if (keyword != null && !"".equals(keyword)) {
            sql.append(" and ( t.description like '%'||#{keyword}||'%' or t.name like '%'||#{keyword}||'%') ");
        }

        if ("today".equals(queryDate)) {//查询当天数据
            sql.append(" and to_char(t.create_time,'yyyy-MM-dd') = to_char(now(),'yyyy-MM-dd') ");
        } else if ("lastday".equals(queryDate)) {//查询昨日数据
            sql.append(" and to_char(t.create_time ,'yyyy-MM-dd') = to_char(now() -interval '1 day' ,'yyyy-MM-dd')");
        }
        System.out.println("sql:" + sql.toString());

        return sql.toString();

    }

    public String select(Map<String, Object> paramMap) {
        return super.page(selectSql(paramMap), "convert_to(t.name,'gbk')", true);
    }

    public String getList(Map<String, Object> paramMap) {
        return super.page(selectSql(paramMap),"id");
    }


    public String getListCount(Map<String, Object> paramMap) {
        return super.pageCount(selectSql(paramMap));
    }
}
