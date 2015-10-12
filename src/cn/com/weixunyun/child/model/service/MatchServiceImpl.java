package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.model.bean.Match;
import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.dao.MatchMapper;
import cn.com.weixunyun.child.model.dao.TeamMapper;
import cn.com.weixunyun.child.model.dao.TeamPlayerMapper;
import cn.com.weixunyun.child.model.vo.MatchVO;
import cn.com.weixunyun.child.model.vo.TeamPlayerVO;
import cn.com.weixunyun.child.model.vo.TeamVO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 2015/7/25.
 * bug：凡使用到teamService的方法，均存在事务不一致的bug。。。
 */
public class MatchServiceImpl extends AbstractService implements MatchService {
    @Autowired
    private MatchMapper matchMapper;

//    @Autowired
//    private TeamService teamService;

//    private TeamService teamService;

    public TeamService getTeamService() {
        TeamServiceImpl teamService = new TeamServiceImpl();
        teamService.setSession(super.getSession());

        return teamService;
    }


    @Override
    public int delete(Long id) {
        return matchMapper.delete(id);
    }

    @Override
    public void insert(Match record) {
        matchMapper.insert(record);
    }

    @Override
    public MatchVO get(Long id) {
        return setTeamData(matchMapper.get(id));
    }

    private MatchVO setTeamData(MatchVO matchVO) {
       /* //设置参赛队
        TeamVO team = teamService.get(matchVO.getTeamId());
        matchVO.setTeam(team);
        //设置迎战队
        TeamVO acceptTeam = matchVO.getAcceptTeamId() == null ? null : teamService.get(matchVO.getAcceptTeamId());
        matchVO.setAcceptTeam(acceptTeam);*/

        //获取参赛队，及其队员列表
        TeamVO team = getTeamVO(matchVO.getTeamId());

        //获取应战队，及其队员列表
        TeamVO acceptTeam = getTeamVO(matchVO.getAcceptTeamId());

        matchVO.setTeam(team);
        matchVO.setAcceptTeam(acceptTeam);

        //设置赛制
        if (matchVO.getRule() != null) {
            matchVO.setRuleName(super.getDicValueList("team", "rule", matchVO.getRule()).get(0));
        }

        return matchVO;
    }

    private TeamVO getTeamVO(Long teamId) {
        if (teamId != null) {
            TeamVO team = super.getMapper(TeamMapper.class).get(teamId);
            //获取临时球队的球员列表
            List<TeamPlayerVO> teamPlayerList = super.getMapper(TeamPlayerMapper.class).getList(teamId, null, true, null);
            team.setTeamPlayerList(teamPlayerList);
            team.setPlayerCount(teamPlayerList.size());
            team.setColorList(super.getDicValueList("team", "color", team.getColor()));

            return team;
        }
        return null;
    }

    private List<MatchVO> setTeamData(List<MatchVO> matchVOList) {
        List<MatchVO> resultList = new ArrayList<>();
        for (MatchVO matchVO : matchVOList) {
            //如果非公开，并且应战方为空，则不显示在附近中。
//            if(!matchVO.getOpen() && matchVO.getAcceptTeamId() == null){
//                continue;
//            }

            matchVO = setTeamData(matchVO);
            //清空球员列表
            matchVO.getTeam().setTeamPlayerList(null);

            if (matchVO.getAcceptTeam() != null) {
                matchVO.getAcceptTeam().setTeamPlayerList(null);
            }

            resultList.add(matchVO);
        }
        return resultList;
    }

    @Override
    public List<MatchVO> getList(String city, Integer rule, Date beginDate, Date endDate,
                                 String keyword, Double px, Double py, long rows, long offset) {
        List<MatchVO> matchVOList = matchMapper.getList(city, rule, beginDate, endDate, keyword, px, py, rows, offset);

        return setTeamData(matchVOList);
    }

    @Override
    public void update(Match record) {
        matchMapper.update(record);
    }

    @Override
    public Match select(Long id) {
        return matchMapper.select(id);
    }

    @Override
    public List<Map<Date, Integer>> getListByPlayerId(Long playerId, Date beginDate, Date endDate) {
        return matchMapper.getListByPlayerId(playerId, beginDate, endDate);
    }

    @Override
    public List<Map<Date, Integer>> getListByTeamId(Long teamId, Date beginDate, Date endDate) {
        return matchMapper.getListByTeamId(teamId, beginDate, endDate);
    }

    @Override
    public List<MatchVO> getPlayerMatchList(Long playerId, Integer type,
                                            Date beginDate, Date endDate, String keyword, int rows, int offset) {
        List<MatchVO> matchVOList = matchMapper
                .getPlayerMatchList(playerId, type, beginDate, endDate, keyword, rows, offset);
        return setTeamData(matchVOList);
    }

    @Override
    public List<MatchVO> getCourtMatchList(Long courtId, Integer type,
                                           Date beginDate, Date endDate, String keyword, int rows, int offset) {
        List<MatchVO> matchVOList = matchMapper.getCourtMatchList(courtId, type, beginDate, endDate, keyword, rows, offset);
        return setTeamData(matchVOList);
    }

    @Override
    public List<MatchVO> getTeamMatchList(Long teamId, Integer type,
                                          Date beginDate, Date endDate, String keyword, int rows, int offset) {
        List<MatchVO> matchVOList = matchMapper.getTeamMatchList(teamId, type, beginDate, endDate, keyword, rows, offset);
        return setTeamData(matchVOList);
    }

    @Override
    public void setOpen(Long id, boolean open) {
        matchMapper.setOpen(id, open);
    }


    @Override
    public void insertMatch(Match match, Team team, Team acceptTeam, String[] playerIds) {
        getTeamService().insertPlayer(team, playerIds, true);//审核字段默认同意，由环信审核
        if (acceptTeam.getId() != null) {//创建客场队
            getTeamService().insertPlayer(acceptTeam, null, true);
        }
        matchMapper.insert(match);
    }

    @Override
    public void acceptMatch(Match match, Team team, String[] playerIds) {
        getTeamService().insertPlayer(team, playerIds, true);//审核字段默认同意，由环信审核
        matchMapper.update(match);
    }

    @Override
    public void updateMatch(Match match, Team team) {
        getTeamService().update(team);
        matchMapper.update(match);
    }

    @Override
    public int deleteMatch(Long id) {
        Match match = matchMapper.get(id);

        getTeamService().delete(match.getTeamId());
        if (match.getAcceptTeamId() != null) {
            getTeamService().delete(match.getAcceptTeamId());
        }

        return matchMapper.delete(id);
    }
}
