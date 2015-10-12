package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.FavoriteMapper;

/**
 * Created by PC on 2015/7/24.
 */
public interface FavoriteService extends FavoriteMapper {
    void addFavorite(Long[] playerIds, Long courtId);
}
