package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.bean.TeamPlayer;
import cn.com.weixunyun.child.model.dao.MatchMapper;
import cn.com.weixunyun.child.model.dao.TeamMapper;
import cn.com.weixunyun.child.model.dao.TeamPlayerMapper;
import cn.com.weixunyun.child.model.vo.TeamPlayerVO;
import cn.com.weixunyun.child.model.vo.TeamVO;
import cn.com.weixunyun.child.module.calendar.CalendarMapper;
import cn.com.weixunyun.child.util.DateUtil;

import java.sql.Date;
import java.util.List;


public class TeamServiceImpl extends AbstractService implements TeamService {
    @Override
    public void delete(Long id) {
        super.getMapper(TeamMapper.class).delete(id);
    }

    @Override
    public void insert(Team record) {
        //创建球队，并将创建者加入该球队
        super.getMapper(TeamMapper.class).insert(record);

        TeamPlayer teamPlayer = new TeamPlayer();
        teamPlayer.setPlayerId(record.getCreatePlayerId());
        teamPlayer.setTeamId(record.getId());
        teamPlayer.setAgreed(true);

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

        //查询球队近两周的人员空闲情况
        Date beginDate = DateUtil.getMondayOfThisWeek();
        Date endDate = DateUtil.addDays(beginDate, 14);

        team.setFreeTimeList(super.getMapper(CalendarMapper.class).getListByTeamId(id, beginDate, endDate));

        team.setMatchList(super.getMapper(MatchMapper.class).getListByTeamId(id, beginDate, endDate));


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

    @Override
    public void insertPlayer(Team team, String[] playerIds) {
        this.insert(team);

        if (playerIds != null) {
            for (String playerId : playerIds) {
                TeamPlayer teamPlayer = new TeamPlayer();
                teamPlayer.setPlayerId(Long.parseLong(playerId));
                teamPlayer.setTeamId(team.getId());
                teamPlayer.setAgreed(true);

                super.getMapper(TeamPlayerMapper.class).insert(teamPlayer);
            }
        }
    }
}
