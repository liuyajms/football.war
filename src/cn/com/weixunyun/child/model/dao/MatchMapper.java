package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.bean.Match;
import cn.com.weixunyun.child.model.vo.MatchVO;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

public interface MatchMapper {
    int delete(Long id);

    void insert(Match record);

    MatchVO get(Long id);

    List<MatchVO> getList(@Param("city") String city, @Param("rule") Integer rule,
                          @Param("beginDate") Date beginDate, @Param("endDate") Date endDate,
                          @Param("keyword") String keyword, @Param("rows") long rows, @Param("offset") long offset);

    void update(Match record);

    List<MatchVO> getPlayerMatchList(@Param("playerId") Long playerId, @Param("type") Integer type,
                                     @Param("beginDate") Date beginDate, @Param("endDate") Date endDate,
                                     @Param("keyword") String keyword, @Param("rows") long rows, @Param("offset") long offset);

}