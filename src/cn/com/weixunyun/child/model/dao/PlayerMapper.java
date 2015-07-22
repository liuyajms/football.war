package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.bean.Player;
import cn.com.weixunyun.child.model.vo.PlayerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayerMapper {
    void delete(Long id);

    void insert(Player record);

    PlayerVO get(Long id);

    List<PlayerVO> getList(@Param("city") String city, @Param("role") Integer role,
                           @Param("beginAge") Integer beginAge, @Param("endAge") Integer endAge,
                           @Param("keyword") String keyword, @Param("rows") long rows, @Param("offset") long offset);

    void update(Player record);

    void updated(Long id);

    PlayerVO login(@Param("username") String username, @Param("password") String password);

    int updateInfo(@Param("id") Long id, @Param("password") String password);

}