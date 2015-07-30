package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.dao.TeamMapper;

public interface TeamService extends TeamMapper{
    void insertPlayer(Team team, String[] playerIds);
}
