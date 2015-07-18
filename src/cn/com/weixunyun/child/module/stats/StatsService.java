package cn.com.weixunyun.child.module.stats;

import java.util.List;
import java.util.Map;

public interface StatsService extends StatsMapper {
    public Map<String, List<ClassStats>> getClassMap(Map<String, Object> params);

    public Map<String, List<SchoolStats>> getSchoolMap(Map<String, Object> params);

    public Map<String, List<VisitStats>> getVisitMap(Map<String, Object> params);

    public LoginStats getLoginStats(Map<String, Object> params);
}
