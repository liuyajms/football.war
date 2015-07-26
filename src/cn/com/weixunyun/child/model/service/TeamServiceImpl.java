package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.bean.TeamPlayer;
import cn.com.weixunyun.child.model.dao.TeamMapper;
import cn.com.weixunyun.child.model.dao.TeamPlayerMapper;
import cn.com.weixunyun.child.model.vo.TeamPlayerVO;
import cn.com.weixunyun.child.model.vo.TeamVO;

import java.util.List;


public class TeamServiceImpl extends AbstractService implements TeamService {
    @Override
    public void delete(Long id) {
        super.getMapper(TeamMapper.class).delete(id);
    }

    @Override
    public void insert(Team record) {
        //创建球队，并加入球员
        super.getMapper(TeamMapper.class).insert(record);

        TeamPlayer teamPlayer = new TeamPlayer();
        teamPlayer.setPlayerId(record.getCreatePlayerId());
        teamPlayer.setTeamId(record.getId());

        super.getMapper(TeamPlayerMapper.class).insert(teamPlayer);
    }

    @Override
    public Team select(Long id) {
        return super.getMapper(TeamMapper.class).select(id);
    }

    @Override
    public TeamVO get(Long id) {
        TeamVO team = super.getMapper(TeamMapper.class).get(id);

        team.setRuleList(super.getDicValueList("team", "rule", team.getRule()));
        team.setColorList(super.getDicValueList("team", "color", team.getColor()));

        //查询球队成员
        List<TeamPlayerVO> teamPlayerList = super.getMapper(TeamPlayerMapper.class).getList(id, null, null);
        team.setTeamPlayerList(teamPlayerList);
        team.setPlayerCount(teamPlayerList.size());

        return team;
    }

    @Override
    public List<TeamVO> getList(String city, Integer rule, Integer beginAge, Integer endAge,
                                String keyword, Long sourceId, long rows, long offset) {
        List<TeamVO> list = super.getMapper(TeamMapper.class)
                .getList(city, rule, beginAge, endAge, keyword, sourceId, rows, offset);

        for (TeamVO team : list) {
            team.setRuleList(super.getDicValueList("team", "rule", team.getRule()));
        }

        return list;
    }

    @Override
    public void update(Team record) {
        super.getMapper(TeamMapper.class).update(record);
    }

    @Override
    public void updated(Long id) {
        super.getMapper(TeamMapper.class).updated(id);
    }

}
