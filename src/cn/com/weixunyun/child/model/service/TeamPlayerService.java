package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.TeamPlayerMapper;

/**
 * Created by PC on 2015/7/23.
 */
public interface TeamPlayerService extends TeamPlayerMapper{
    void deleteCreatePlayerId(Long teamId, Long playerId);
}
