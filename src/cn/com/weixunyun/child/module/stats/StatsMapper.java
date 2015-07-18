package cn.com.weixunyun.child.module.stats;

import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public interface StatsMapper {

    @SelectProvider(type = StatsMapperProvider.class, method = "listSchoolWeibo")
    public List<SchoolStats> listSchoolWeibo(Map<String, Object> params); // 按日统计校园微博数

    @SelectProvider(type = StatsMapperProvider.class, method = "listSchoolNotice")
    public List<SchoolStats> listSchoolNotice(Map<String, Object> params); // 按日统计校园公告

    @SelectProvider(type = StatsMapperProvider.class, method = "listSchoolNews")
    public List<SchoolStats> listSchoolNews(Map<String, Object> params); // 按日统计校园新闻数


    @SelectProvider(type = StatsMapperProvider.class, method = "listVisitParents")
    public List<VisitStats> listVisitParents(Map<String, Object> params); // 按日统计家长登陆人次

    @SelectProvider(type = StatsMapperProvider.class, method = "listVisitTeachers")
    public List<VisitStats> listVisitTeachers(Map<String, Object> params); // 按日统计教师登陆人次


    @Deprecated
    @SelectProvider(type = StatsMapperProvider.class, method = "listLogin")
    public List<LoginStats> listLogin(Map<String, Object> params); // 时间区间内，已有帐号和登录帐号比较数(以饼图方式展现)

    @SelectProvider(type = StatsMapperProvider.class, method = "getTotalTeachers")
    public int getTotalTeachers(Map<String, Object> params); // 总的注册教师数

    @SelectProvider(type = StatsMapperProvider.class, method = "getTotalParents")
    public int getTotalParents(Map<String, Object> params); // 总的注册家长数

    @SelectProvider(type = StatsMapperProvider.class, method = "getLoginTeachers")
    public int getLoginTeachers(Map<String, Object> params); // 登陆教师数

    @SelectProvider(type = StatsMapperProvider.class, method = "getLoginParents")
    public int getLoginParents(Map<String, Object> params); // 登陆家长数


    @SelectProvider(type = StatsMapperProvider.class, method = "listClassBroadcast")//获取班级广播信息
    public List<ClassStats> listClassBroadcast(Map<String, Object> params);

    @SelectProvider(type = StatsMapperProvider.class, method = "listClassWeibo")
    public List<ClassStats> listClassWeibo(Map<String, Object> params);

    @SelectProvider(type = StatsMapperProvider.class, method = "listClassStars")
    public List<ClassStats> listClassStars(Map<String, Object> params);

    @SelectProvider(type = StatsMapperProvider.class, method = "listClassUploads")
    public List<ClassStats> listClassUploads(Map<String, Object> params);

    @SelectProvider(type = StatsMapperProvider.class, method = "listClassParentsWeibo")
    public List<ClassStats> listClassParentsWeibo(Map<String, Object> params);

}