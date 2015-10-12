package cn.com.weixunyun.child.model.service;


import cn.com.weixunyun.child.model.bean.Favorite;
import cn.com.weixunyun.child.model.dao.FavoriteMapper;
import cn.com.weixunyun.child.model.vo.CourtVO;

import java.sql.Timestamp;
import java.text.MessageFormat;
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
    public List<CourtVO> getList(Long playerId, String keyword, long rows, long offset) {
        List<CourtVO> list = super.getMapper(FavoriteMapper.class).getList(playerId, keyword, rows, offset);

        for (CourtVO courtVO : list) {
            courtVO.setRuleList(super.getDicValueList("team", "rule", courtVO.getRule()));
        }

        return list;
    }

    @Override
    public int isFavorite(Long playerId, Long favoriteId) {
        return super.getMapper(FavoriteMapper.class).isFavorite(playerId, favoriteId);
    }

    @Override
    public void addFavorite(Long[] playerIds, Long courtId) {

        for (Long playerId : playerIds) {

            if (courtId != null) {
                Favorite favorite = new Favorite();
                favorite.setCourtId(courtId);
                favorite.setPlayerId(playerId);
                favorite.setCreateTime(new Timestamp(System.currentTimeMillis()));

                if (this.isFavorite(playerId, courtId) == 0) {
                    this.insert(favorite);
                } else {
                    System.out.println(MessageFormat.format("已加入该收藏，courtId：{0}，playerId：{1}",
                            courtId, playerId));
                }
            }
        }
    }
}
