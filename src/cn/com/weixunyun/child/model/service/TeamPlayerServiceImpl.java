package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.bean.TeamPlayer;
import cn.com.weixunyun.child.model.dao.FriendMapper;
import cn.com.weixunyun.child.model.dao.TeamMapper;
import cn.com.weixunyun.child.model.dao.TeamPlayerMapper;
import cn.com.weixunyun.child.model.vo.TeamPlayerVO;
import cn.com.weixunyun.child.module.easemob.EasemobHelper;

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
    public List<TeamPlayerVO> getList(Long teamId, Long playerId, Boolean tmp, String keyword) {
        return super.getMapper(TeamPlayerMapper.class).getList(teamId, playerId, tmp, keyword);
    }

    @Override
    public void agreed(Long teamId, Long playerId) {
        super.getMapper(TeamPlayerMapper.class).agreed(teamId, playerId);
    }

    @Override
    public int getCount(Long teamId, Long playerId) {
        return super.getMapper(TeamPlayerMapper.class).getCount(teamId, playerId);
    }

    @Override
    public void deleteCreatePlayerId(Long teamId, Long playerId) {
        TeamPlayerMapper mapper = super.getMapper(TeamPlayerMapper.class);

        TeamMapper teamMapper = super.getMapper(TeamMapper.class);

        this.delete(teamId, playerId);

        //获取playerId球队的所有球员
        List<TeamPlayerVO> list = mapper.getList(teamId, playerId, null, null);
        if (list.size() > 0) {//appoint create_player_id
            Team team = new Team();
            team.setId(teamId);
            team.setCreatePlayerId(list.get(0).getPlayerId());
            teamMapper.update(team);
        } else {//delete team
            teamMapper.delete(teamId);
        }
    }

    @Override
    public void insertMulti(Long userId, Long teamId, String[] playerIds) {

        TeamPlayer teamPlayer = new TeamPlayer();
        teamPlayer.setTeamId(teamId);
        teamPlayer.setAgreed(true);

        for (String playerId : playerIds) {
            if (super.getMapper(FriendMapper.class).isFriend(userId, Long.valueOf(playerId)) == 0) {
                throw new RuntimeException("请检查该球员是否为您的好友");
            }

            //检查是否已有该记录,注意：存在并发，可能导致数据不同步
            if (super.getMapper(TeamPlayerMapper.class).getCount(teamId, Long.valueOf(playerId)) > 0) {
                continue;
            }

            teamPlayer.setPlayerId(Long.valueOf(playerId));
            this.insert(teamPlayer);

          /*  try {
            } catch (RuntimeException e) {
                if(!e.getMessage().contains("un_team_player")){
                    throw new RuntimeException(e.getMessage());
                }
            }*/
        }

    }

    @Override
    public void deleteMulti(Long authedId, Long teamId, String[] playerIds) {

        Long createPlayerId = null;
        for (String playerId : playerIds) {
            if (playerId.equals(authedId.toString())) {//删除创建者
                createPlayerId = Long.valueOf(playerId);
                continue;
            }
            this.delete(teamId, Long.valueOf(playerId));
        }

        if (createPlayerId != null) {
            this.deleteCreatePlayerId(teamId, createPlayerId);
        }

    }
}
