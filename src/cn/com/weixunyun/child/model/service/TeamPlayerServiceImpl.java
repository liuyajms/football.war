package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.TeamPlayer;
import cn.com.weixunyun.child.model.dao.TeamPlayerMapper;
import cn.com.weixunyun.child.model.vo.TeamPlayerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by PC on 2015/7/23.
 */
public class TeamPlayerServiceImpl extends AbstractService implements TeamPlayerService {
    @Override
    public int delete(@Param("teamId") Long teamId, @Param("playerId") Long playerId) {
        return super.getMapper(TeamPlayerMapper.class).delete(teamId, playerId);
    }

    @Override
    public void insert(TeamPlayer teamPlayer) {
        super.getMapper(TeamPlayerMapper.class).insert(teamPlayer);
    }

    @Override
    public List<TeamPlayerVO> getList(@Param("teamId") Long teamId, @Param("playerId") Long playerId, @Param("keyword") String keyword) {
        return super.getMapper(TeamPlayerMapper.class).getList(teamId, playerId, keyword);
    }

    @Override
    public void agreed(@Param("teamId") Long teamId, @Param("playerId") Long playerId) {
        super.getMapper(TeamPlayerMapper.class).agreed(teamId, playerId);
    }
}
