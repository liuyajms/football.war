package cn.com.weixunyun.child.module.stats;

import java.util.HashMap;
import java.util.Map;

public class StatsMapperProvider {


    public String listVisitParents(Map<String, Object> paramMap) {
        String sqlWhere = getVisitSqlWhere(paramMap);
        StringBuffer sb = new StringBuffer();

        sb.append(" select time::date date , count(*) parents from log t \n");
        sb.append(" where url ='/auth/parents' and method='POST' \n");
        sb.append(sqlWhere);
        sb.append(" group by time::date order by date \n");

        if (paramMap.get("rows") != null && !paramMap.get("rows").equals(0)) {
            sb.append(" limit #{rows} offset #{offset}");
        }
        return sb.toString();
    }

    public String listVisitTeachers(Map<String, Object> paramMap) {
        String sqlWhere = getVisitSqlWhere(paramMap);
        StringBuffer sb = new StringBuffer();

        sb.append(" select time::date date , count(*) teachers from log t \n");
        sb.append(" where url ='/auth/teacher' and method='POST' \n");
        sb.append(sqlWhere);
        sb.append(" group by time::date order by date \n");

        if (paramMap.get("rows") != null && !paramMap.get("rows").equals(0)) {
            sb.append(" limit #{rows} offset #{offset}");
        }
        return sb.toString();
    }

    public String listSchoolNotice(Map<String, Object> paramMap) {
        String sqlWhere = getSchoolSqlWhere(paramMap);
        StringBuffer sb = new StringBuffer();

        sb.append(" select count(*) notice , create_time::date date from notice where classes_id is null  \n");
        sb.append(sqlWhere);
        sb.append(" group by create_time order by date \n");

        if (paramMap.get("rows") != null && !paramMap.get("rows").equals(0)) {
            sb.append(" limit #{rows} offset #{offset}");
        }
        return sb.toString();
    }


    public String listSchoolWeibo(Map<String, Object> paramMap) {
        String sqlWhere = getSchoolSqlWhere(paramMap);
        StringBuffer sb = new StringBuffer();

        sb.append("select count(*) weibo , create_time::date date from weibo  where classes_id is null \n");
        sb.append(sqlWhere);
        sb.append(" group by create_time::date order by date \n");

        if (paramMap.get("rows") != null && !paramMap.get("rows").equals(0)) {
            sb.append(" limit #{rows} offset #{offset}");
        }
        return sb.toString();
    }

    public String listSchoolNews(Map<String, Object> paramMap) {
        String sqlWhere = getSchoolSqlWhere(paramMap);

        StringBuffer sb = new StringBuffer();

        sb.append("select count(*) news , create_time::date date from news where type=1 ");
        sb.append(sqlWhere);
        sb.append("group by create_time::date order by date \n");

        if (paramMap.get("rows") != null && !paramMap.get("rows").equals(0)) {
            sb.append(" limit #{rows} offset #{offset}");
        }
        return sb.toString();
    }

    private String getSchoolSqlWhere(Map<String, Object> paramMap) {
        String schoolWhere = paramMap.get("schoolId") == null ? "" : "and school_id=" + paramMap.get("schoolId").toString() + " ";
        String beginDate = paramMap.get("beginDate") == null ? null : paramMap.get("beginDate").toString();
        String endDate = paramMap.get("beginDate") == null ? null : paramMap.get("endDate").toString();
        String dateWhere = "";
        if (beginDate != null && endDate != null) {
            dateWhere = " and (create_time >= #{beginDate}::date and create_time < #{endDate}::date ) \n";
        }
        return schoolWhere + dateWhere;
    }

    private String getVisitSqlWhere(Map<String, Object> paramMap) {
        String schoolWhere = paramMap.get("schoolId") == null ? "" : "and school_id=" + paramMap.get("schoolId").toString() + " ";
        String beginDate = paramMap.get("beginDate") == null ? null : paramMap.get("beginDate").toString();
        String endDate = paramMap.get("beginDate") == null ? null : paramMap.get("endDate").toString();
        String dateWhere = "";
        if (beginDate != null && endDate != null) {
            dateWhere = " and (t.time >= #{beginDate}::date and t.time < #{endDate}::date ) \n";
        }
        return schoolWhere + dateWhere;
    }


    @Deprecated
    public String listLogin(Map<String, Object> paramMap) {
        String schoolWhere = paramMap.get("schoolId") == null ? "" : "and t.school_id=" + paramMap.get("schoolId").toString();
        String beginDate = paramMap.get("beginDate") == null ? null : paramMap.get("beginDate").toString();
        String endDate = paramMap.get("beginDate") == null ? null : paramMap.get("endDate").toString();
        String dateWhere = "";
        if (beginDate != null && endDate != null) {
            dateWhere = " and (t.time >= #{beginDate}::timestamp and t.time < #{endDate}::timestamp ) \n";
        }

        StringBuffer sb = new StringBuffer();

        sb.append("select " +
                " (select count(*) from teacher t where 1=1 " + schoolWhere + ") teachers, " +
                " (select count(*) from student_parents t where 1=1 " + schoolWhere + ") parents, \n");

//  由于log表中登陆接口记录中的school_id为空,故采用weibo接口统计登录数
        sb.append("(select count(distinct(user_id)) from log t where ( (url = '/auth/teacher' and method='POST') \n");
        sb.append(" or (url='/weibo' and method='GET') ) \n");
        sb.append(schoolWhere + dateWhere);
        sb.append(" )teachers_login,");

        sb.append("(select count(distinct(user_id)) from log t where ( (url = '/auth/parents' and method='POST') \n");
        sb.append(" or (url='/weibo' and method='GET') ) \n");
        sb.append(schoolWhere + dateWhere);
        sb.append(" )parents_login");

        return sb.toString();
    }

    public String getTotalTeachers(Map<String, Object> paramMap) {
        String schoolWhere = paramMap.get("schoolId") == null ? "" : "and t.school_id=" + paramMap.get("schoolId").toString();
        StringBuffer sb = new StringBuffer();

        sb.append(" select count(*) teachers from teacher t where 1=1 "  );
        sb.append(schoolWhere);

        return sb.toString();
    }

    public String getTotalParents(Map<String, Object> paramMap) {
        String schoolWhere = paramMap.get("schoolId") == null ? "" : "and t.school_id=" + paramMap.get("schoolId").toString();
        StringBuffer sb = new StringBuffer();

        sb.append(" select count(*) parents from v_user t where t.type=1 "  );
        sb.append(schoolWhere);

        return sb.toString();
    }

    public String getLoginTeachers(Map<String, Object> paramMap) {
        String schoolWhere = paramMap.get("schoolId") == null ? "" : "and t.school_id=" + paramMap.get("schoolId").toString();
        String beginDate = paramMap.get("beginDate") == null ? null : paramMap.get("beginDate").toString();
        String endDate = paramMap.get("beginDate") == null ? null : paramMap.get("endDate").toString();
        String dateWhere = "";
        if (beginDate != null && endDate != null) {
            dateWhere = " and (t.time >= #{beginDate}::timestamp and t.time < #{endDate}::timestamp ) \n";
        }

        StringBuffer sb = new StringBuffer();
//  由于log表中登陆接口记录中的school_id为空,故采用weibo接口统计登录数
        sb.append("SELECT\n" +
                "	COUNT (*) teachers_login \n" +
                "FROM\n" +
                "	(\n" +
                "		SELECT\n" +
                "			1\n" +
                "		FROM\n" +
                "			log T,\n" +
                "			teacher t2\n" +
                "		WHERE\n" +
                "			(\n" +
                "				url = '/weibo'\n" +
                "				AND METHOD = 'GET'\n" +
                "			)\n" +
                "		AND T .user_id = t2. ID\n" +
                schoolWhere + dateWhere +
                "		GROUP BY\n" +
                "			T .user_id\n" +
                "	) v");

        return sb.toString();
    }

    public String getLoginParents(Map<String, Object> paramMap) {
        String schoolWhere = paramMap.get("schoolId") == null ? "" : "and t.school_id=" + paramMap.get("schoolId").toString();
        String beginDate = paramMap.get("beginDate") == null ? null : paramMap.get("beginDate").toString();
        String endDate = paramMap.get("beginDate") == null ? null : paramMap.get("endDate").toString();
        String dateWhere = "";
        if (beginDate != null && endDate != null) {
            dateWhere = " and (t.time >= #{beginDate}::timestamp and t.time < #{endDate}::timestamp ) \n";
        }

        StringBuffer sb = new StringBuffer();
//  由于log表中登陆接口记录中的school_id为空,故采用weibo接口统计登录数
        sb.append("SELECT\n" +
                "	COUNT (*) parents_login \n" +
                "FROM\n" +
                "	(\n" +
                "		SELECT\n" +
                "			1\n" +
                "		FROM\n" +
                "			log T,\n" +
                "			parents t2\n" +
                "		WHERE\n" +
                "			(\n" +
                "				url = '/weibo'\n" +
                "				AND METHOD = 'GET'\n" +
                "			)\n" +
                "		AND T .user_id = t2. ID\n" +
                schoolWhere + dateWhere +
                "		GROUP BY\n" +
                "			T .user_id\n" +
                "	) v");

        return sb.toString();
    }

    public static void main(String[] args) {
        StatsMapperProvider s = new StatsMapperProvider();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("schoolId", 1176);
        params.put("beginDate", "2014-03-11");
        params.put("endDate", "2015-01-11");
        params.put("classId", 232);
//		System.out.println(s.listClass(params));
//		System.out.println(s.listSchool(params));
//		System.out.println(s.listVisit(params));
        System.out.println(s.listLogin(params));
    }


    //---------------- 获取班级统计 SQL ----------------
    public String listClassBroadcast(Map<String, Object> paramMap) {
        String sqlWhere = getClassSqlWhere(paramMap);

        StringBuffer sb = new StringBuffer();

/*        sb.append("select count(*) notice , create_time::date date from notice t  where 1=1 " +
                sqlWhere +
                " group by create_time::date order by date \n");*/
        sb.append("select count(*) notice , create_time::date date from broadcast t  where 1=1 " +
                sqlWhere +
                " group by create_time::date order by date \n");

        if (paramMap.get("rows") != null && !paramMap.get("rows").equals(0)) {
            sb.append(" limit #{rows} offset #{offset}");
        }

        return sb.toString();
    }

    public String listClassWeibo(Map<String, Object> paramMap) {
        String sqlWhere = getClassSqlWhere(paramMap);
        StringBuffer sb = new StringBuffer();

        sb.append("select count(*) classWeibo , create_time::date date from weibo t  where 1=1 " +
                sqlWhere +
                " group by create_time::date order by date \n");

        if (paramMap.get("rows") != null && !paramMap.get("rows").equals(0)) {
            sb.append(" limit #{rows} offset #{offset}");
        }

        return sb.toString();
    }

    public String listClassStars(Map<String, Object> paramMap) {
        String sqlWhere = getClassSqlWhere(paramMap);
        StringBuffer sb = new StringBuffer();

        sb.append("select count(*) stars , create_time::date date from star  t where 1=1 " +
                sqlWhere +
                " group by create_time::date order by date \n");

        if (paramMap.get("rows") != null && !paramMap.get("rows").equals(0)) {
            sb.append(" limit #{rows} offset #{offset}");
        }

        return sb.toString();
    }

    public String listClassUploads(Map<String, Object> paramMap) {
        String sqlWhere = getClassSqlWhere(paramMap);
        StringBuffer sb = new StringBuffer();

        sb.append("select count(*) uploads , create_time::date date from download t where 1=1 " +
                sqlWhere +
                " group by create_time::date order by date \n");

        if (paramMap.get("rows") != null && !paramMap.get("rows").equals(0)) {
            sb.append(" limit #{rows} offset #{offset}");
        }

        return sb.toString();
    }

    public String listClassParentsWeibo(Map<String, Object> paramMap) {
        String sqlWhere = getClassSqlWhere(paramMap);
        StringBuffer sb = new StringBuffer();

        sb.append("select count(*) parentWeibo , t.create_time::date date from weibo t  "
                + "inner join v_user v on v.id=t.create_user_id  where 1=1 " +
                sqlWhere +
                " and v.type=1 group by t.create_time::date order by date \n");

        if (paramMap.get("rows") != null && !paramMap.get("rows").equals(0)) {
            sb.append(" limit #{rows} offset #{offset}");
        }

        return sb.toString();
    }

    private String getClassSqlWhere(Map<String, Object> paramMap) {
        String schoolWhere = paramMap.get("schoolId") == null ? "" : " and t.school_id=" + paramMap.get("schoolId").toString();
        String classWhere = paramMap.get("classId") == null ? " and t.classes_id is not null " : " and t.classes_id=" + paramMap.get("classId").toString();
        String beginDate = paramMap.get("beginDate") == null ? null : paramMap.get("beginDate").toString();
        String endDate = paramMap.get("beginDate") == null ? null : paramMap.get("endDate").toString();
        String dateWhere = "";
        if (beginDate != null && endDate != null) {
            dateWhere = " and (t.create_time >= #{beginDate}::date and t.create_time < #{endDate}::date ) \n";
        }
        return schoolWhere + classWhere + dateWhere;
    }

}
