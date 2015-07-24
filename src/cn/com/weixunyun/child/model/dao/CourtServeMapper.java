package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.vo.CourtServeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by PC on 2015/7/24.
 */
public interface CourtServeMapper {

    void insert(@Param("courtId") Long courtId, @Param("serveId") Long serveId);

    void delete(@Param("courtId") Long courtId, @Param("serveId") Long serveId);

    List<CourtServeVO> getList(@Param("courtId") Long courtId, @Param("keyword") String keyword);

}
