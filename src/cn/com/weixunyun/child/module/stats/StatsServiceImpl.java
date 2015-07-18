package cn.com.weixunyun.child.module.stats;

import cn.com.weixunyun.child.model.service.AbstractService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsServiceImpl extends AbstractService implements StatsService {

    public Map<String, List<ClassStats>> getClassMap(Map<String, Object> params) {

        Map<String, List<ClassStats>> resultMap = new HashMap<String, List<ClassStats>>();
        StatsMapper mapper = getMapper(StatsMapper.class);
        resultMap.put("broadcastStatsList", mapper.listClassBroadcast(params));
        resultMap.put("weiboStatsList", mapper.listClassWeibo(params));
        resultMap.put("starsStatsList", mapper.listClassStars(params));
        resultMap.put("uploadsStatsList", mapper.listClassUploads(params));

        return resultMap;
    }

    @Override
    public Map<String, List<SchoolStats>> getSchoolMap(Map<String, Object> params) {
        Map<String, List<SchoolStats>> resultMap = new HashMap<String, List<SchoolStats>>();
        StatsMapper mapper = getMapper(StatsMapper.class);
        resultMap.put("newsStatsList", mapper.listSchoolNews(params));
        resultMap.put("noticeStatsList", mapper.listSchoolNotice(params));
        resultMap.put("weiboStatsList", mapper.listSchoolWeibo(params));
        return resultMap;
    }

    @Override
    public Map<String, List<VisitStats>> getVisitMap(Map<String, Object> params) {
        Map<String, List<VisitStats>> resultMap = new HashMap<String, List<VisitStats>>();
        StatsMapper mapper = getMapper(StatsMapper.class);
        resultMap.put("parentsStatsList", mapper.listVisitParents(params));
        resultMap.put("teachersStatsList", mapper.listVisitTeachers(params));
        return resultMap;
    }

    @Override
    public LoginStats getLoginStats(Map<String, Object> params) {
        StatsMapper mapper = getMapper(StatsMapper.class);
        int totalTeachers = mapper.getTotalTeachers(params);
        int totalParents = mapper.getTotalParents(params);
        int loginParents = mapper.getLoginParents(params);
        int loginTeachers = mapper.getLoginTeachers(params);
        LoginStats stats = new LoginStats(totalParents,totalTeachers,loginParents,loginTeachers);
        return stats;
    }


    @Override
    public List<SchoolStats> listSchoolWeibo(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).listSchoolWeibo(params);
    }

    @Override
    public List<SchoolStats> listSchoolNotice(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).listSchoolNotice(params);
    }

    @Override
    public List<SchoolStats> listSchoolNews(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).listSchoolNews(params);
    }

    @Override
    public List<VisitStats> listVisitParents(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).listVisitParents(params);
    }

    @Override
    public List<VisitStats> listVisitTeachers(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).listVisitTeachers(params);
    }

    @Override
    public List<LoginStats> listLogin(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).listLogin(params);
    }

    @Override
    public int getTotalTeachers(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).getTotalTeachers(params);
    }

    @Override
    public int getTotalParents(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).getTotalParents(params);
    }

    @Override
    public int getLoginTeachers(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).getLoginTeachers(params);
    }

    @Override
    public int getLoginParents(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).getLoginParents(params);
    }

    @Override
    public List<ClassStats> listClassBroadcast(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).listClassBroadcast(params);
    }

    @Override
    public List<ClassStats> listClassWeibo(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).listClassWeibo(params);
    }

    @Override
    public List<ClassStats> listClassStars(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).listClassStars(params);
    }

    @Override
    public List<ClassStats> listClassUploads(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).listClassUploads(params);
    }

    @Override
    public List<ClassStats> listClassParentsWeibo(Map<String, Object> params) {
        return super.getMapper(StatsMapper.class).listClassParentsWeibo(params);
    }


}
