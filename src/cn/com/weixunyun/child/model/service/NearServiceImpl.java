package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.NearMapper;
import cn.com.weixunyun.child.model.dao.TeamPlayerMapper;
import cn.com.weixunyun.child.model.vo.MatchVO;
import cn.com.weixunyun.child.model.vo.Near;

import java.util.List;

/**
 * Created by PC on 2015/9/10.
 */
public class NearServiceImpl extends AbstractService implements NearService {

    @Override
    public List<Near> getList(String city, Double px, Double py, int rows, int offset) {
        List<Near> nearList = super.getMapper(NearMapper.class).getList(city, px, py, rows, offset);

        for (Near near : nearList) {

            switch (near.getType()) {
                case 0://球赛,设置球队信息，及球赛比赛结束时间等信息

                    MatchServiceImpl matchService = new MatchServiceImpl();
                    matchService.setSession(super.getSession());

                    MatchVO matchVO = matchService.get(near.getId());
                    near.setTeamNum(matchVO.getTeam().getPlayerCount());
                    near.setTeamId(matchVO.getTeamId());
                    near.setTeamName(matchVO.getTeam().getName());

                    near.setAcceptTeamNum(matchVO.getAcceptTeam().getPlayerCount());
                    near.setAcceptTeamId(matchVO.getAcceptTeamId());
                    near.setTeamName(matchVO.getAcceptTeam().getName());

                    near.setBeginTime(matchVO.getBeginTime());
                    near.setEndTime(matchVO.getEndTime());
                    near.setDicList(super.getDicValueList("team", "rule", near.getDic()));
                    break;
                case 1://球队，设置球队人数
                    near.setNum(super.getMapper(TeamPlayerMapper.class).getCount(near.getId(), null));
                    near.setDicList(super.getDicValueList("team", "rule", near.getDic()));
                    break;
                case 2://球员，数据字典
                    near.setDicList(super.getDicValueList("player", "role", near.getDic()));
                    break;
                case 3://球场
                    near.setDicList(super.getDicValueList("team", "rule", near.getDic()));
                    break;
            }
        }

        return nearList;
    }
}
