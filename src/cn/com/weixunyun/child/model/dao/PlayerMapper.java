package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.bean.Player;
import cn.com.weixunyun.child.model.vo.PlayerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayerMapper {
    void delete(Long id);

    void insert(Player record);

    PlayerVO get(Long id);

    List<Player> getPlayerList(@Param("rows") int rows, @Param("offset") int offset);

    List<PlayerVO> getList(@Param("loginId") Long loginId, @Param("city") String city, @Param("role") Integer role,
                           @Param("beginAge") Integer beginAge, @Param("endAge") Integer endAge,
                           @Param("keyword") String keyword,
                           @Param("px") Double px, @Param("py") Double py,
                           @Param("rows") long rows, @Param("offset") long offset);

    void update(Player record);

    void updated(Long id);

    PlayerVO login(@Param("username") String username, @Param("password") String password);

    int updateInfo(@Param("id") Long id, @Param("password") String password);

    int findPassword(@Param("mobile") String mobile, @Param("password") String password);
}