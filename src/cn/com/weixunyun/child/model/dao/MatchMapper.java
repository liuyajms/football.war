package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.bean.Match;
import cn.com.weixunyun.child.model.vo.MatchVO;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface MatchMapper {
    int delete(Long id);

    void insert(Match record);

    MatchVO get(Long id);

    List<MatchVO> getList(@Param("city") String city, @Param("rule") Integer rule,
                          @Param("beginDate") Date beginDate, @Param("endDate") Date endDate,
                          @Param("keyword") String keyword,
                          @Param("px") Double px, @Param("py") Double py,
                          @Param("rows") long rows, @Param("offset") long offset);

    void update(Match record);

    Match select(Long id);

    /**
     * 用于查询球员近两周参与的球赛数
     *
     * @param playerId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<Map<Date, Integer>> getListByPlayerId(@Param("playerId") Long playerId,
                                               @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);


    /**
     * 用于查询球队近两周参与的球赛数
     *
     * @param teamId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<Map<Date, Integer>> getListByTeamId(@Param("teamId") Long teamId,
                                             @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    List<MatchVO> getPlayerMatchList(@Param("playerId") Long playerId, @Param("type") Integer type,
                                     @Param("beginDate") Date beginDate, @Param("endDate") Date endDate,
                                     @Param("keyword") String keyword, @Param("rows") int rows, @Param("offset") int offset);

    List<MatchVO> getCourtMatchList(@Param("courtId") Long courtId, @Param("type") Integer type,
                                    @Param("beginDate") Date beginDate, @Param("endDate") Date endDate,
                                    @Param("keyword") String keyword, @Param("rows") int rows, @Param("offset") int offset);

    List<MatchVO> getTeamMatchList(@Param("teamId") Long teamId, @Param("type") Integer type,
                                   @Param("beginDate") Date beginDate, @Param("endDate") Date endDate,
                                   @Param("keyword") String keyword, @Param("rows") int rows, @Param("offset") int offset);

    void setOpen(@Param("id") Long id, @Param("open") boolean open);
}