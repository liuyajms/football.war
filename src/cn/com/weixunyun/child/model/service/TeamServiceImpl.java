package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.bean.TeamPlayer;
import cn.com.weixunyun.child.model.dao.TeamMapper;
import cn.com.weixunyun.child.model.dao.TeamPlayerMapper;
import cn.com.weixunyun.child.model.vo.TeamPlayerVO;
import cn.com.weixunyun.child.model.vo.TeamVO;
import cn.com.weixunyun.child.module.calendar.CalendarMapper;
import cn.com.weixunyun.child.module.easemob.EasemobHelper;
import cn.com.weixunyun.child.util.DateUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class TeamServiceImpl extends AbstractService implements TeamService {

    @Autowired
    TeamMapper mapper;

    @Override
    public void delete(Long id) {
        if (super.isHuanXinOpen()) {
            EasemobHelper.deleteGroup(super.getMapper(TeamMapper.class).select(id).getGroupId());
        }
        super.getMapper(TeamMapper.class).delete(id);
    }

    @Override
    public void insert(Team record) {
        //1.没有队长时，不创建群组
        if (super.isHuanXinOpen() && record.getCreatePlayerId() != null) {
            record.setGroupId(EasemobHelper.createGroup(record.getName(), new Long[]{record.getCreatePlayerId()}));
        }
        //2.创建球队
        super.getMapper(TeamMapper.class).insert(record);

        //3.将创建者加入该球队
        if (record.getCreatePlayerId() != null) {
            TeamPlayer teamPlayer = new TeamPlayer();
            teamPlayer.setPlayerId(record.getCreatePlayerId());
            teamPlayer.setTeamId(record.getId());
            teamPlayer.setAgreed(true);

            super.getMapper(TeamPlayerMapper.class).insert(teamPlayer);
        }

        //4.将队长加入球场收藏
        FavoriteServiceImpl favoriteService = new FavoriteServiceImpl();
        favoriteService.setSession(super.getSession());
        favoriteService.addFavorite(new Long[]{record.getCreatePlayerId()}, record.getCourtId());

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
        List<TeamPlayerVO> teamPlayerList = super.getMapper(TeamPlayerMapper.class).getList(id, null, null, null);
        team.setTeamPlayerList(teamPlayerList);
        team.setPlayerCount(teamPlayerList.size());

        //查询球队近两周的人员空闲情况
        Date beginDate = DateUtil.getMondayOfThisWeek();
        Date endDate = DateUtil.addDays(beginDate, 14);

        team.setFreeTimeList(super.getMapper(CalendarMapper.class).getListByTeamId(id, beginDate, endDate));


        return team;
    }

    @Override
    public List<TeamVO> getList(String city, Integer rule, Integer beginAge, Integer endAge,
                                String keyword, Long sourceId, Double px, Double py, long rows, long offset) {
        List<TeamVO> list = super.getMapper(TeamMapper.class)
                .getList(city, rule, beginAge, endAge, keyword, sourceId, px, py, rows, offset);

        for (TeamVO team : list) {
            team.setRuleList(super.getDicValueList("team", "rule", team.getRule()));
        }

        return list;
    }

    @Override
    public List<TeamVO> getAllList(Boolean tmp, int rows, int offset) {
        return super.getMapper(TeamMapper.class).getAllList(tmp, rows, offset);
    }

    @Override
    public void update(Team record) {
        Team team = super.getMapper(TeamMapper.class).get(record.getId());
        if (super.isHuanXinOpen() && record.getName() != null) {
            if (team.getName().equals(record.getName())) {
                EasemobHelper.updateGroup(team.getGroupId(), record.getName());
            }
        }

        //1.修改球队信息
        super.getMapper(TeamMapper.class).update(record);

        //2. 修改球队所有球员的球场收藏
        if (team.getCourtId() != null && !team.getCourtId().equals(record.getCourtId())) {
            FavoriteServiceImpl favoriteService = new FavoriteServiceImpl();
            favoriteService.setSession(super.getSession());

            List<TeamPlayerVO> playerVOList = super.getMapper(TeamPlayerMapper.class).getList(team.getId(), null, false, null);

            if (playerVOList.size() > 0) {
                ArrayList<Long> playerIdList = new ArrayList<>();
                for (TeamPlayerVO playerVO : playerVOList) {
                    playerIdList.add(playerVO.getPlayerId());
                }

                favoriteService.addFavorite(playerIdList.toArray(new Long[playerIdList.size()]), record.getCourtId());
            }

        }
    }

    @Override
    public void updated(Long id) {
        super.getMapper(TeamMapper.class).updated(id);
    }

    @Override
    public List<TeamVO> getPlayerTeamList(Long playerId, int rows, int offset) {
        List<TeamVO> list = mapper.getPlayerTeamList(playerId, rows, offset);
        for (TeamVO team : list) {
            team.setRuleList(super.getDicValueList("team", "rule", team.getRule()));
        }
        return list;
    }

    @Override
    public void insertPlayer(Team team, String[] playerIds, boolean agreed) {
        this.insert(team);//已将队长插入该球队,插入playerIds中其他队友时,需要过滤该队长

        if (playerIds != null) {
            for (String playerId : playerIds) {
                if (team.getCreatePlayerId() != null &&
                        team.getCreatePlayerId().toString().equals(playerId)) {
                    continue;
                }
                TeamPlayer teamPlayer = new TeamPlayer();
                teamPlayer.setPlayerId(Long.parseLong(playerId));
                teamPlayer.setTeamId(team.getId());
                teamPlayer.setAgreed(agreed);

                TeamPlayerServiceImpl teamPlayerService = new TeamPlayerServiceImpl();
                teamPlayerService.setSession(super.getSession());

                teamPlayerService.insert(teamPlayer);

            }
        }
    }
}
