package cn.com.weixunyun.child.model.service;


import cn.com.weixunyun.child.model.bean.Favorite;
import cn.com.weixunyun.child.model.dao.FavoriteMapper;
import cn.com.weixunyun.child.model.vo.CourtVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public class FavoriteServiceImpl extends AbstractService implements FavoriteService {
    @Override
    public void delete(Long playerId, Long favoriteId) {
        super.getMapper(FavoriteMapper.class).delete(playerId, favoriteId);
    }

    @Override
    public void insert(Favorite favorite) {
        super.getMapper(FavoriteMapper.class).insert(favorite);
    }

    @Override
    public List<CourtVO> getList(@Param("playerId") Long playerId, @Param("keyword") String keyword,
                                 @Param("rows") long rows, @Param("offset") long offset) {
        List<CourtVO> list = super.getMapper(FavoriteMapper.class).getList(playerId, keyword, rows, offset);

        for (CourtVO courtVO : list) {
            courtVO.setRuleList(super.getDicValueList("team", "rule", courtVO.getRule()));
        }

        return list;
    }

    @Override
    public int isFavorite(@Param("playerId") Long playerId, @Param("favoriteId") Long favoriteId) {
        return super.getMapper(FavoriteMapper.class).isFavorite(playerId, favoriteId);
    }
}
