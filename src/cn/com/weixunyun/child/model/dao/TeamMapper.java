package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.vo.TeamVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeamMapper {
    void delete(Long id);

    void insert(Team record);

    Team select(Long id);

    TeamVO get(Long id);

    List<TeamVO> getList(@Param("city") String city, @Param("rule") Integer rule,
                         @Param("beginAge") Integer beginAge, @Param("endAge") Integer endAge,
                         @Param("keyword") String keyword, @Param("srcTeamId") Long srcTeamId,
                         @Param("px") Double px, @Param("py") Double py,
                         @Param("rows") long rows, @Param("offset") long offset);

    List<TeamVO> getAllList(@Param("tmp") Boolean tmp, @Param("rows") int rows, @Param("offset") int offset);

    void update(Team record);

    void updated(Long id);

    List<TeamVO> getPlayerTeamList(@Param("playerId") Long playerId, @Param("rows") int rows, @Param("offset") int offset);
}