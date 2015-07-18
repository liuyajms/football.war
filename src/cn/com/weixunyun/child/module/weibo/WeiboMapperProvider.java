package cn.com.weixunyun.child.module.weibo;

import java.util.Map;

import cn.com.weixunyun.child.model.dao.AbstractMapperProvider;

public class WeiboMapperProvider extends AbstractMapperProvider {

    public String getSchoolWeiboList(Map<String, Object> paramMap) {
        StringBuilder sql = new StringBuilder(1024);
        sql.append("select w.*, \n");
        //微博点赞
        sql.append("  wz.weibo_id, wz.user_id, wz.time , vu.name wz_user_name," +
                " vu.type as wz_user_type, vu.update_time as wz_user_update_time, \n");
        //微博评论
        sql.append(" wc.id as wc_id, wc.weibo_id as wc_weibo_id, wc.description as wc_description, \n" +
                "wc.create_time as wc_create_time, wc.create_user_id as wc_create_user_id, wc.position_x as wc_position_x, \n" +
                "wc.position_y as wc_position_y, wc.position_name as wc_position_name, \n" +
                "wc.school_id as wc_school_id, wc.user_id_to as wc_user_id_to, \n");
        sql.append("vu_1.name as wc_create_user_name, vu_1.TYPE AS wc_create_user_type, vu_1.update_time AS wc_create_user_update_time, \n" +
                "vu_2.name as wc_user_name_to, vu_2.TYPE AS wc_user_type_to, vu_2.update_time AS wc_user_update_time_to, \n");

        sql.append("       (select count(wc.*) from weibo_comment wc where wc.weibo_id = w.id) as comment_count, \n");
        sql.append("       c.name as classes_name, \n");
        sql.append("       u.name as create_user_name, \n");
        sql.append("       u.type as create_user_type, \n");
        sql.append("       u.update_time as create_user_update_time, \n");
        sql.append("       wf.time is not null as favorit \n");
        sql.append("  from weibo w \n");
        sql.append("  left join v_user_classes u \n");
        sql.append("    on w.create_user_id = u.id \n");
        sql.append("   and (u.classes_id is null or u.classes_id = w.classes_id) \n");
        sql.append("  left join classes c \n");
        sql.append("    on w.classes_Id = c.id \n");
        sql.append("  left join weibo_favorit wf \n");
        sql.append("    on wf.weibo_id = w.id \n");
        sql.append("   and wf.user_id = #{userId} \n");
        //微博点赞
        sql.append(" left join weibo_zan wz on wz.weibo_id=w.id \n");
        sql.append(" left join v_user_classes vu on vu.id=wz.user_id \n" );
//                "and (vu.classes_id is null or vu.classes_id = w.classes_id)  \n");
        //微博评论
        sql.append("LEFT JOIN weibo_comment wc ON w. ID = wc.weibo_id\n" +
                "LEFT JOIN v_user_classes vu_1 ON wc.create_user_id = vu_1. ID\n" +
//                "AND (\n" +
//                "	vu_1.classes_id IS NULL\n" +
//                "	OR vu_1.classes_id = w.classes_id\n" +
//                ")\n" +
                "LEFT JOIN v_user_classes vu_2 ON wc.user_id_to = vu_2. ID \n" );
//                "AND (\n" +
//                "	vu_2.classes_id IS NULL\n" +
//                "	OR vu_2.classes_id = w.classes_id\n" +
//                ")\n");


        sql.append(" where w.id in ( \n");

        sql.append(" select id from weibo w \n");
        sql.append(" where w.school_id = #{schoolId} \n");
        sql.append("   and w.classes_id is null \n");
        String keyword = (String) paramMap.get("keyword");
        if (keyword != null && !keyword.equals("")) {
            sql.append("   and (w.description like '%'||#{keyword}||'%') \n");
        }
        sql.append(" order by w.id desc \n");
        if (paramMap.containsKey("rows") && paramMap.get("rows") != null) {
            sql.append(" limit #{rows} offset #{offset} ");
        }

        sql.append(" )\n ");

        sql.append(" order by w.id desc,wc.id asc, wz.time \n");
        return sql.toString();
    }

    public String getSchoolWeiboListCount(Map<String, Object> paramMap) {
        StringBuilder sql = new StringBuilder(1024);
        sql.append(" select count(*) from weibo w \n");
        sql.append(" where w.school_id = #{schoolId} \n");
        sql.append("   and w.classes_id is null \n");
        String keyword = (String) paramMap.get("keyword");
        if (keyword != null && !keyword.equals("")) {
            sql.append("   and (w.description like '%'||#{keyword}||'%') \n");
        }
        return sql.toString();
    }


    public String get(Map<String, Object> paramMap) {
        StringBuilder sql = new StringBuilder(512);
        sql.append("select w.*, \n");
        sql.append("  wz.weibo_id, wz.user_id, wz.time , vu.name user_name, \n");

        sql.append("       (select count(wc.*) from weibo_comment wc where wc.weibo_id = w.id) as comment_count, \n");
        sql.append("       c.name as classes_name, \n");
        sql.append("       u.name as create_user_name, \n");
        sql.append("       u.type as create_user_type, \n");
        sql.append("       u.update_time as create_user_update_time, \n");
        sql.append("       wf.time is not null as favorit \n");
        sql.append("  from weibo w \n");
        sql.append("  left join v_user_classes u \n");
        sql.append("    on w.create_user_id = u.id \n");
        sql.append("   and (u.classes_id is null or u.classes_id = w.classes_id) \n");
        sql.append("  left join classes c \n");
        sql.append("    on w.classes_Id = c.id \n");
        sql.append("  left join weibo_favorit wf \n");
        sql.append("    on wf.weibo_id = w.id \n");
        sql.append("   and wf.user_id = #{userId} \n");

        sql.append(" left join weibo_zan wz on wz.weibo_id=w.id");
        sql.append(" left join v_user vu on vu.id=wz.user_id");

        sql.append(" where w.id = #{id} \n");
        sql.append(" limit 1 ");
        return sql.toString();
    }

    public String getList(Map<String, Object> paramMap) {
        StringBuilder sql = new StringBuilder(1024);
        sql.append("select w.*, \n");
        //微博点赞
        sql.append("  wz.weibo_id, wz.user_id, wz.time , vu.name wz_user_name," +
                " vu.type as wz_user_type, vu.update_time as wz_user_update_time, \n");
        //微博评论
        sql.append(" wc.id as wc_id, wc.weibo_id as wc_weibo_id, wc.description as wc_description, \n" +
                "wc.create_time as wc_create_time, wc.create_user_id as wc_create_user_id, wc.position_x as wc_position_x, \n" +
                "wc.position_y as wc_position_y, wc.position_name as wc_position_name, \n" +
                "wc.school_id as wc_school_id, wc.user_id_to as wc_user_id_to, \n");
        sql.append("vu_1.name as wc_create_user_name, vu_1.TYPE AS wc_create_user_type, vu_1.update_time AS wc_create_user_update_time, \n" +
                "vu_2.name as wc_user_name_to, vu_2.TYPE AS wc_user_type_to, vu_2.update_time AS wc_user_update_time_to, \n");

        sql.append("       (select count(wc.*) from weibo_comment wc where wc.weibo_id = w.id) as comment_count, \n");
        sql.append("       c.name as classes_name, \n");
        sql.append("       u.name as create_user_name, \n");
        sql.append("       u.type as create_user_type, \n");
        sql.append("       u.update_time as create_user_update_time, \n");
        sql.append("       wf.time is not null as favorit \n");
        sql.append("  from weibo w \n");
        sql.append("  left join v_user_classes u \n");
        sql.append("    on w.create_user_id = u.id \n");
        sql.append("   and (u.classes_id is null or u.classes_id = w.classes_id) \n");
        sql.append("  left join classes c \n");
        sql.append("    on w.classes_Id = c.id \n");
        sql.append("  left join weibo_favorit wf \n");
        sql.append("    on wf.weibo_id = w.id \n");
        sql.append("   and wf.user_id = #{userId} \n");
        //微博点赞
        sql.append(" left join weibo_zan wz on wz.weibo_id=w.id \n");
        sql.append(" left join v_user_classes vu on vu.id=wz.user_id " +
                "and (vu.classes_id is null or vu.classes_id = w.classes_id)  \n");
        //微博评论
        sql.append("LEFT JOIN weibo_comment wc ON w. ID = wc.weibo_id\n" +
                "LEFT JOIN v_user_classes vu_1 ON wc.create_user_id = vu_1. ID\n" +
                "AND (\n" +
                "	vu_1.classes_id IS NULL\n" +
                "	OR vu_1.classes_id = w.classes_id\n" +
                ")\n" +
                "LEFT JOIN v_user_classes vu_2 ON wc.user_id_to = vu_2. ID\n" +
                "AND (\n" +
                "	vu_2.classes_id IS NULL\n" +
                "	OR vu_2.classes_id = w.classes_id\n" +
                ")\n");


        sql.append(" where w.id in ( \n");
        sql.append(" select id from weibo w \n");
        sql.append(" where w.school_id = #{schoolId} \n");
        Long classesId = (Long) paramMap.get("classesId");
        if (classesId != null && classesId == 0) {
            sql.append(" and w.classes_id is null");
        } else if (classesId != null && classesId != 0) {
            sql.append(" and w.classes_id = #{classesId}");
        }
        Long teacherId = (Long) paramMap.get("teacherId");
        if (teacherId != null) {
            sql.append(" and w.create_user_id = #{teacherId}");
        }

        Long studentId = (Long) paramMap.get("studentId");
        if (studentId != null) {
            sql.append(" and w.create_user_id in ( select parents_id from student_parents where student_id = #{studentId})");
        }
        if (paramMap.containsKey("keyword") && paramMap.get("keyword") != null && !paramMap.get("keyword").equals("")) {
            sql.append("   and (w.description like '%'||#{keyword}||'%') \n");
        }
        sql.append(" order by w.id desc \n");
        if (paramMap.containsKey("rows") && paramMap.get("rows") != null) {
            sql.append(" limit #{rows} offset #{offset} ");
        }

        sql.append(" )\n ");

        sql.append(" order by w.id desc,wc.id asc, wz.time \n");

        System.out.println(sql.toString());
        return sql.toString();
    }

    public String getListCount(Map<String, Object> paramMap) {
        StringBuilder sql = new StringBuilder(512);

        sql.append(" select count(*) from weibo w \n");
        sql.append(" where w.school_id = #{schoolId} \n");
        Long classesId = (Long) paramMap.get("classesId");
        if (classesId != null && classesId == 0) {
            sql.append(" and w.classes_id is null");
        } else if (classesId != null && classesId != 0) {
            sql.append(" and w.classes_id = #{classesId}");
        }
        Long teacherId = (Long) paramMap.get("teacherId");
        if (teacherId != null) {
            sql.append(" and w.create_user_id = #{teacherId}");
        }

        Long studentId = (Long) paramMap.get("studentId");
        if (studentId != null) {
            sql.append(" and w.create_user_id in ( select parents_id from student_parents where student_id = #{studentId})");
        }
        if (paramMap.containsKey("keyword") && paramMap.get("keyword") != null) {
            sql.append("   and (w.description like '%'||#{keyword}||'%') \n");
        }

        return sql.toString();
    }

    public String getClassesList(Map<String, Object> paramMap) {
        StringBuilder sql = new StringBuilder(512);
        sql.append("select w.*, \n");
        //微博点赞
        sql.append("  wz.weibo_id, wz.user_id, wz.time , vu.name wz_user_name," +
                " vu.type as wz_user_type, vu.update_time as wz_user_update_time, \n");
        //微博评论
        sql.append(" wc.id as wc_id, wc.weibo_id as wc_weibo_id, wc.description as wc_description, \n" +
                "wc.create_time as wc_create_time, wc.create_user_id as wc_create_user_id, wc.position_x as wc_position_x, \n" +
                "wc.position_y as wc_position_y, wc.position_name as wc_position_name, \n" +
                "wc.school_id as wc_school_id, wc.user_id_to as wc_user_id_to, \n");
        sql.append("vu_1.name as wc_create_user_name, vu_1.TYPE AS wc_create_user_type, vu_1.update_time AS wc_create_user_update_time, \n" +
                "vu_2.name as wc_user_name_to, vu_2.TYPE AS wc_user_type_to, vu_2.update_time AS wc_user_update_time_to, \n");

        sql.append("       (select count(wc.*) from weibo_comment wc where wc.weibo_id = w.id) as comment_count, \n");
        sql.append("       c.name as classes_name, \n");
        sql.append("       u.name as create_user_name, \n");
        sql.append("       u.type as create_user_type, \n");
        sql.append("       u.update_time as create_user_update_time, \n");
        sql.append("       wf.time is not null as favorit \n");
        sql.append("  from weibo w \n");
        sql.append("  left join v_user_classes u \n");
        sql.append("    on w.create_user_id = u.id \n");
        sql.append("   and (u.classes_id is null or u.classes_id = w.classes_id) \n");
        sql.append("  left join classes c \n");
        sql.append("    on w.classes_Id = c.id \n");
        sql.append("  left join weibo_favorit wf \n");
        sql.append("    on wf.weibo_id = w.id \n");
        //微博点赞
        sql.append(" left join weibo_zan wz on wz.weibo_id=w.id \n");
        sql.append(" left join v_user_classes vu on vu.id=wz.user_id " +
                "and (vu.classes_id is null or vu.classes_id = w.classes_id)  \n");
        //微博评论
        sql.append("LEFT JOIN weibo_comment wc ON w. ID = wc.weibo_id\n" +
                "LEFT JOIN v_user_classes vu_1 ON wc.create_user_id = vu_1. ID\n" +
                "AND (\n" +
                "	vu_1.classes_id IS NULL\n" +
                "	OR vu_1.classes_id = w.classes_id\n" +
                ")\n" +
                "LEFT JOIN v_user_classes vu_2 ON wc.user_id_to = vu_2. ID\n" +
                "AND (\n" +
                "	vu_2.classes_id IS NULL\n" +
                "	OR vu_2.classes_id = w.classes_id\n" +
                ")\n");

        sql.append(" where w.id in( \n");

        sql.append("select w.id from weibo w \n");
        sql.append(" where w.school_id = #{schoolId} \n");

        Long classesId = (Long) paramMap.get("classesId");
        if (classesId == null || classesId == 0) {
            sql.append(" and w.classes_id is not null ");
        } else {
            sql.append(" and w.classes_id = #{classesId} \n");
        }
        sql.append(" order by w.id desc ");//排序
        if (paramMap.containsKey("rows") && paramMap.get("rows") != null) {
            sql.append(" limit #{rows} offset #{offset} ");
        }

        sql.append(" )\n");

        sql.append("order by w.id desc, wc.id asc, wz.time");//排序

        return sql.toString();
    }


    public String getClassesListCount(Map<String, Object> paramMap) {
        StringBuilder sql = new StringBuilder(512);

        sql.append(" select count(*) from  weibo w \n");
        sql.append(" where w.school_id = #{schoolId} \n");

        Long classesId = (Long) paramMap.get("classesId");
        if (classesId == null || classesId == 0) {
            sql.append(" and w.classes_id is not null ");
        } else {
            sql.append(" and w.classes_id = #{classesId} \n");
        }

        return sql.toString();

    }

}
