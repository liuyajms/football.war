package cn.com.weixunyun.child.module.calendar;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.model.service.AbstractService;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 2015/7/29.
 */
public class CalendarServiceImpl extends AbstractService implements CalendarService {
    @Autowired
    private CalendarMapper mapper;

    @Override
    public int insert(@Param("playerId") Long playerId, @Param("freeTime") Date freeTime) {
        return mapper.insert(playerId, freeTime);
    }

    @Override
    public int delete(@Param("playerId") Long playerId, @Param("freeTime") Date freeTime) {
        return mapper.delete(playerId, freeTime);
    }

    @Override
    public List<Date> getListByPlayerId(@Param("playerId") Long playerId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate) {
        return mapper.getListByPlayerId(playerId, beginDate, endDate);
    }

    @Override
    public List<Map<Date, Integer>> getListByTeamId(@Param("teamId") Long teamId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate) {
        return mapper.getListByTeamId(teamId, beginDate, endDate);
    }
}
