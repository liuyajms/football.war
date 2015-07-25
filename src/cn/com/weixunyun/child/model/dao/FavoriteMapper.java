package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.bean.Favorite;
import cn.com.weixunyun.child.model.vo.CourtVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoriteMapper {
    void delete(@Param("playerId") Long playerId, @Param("courtId") Long courtId);

    void insert(Favorite favorite);

    List<CourtVO> getList(@Param("playerId") Long playerId, @Param("keyword") String keyword,
                          @Param("rows") long rows, @Param("offset") long offset);

    int isFavorite(@Param("playerId") Long playerId, @Param("courtId") Long courtId);
}