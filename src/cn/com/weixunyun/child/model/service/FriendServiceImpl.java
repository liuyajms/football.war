package cn.com.weixunyun.child.model.service;


import cn.com.weixunyun.child.model.bean.Friend;
import cn.com.weixunyun.child.model.dao.FriendMapper;
import cn.com.weixunyun.child.model.vo.FriendVO;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendServiceImpl extends AbstractService implements FriendService {
    @Override
    public void delete(Long playerId, Long friendId) {
        super.getMapper(FriendMapper.class).delete(playerId, friendId);
    }

    @Override
    public void insert(Friend friend) {
        super.getMapper(FriendMapper.class).insert(friend);
    }

    @Override
    public List<FriendVO> getList(@Param("playerId") Long playerId, @Param("keyword") String keyword,
                                  @Param("rows") long rows, @Param("offset") long offset) {
        return super.getMapper(FriendMapper.class).getList(playerId, keyword, rows, offset);
    }

    @Override
    public int isFriend(@Param("playerId") Long playerId, @Param("friendId") Long friendId) {
        return super.getMapper(FriendMapper.class).isFriend(playerId, friendId);
    }

    @Override
    public List<FriendVO> getTeamPlayerList(Long playerId, Long teamId, String keyword) {
        return super.getMapper(FriendMapper.class).getTeamPlayerList(playerId, teamId, keyword);
    }

    @Override
    public Map<String, List<? extends Object>> getFormatAllList(Long playerId, Long teamId, String keyword) {
        Map<String, List<? extends Object>> resultMap = new HashMap<>();
        //查询好友
        resultMap.put("friendList", getList(playerId, keyword, 0, 0));

        //查询球队球友列表
        List<FriendVO> list = getTeamPlayerList(playerId, teamId, keyword);

        //组装数据

        teamId = null;
        List<List<FriendVO>> teamList = new ArrayList<>();
        List<FriendVO> playerList = new ArrayList<>();
        for (FriendVO friendVO : list) {
            if (teamId == null) {
                teamId = friendVO.getTeamId();
                playerList.add(friendVO);
            } else if (teamId.equals(friendVO.getTeamId())) {
                playerList.add(friendVO);
            } else {
                //将同一个球队的球员加入teamList
                teamList.add(playerList);
                //清空，进入下一个球队
                playerList = new ArrayList<>();
                teamId = null;
            }
        }
        //加入最后一个球队
        teamList.add(playerList);

        resultMap.put("teamList", teamList);

        return resultMap;
    }
}
