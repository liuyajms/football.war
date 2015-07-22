package cn.com.weixunyun.child.model.service;


import cn.com.weixunyun.child.model.bean.Friend;
import cn.com.weixunyun.child.model.dao.FriendMapper;
import cn.com.weixunyun.child.model.vo.FriendVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}
