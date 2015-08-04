package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.FriendMapper;
import cn.com.weixunyun.child.model.vo.FriendVO;

import java.util.List;
import java.util.Map;

public interface FriendService extends FriendMapper{
    Map<String,List<? extends Object>> getFormatAllList(Long playerId, Long teamId, String keyword);
}
