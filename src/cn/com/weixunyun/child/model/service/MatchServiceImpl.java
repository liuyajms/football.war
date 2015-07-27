package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.model.bean.Match;
import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.dao.MatchMapper;
import cn.com.weixunyun.child.model.vo.MatchVO;
import cn.com.weixunyun.child.model.vo.TeamVO;

import java.sql.Date;
import java.util.List;

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
    public List<MatchVO> getPlayerMatchList(Long playerId, Integer type, Date beginDate, Date endDate,
                                            String keyword, long rows, long offset) {
        List<MatchVO> matchVOList = matchMapper
                .getPlayerMatchList(playerId, type, beginDate, endDate, keyword, rows, offset);
        return setTeamData(matchVOList);
    }


    @Override
    public void insertMatch(Team team, Match match) {
        teamService.insert(team);
        matchMapper.insert(match);
//        teamPlayerMapper.insert(teamPlayer);
    }

    @Override
    public void acceptMatch(Team team, Match match) {
        teamService.insert(team);
        matchMapper.update(match);
//        teamPlayerMapper.insert(teamPlayer);
    }
}
