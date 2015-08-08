package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.model.bean.Match;
import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.dao.MatchMapper;
import cn.com.weixunyun.child.model.vo.MatchVO;
import cn.com.weixunyun.child.model.vo.TeamVO;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 2015/7/25.
 */
public class MatchServiceImpl extends AbstractService implements MatchService {
    @Autowired
    private MatchMapper matchMapper;

    @Autowired
    private TeamService teamService;


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
        //设置参赛队
        TeamVO team = teamService.get(matchVO.getTeamId());
        matchVO.setTeam(team);
        //设置迎战队
        TeamVO acceptTeam = matchVO.getAcceptTeamId() == null ? null : teamService.get(matchVO.getAcceptTeamId());
        matchVO.setAcceptTeam(acceptTeam);

        return matchVO;
    }

    private List<MatchVO> setTeamData(List<MatchVO> matchVOList) {
        for (MatchVO matchVO : matchVOList) {
            matchVO = setTeamData(matchVO);
            //清空球员列表
            matchVO.getTeam().setTeamPlayerList(null);

            if (matchVO.getAcceptTeam() != null) {
                matchVO.getAcceptTeam().setTeamPlayerList(null);
            }
        }
        return matchVOList;
    }

    @Override
    public List<MatchVO> getList(String city, Integer rule, Date beginDate, Date endDate,
                                 String keyword, long rows, long offset) {
        List<MatchVO> matchVOList = matchMapper.getList(city, rule, beginDate, endDate, keyword, rows, offset);

        return setTeamData(matchVOList);
    }

    @Override
    public void update(Match record) {
        matchMapper.update(record);
    }

    @Override
    public List<MatchVO> getMatchList(Long playerId, Long courtId, Long teamId, Integer type,
                                      Date beginDate, Date endDate,
                                      String keyword, int rows, int offset) {
        List<MatchVO> matchVOList = matchMapper
                .getMatchList(playerId, courtId, teamId, type, beginDate, endDate, keyword, rows, offset);
        return setTeamData(matchVOList);
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
    public void insertMatch(Match match, Team team, String[] playerIds) {
        teamService.insertPlayer(team, playerIds, false);
        matchMapper.insert(match);
    }

    @Override
    public void acceptMatch(Match match, Team team, String[] playerIds) {
        teamService.insertPlayer(team, playerIds, false);
        matchMapper.update(match);
    }

    @Override
    public void updateMatch(Match match, Team team) {
        teamService.update(team);
        matchMapper.update(match);
    }

    @Override
    public int deleteMatch(Long id) {
        Match match = matchMapper.get(id);

        teamService.delete(match.getTeamId());
        if (match.getAcceptTeamId() != null) {
            teamService.delete(match.getAcceptTeamId());
        }

        return matchMapper.delete(id);
    }
}
