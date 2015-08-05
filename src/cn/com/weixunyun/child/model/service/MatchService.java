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

    //接受挑战
    void acceptMatch(Team team, Match match);

    //修改球赛
    void updateMatch(Team team, Match match);

    //删除球赛，同时需要删除球队AB方
    int deleteMatch(Long id);
}
