package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.Match;
import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.dao.MatchMapper;

/**
 * Created by PC on 2015/7/25.
 */
public interface MatchService extends MatchMapper {
    //创建球赛
    void insertMatch(Team team, Match match);

    //加入球赛应战
    void addMatch(Team team, Match match);
}
