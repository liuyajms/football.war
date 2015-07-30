package cn.com.weixunyun.child.module.calendar;

import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 2015/7/29.
 */
public interface CalendarMapper {
    int insert(@Param("playerId") Long playerId, @Param("freeTime") Date freeTime);

    int delete(@Param("playerId") Long playerId, @Param("freeTime") Date freeTime);

    /**
     * 用于查询球员日历
     * @param playerId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<Date> getListByPlayerId(@Param("playerId") Long playerId,
                                 @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    /**
     * 用于查询球队日历
     * @param teamId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<Map<Date, Integer>> getListByTeamId(@Param("teamId") Long teamId,
                                             @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);
}
