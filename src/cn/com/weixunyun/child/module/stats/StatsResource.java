package cn.com.weixunyun.child.module.stats;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.util.DateUtil;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.util.*;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
@Description("统计查询")
public class StatsResource extends AbstractResource {

    @GET
    @Path("class")
    public List<ClassStats> getClassList(@CookieParam("rsessionid") String rsessionid,
                                         @QueryParam("schoolId") Long schoolId,
                                         @QueryParam("classId") Long classId,
                                         @QueryParam("beginDate") String beginDate,
                                         @QueryParam("endDate") String endDate,
                                         @QueryParam("page") int page, @QueryParam("rows") int rows) {
        long time1 = System.currentTimeMillis();

        StatsService service = super.getService(StatsService.class);

        Map<String, Object> params = new HashMap<String, Object>();
        if (schoolId != null) {//为空则查询所有
            params.put("schoolId", schoolId);
        }
        params.put("offset", page * rows);
        params.put("rows", rows);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        params.put("classId", classId);

        Map<String, List<ClassStats>> resultMap = service.getClassMap(params);

        List<ClassStats> broadcastStatsList = resultMap.get("broadcastStatsList");
        List<ClassStats> weiboStatsList = resultMap.get("weiboStatsList");
        List<ClassStats> starsStatsList = resultMap.get("starsStatsList");
        List<ClassStats> uploadsStatsList = resultMap.get("uploadsStatsList");

        Date beginSqlDate = Date.valueOf(beginDate);
        Date endSqlDate = Date.valueOf(endDate);
        int dayLength = DateUtil.getDateMinus(beginSqlDate, endSqlDate, DateUtil.DAY_TYPE);

        ClassStats[] stats = new ClassStats[dayLength];
        List<java.sql.Date> dateList = getDateList(beginDate, endDate);
        System.out.println("=====getClassesDataList cost:" + (System.currentTimeMillis() - time1));

        for (int i = 0; i < dateList.size(); i++) {
            stats[i] = new ClassStats(dateList.get(i), 0, 0, 0, 0);
        }

        for (ClassStats obj : broadcastStatsList) {
            int day = DateUtil.getDateMinus(obj.getDate(), beginSqlDate, DateUtil.DAY_TYPE);
            stats[day].setBroadcast(obj.getBroadcast());
        }

        for (ClassStats obj : weiboStatsList) {
            int day = DateUtil.getDateMinus(obj.getDate(), beginSqlDate, DateUtil.DAY_TYPE);
            stats[day].setClassWeibo(obj.getClassWeibo());
        }

        for (ClassStats obj : starsStatsList) {
            int day = DateUtil.getDateMinus(obj.getDate(), beginSqlDate, DateUtil.DAY_TYPE);
            stats[day].setStars(obj.getStars());
        }

        for (ClassStats obj : uploadsStatsList) {
            int day = DateUtil.getDateMinus(obj.getDate(), beginSqlDate, DateUtil.DAY_TYPE);
            stats[day].setUploads(obj.getUploads());
        }

        System.out.println("=====total cost:" + (System.currentTimeMillis() - time1));

        return Arrays.asList(stats);
    }

    @GET
    @Path("school")
    @Description("")
    public List<SchoolStats> getSchoolList(@CookieParam("rsessionid") String rsessionid,
                                           @QueryParam("schoolId") Long schoolId,
                                           @QueryParam("beginDate") String beginDate,
                                           @QueryParam("endDate") String endDate,
                                           @QueryParam("page") int page, @QueryParam("rows") int rows) {
        long time1 = System.currentTimeMillis();
        StatsService service = super.getService(StatsService.class);

        Map<String, Object> params = new HashMap<String, Object>();
        if (schoolId != null) {//为空则查询所有
            params.put("schoolId", schoolId);
        }
        params.put("offset", page * rows);
        params.put("rows", rows);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);


        Map<String, List<SchoolStats>> resultMap = service.getSchoolMap(params);

        List<SchoolStats> noticeStatsList = resultMap.get("noticeStatsList");
        List<SchoolStats> weiboStatsList = resultMap.get("weiboStatsList");
        List<SchoolStats> newsStatsList = resultMap.get("newsStatsList");


        Date beginSqlDate = Date.valueOf(beginDate);
        Date endSqlDate = Date.valueOf(endDate);
        int dayLength = DateUtil.getDateMinus(beginSqlDate, endSqlDate, DateUtil.DAY_TYPE);

        SchoolStats[] stats = new SchoolStats[dayLength];

        List<java.sql.Date> dateList = getDateList(beginDate, endDate);
        System.out.println("=====getSchoolDateList cost:" + (System.currentTimeMillis() - time1));

        for (int i = 0; i < dateList.size(); i++) {
            stats[i] = new SchoolStats(dateList.get(i), 0, 0, 0);
        }

        for (SchoolStats notice : noticeStatsList) {
            int day = DateUtil.getDateMinus(notice.getDate(), beginSqlDate);
            stats[day].setNotice(notice.getNotice());
        }

        for (SchoolStats obj : weiboStatsList) {
            int day = DateUtil.getDateMinus(obj.getDate(), beginSqlDate);
            stats[day].setWeibo(obj.getWeibo());
        }

        for (SchoolStats obj : newsStatsList) {
            int day = DateUtil.getDateMinus(obj.getDate(), beginSqlDate);
            stats[day].setNews(obj.getNews());
        }

        System.out.println("=====total cost:" + (System.currentTimeMillis() - time1));

        return Arrays.asList(stats);

    }


    @GET
    @Path("visit")
    @Description("查询不同客户端访问情况")
    public List<VisitStats> getVisitList(@CookieParam("rsessionid") String rsessionid,
                                         @QueryParam("schoolId") Long schoolId,
                                         @QueryParam("beginDate") String beginDate,
                                         @QueryParam("endDate") String endDate,
                                         @QueryParam("page") int page, @QueryParam("rows") int rows) {
        long time1 = System.currentTimeMillis();
        StatsService service = super.getService(StatsService.class);

        Map<String, Object> params = new HashMap<String, Object>();
        if (schoolId != null) {//为空则查询所有
            params.put("schoolId", schoolId);
        }
        params.put("offset", page * rows);
        params.put("rows", rows);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);

        Map<String, List<VisitStats>> resultMap = service.getVisitMap(params);

        List<VisitStats> teachersStatsList = resultMap.get("teachersStatsList");
        List<VisitStats> parentsStatsList = resultMap.get("parentsStatsList");

        Date beginSqlDate = Date.valueOf(beginDate);
        Date endSqlDate = Date.valueOf(endDate);
        int dayLength = DateUtil.getDateMinus(beginSqlDate, endSqlDate);

        VisitStats[] stats = new VisitStats[dayLength];
        List<java.sql.Date> dateList = getDateList(beginDate, endDate);
        System.out.println("=====getVisitDataList cost:" + (System.currentTimeMillis() - time1));

        for (int i = 0; i < dateList.size(); i++) {
            stats[i] = new VisitStats(dateList.get(i), 0, 0);
        }

        for (VisitStats obj : teachersStatsList) {
            int day = DateUtil.getDateMinus(obj.getDate(), beginSqlDate);
            stats[day].setTeachers(obj.getTeachers());
        }

        for (VisitStats obj : parentsStatsList) {
            int day = DateUtil.getDateMinus(obj.getDate(), beginSqlDate);
            stats[day].setParents(obj.getParents());
        }

        System.out.println("=====total cost:" + (System.currentTimeMillis() - time1));

        return Arrays.asList(stats);
    }


    @GET
    @Path("login")
    @Description("")
    public LoginStats listLogin(@CookieParam("rsessionid") String rsessionid,
                                      @QueryParam("schoolId") Long schoolId,
                                      @QueryParam("beginDate") String beginDate,
                                      @QueryParam("endDate") String endDate,
                                      @QueryParam("page") int page, @QueryParam("rows") int rows) {
        StatsService service = super.getService(StatsService.class);

        Map<String, Object> params = new HashMap<String, Object>();
        if (schoolId != null) {//为空则查询所有
            params.put("schoolId", schoolId);
        }
        params.put("offset", page * rows);
        params.put("rows", rows);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);

        long t1 = System.currentTimeMillis();

        LoginStats stats = service.getLoginStats(params);

        System.out.println("=======getLoginStats cost:"+ (System.currentTimeMillis() - t1));

        return stats;
    }


    /**
     * 获取指定区间内时间
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<Date> getDateList(String beginDate, String endDate) {
        List<Date> dateList = new ArrayList<Date>();

        int year = Integer.parseInt(beginDate.split("-")[0]);
        int month = Integer.parseInt(beginDate.split("-")[1]);
        int day = Integer.parseInt(beginDate.split("-")[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        int y = Integer.parseInt(endDate.split("-")[0]);
        int m = Integer.parseInt(endDate.split("-")[1]);
        int d = Integer.parseInt(endDate.split("-")[2]);
        Calendar c = Calendar.getInstance();
        c.set(y, m - 1, d);

        if (c.getTimeInMillis() < calendar.getTimeInMillis()) {
            return null;
        }

        int length = (int) ((c.getTimeInMillis() - calendar.getTimeInMillis()) / (1000 * 24 * 3600));


        for (int i = 0; i < length; i++) {
            dateList.add(new Date(calendar.getTimeInMillis()));
            calendar.add(Calendar.DATE, 1);
        }

        return dateList;
    }
}
