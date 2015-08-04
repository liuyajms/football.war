package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.bean.Friend;
import cn.com.weixunyun.child.model.vo.FriendVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FriendMapper {
    void delete(@Param("playerId") Long playerId, @Param("friendId") Long friendId);

    void insert(Friend friend);

    List<FriendVO> getList(@Param("playerId") Long playerId, @Param("keyword") String keyword,
                           @Param("rows") long rows, @Param("offset") long offset);

    int isFriend(@Param("playerId") Long playerId, @Param("friendId") Long friendId);

    List<FriendVO> getTeamPlayerList(@Param("playerId") Long playerId, @Param("teamId") Long teamId,
                                     @Param("keyword") String keyword);
}