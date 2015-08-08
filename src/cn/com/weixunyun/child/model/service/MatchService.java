package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.Match;
import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.dao.MatchMapper;

/**
 * Created by PC on 2015/7/25.
 */
public interface MatchService extends MatchMapper {
    //创建球赛
    void insertMatch(Match match, Team team, String[] playerIds);

    //接受挑战
    void acceptMatch(Match match, Team team, String[] playerIds);

    //修改球赛
    void updateMatch(Match match, Team team);

    //删除球赛，同时需要删除球队AB方
    int deleteMatch(Long id);
}
