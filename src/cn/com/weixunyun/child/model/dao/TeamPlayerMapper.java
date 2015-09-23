package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.bean.TeamPlayer;
import cn.com.weixunyun.child.model.vo.TeamPlayerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeamPlayerMapper {
    int delete(@Param("teamId") Long teamId, @Param("playerId") Long playerId);

    void insert(TeamPlayer teamPlayer);

    List<TeamPlayerVO> getList(@Param("teamId") Long teamId, @Param("playerId") Long playerId,
                               @Param("tmp") Boolean tmp,
                               @Param("keyword") String keyword);

    void agreed(@Param("teamId") Long teamId, @Param("playerId") Long playerId);

    int getCount(@Param("teamId") Long teamId, @Param("playerId") Long playerId);
}