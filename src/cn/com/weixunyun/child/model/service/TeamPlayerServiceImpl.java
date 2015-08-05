package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.bean.TeamPlayer;
import cn.com.weixunyun.child.model.dao.TeamMapper;
import cn.com.weixunyun.child.model.dao.TeamPlayerMapper;
import cn.com.weixunyun.child.model.vo.TeamPlayerVO;
import cn.com.weixunyun.child.module.easemob.EasemobHelper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by PC on 2015/7/23.
 */
public class TeamPlayerServiceImpl extends AbstractService implements TeamPlayerService {
    @Override
    public int delete(Long teamId, Long playerId) {
        int n = super.getMapper(TeamPlayerMapper.class).delete(teamId, playerId);

        if (super.isHuanXinOpen()) {
            String groupId = super.getMapper(TeamMapper.class).select(teamId).getGroupId();
            EasemobHelper.addUserToGroup(groupId, playerId);
        }

        return n;
    }

    @Override
    public void insert(TeamPlayer teamPlayer) {
        super.getMapper(TeamPlayerMapper.class).insert(teamPlayer);

        if (super.isHuanXinOpen()) {
            String groupId = super.getMapper(TeamMapper.class).select(teamPlayer.getTeamId()).getGroupId();
            EasemobHelper.addUserToGroup(groupId, teamPlayer.getPlayerId());
        }
    }

    @Override
    public List<TeamPlayerVO> getList(Long teamId, Long playerId, String keyword) {
        return super.getMapper(TeamPlayerMapper.class).getList(teamId, playerId, keyword);
    }

    @Override
    public void agreed(@Param("teamId") Long teamId, @Param("playerId") Long playerId) {
        super.getMapper(TeamPlayerMapper.class).agreed(teamId, playerId);
    }

    @Override
    public void deleteCreatePlayerId(Long teamId, Long playerId) {
        TeamPlayerMapper mapper = super.getMapper(TeamPlayerMapper.class);

        TeamMapper teamMapper = super.getMapper(TeamMapper.class);

        this.delete(teamId, playerId);

        List<TeamPlayerVO> list = mapper.getList(teamId, playerId, null);
        if (list.size() > 0) {//appoint create_player_id
            Team team = new Team();
            team.setId(teamId);
            team.setCreatePlayerId(list.get(0).getPlayerId());
            teamMapper.update(team);
        } else {//delete team
            teamMapper.delete(teamId);
        }
    }
}
