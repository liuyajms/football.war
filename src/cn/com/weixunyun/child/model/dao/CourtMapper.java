package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.bean.Court;
import cn.com.weixunyun.child.model.vo.CourtVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourtMapper {
    void delete(Long id);

    void insert(Court record);

    CourtVO get(Long id);

    List<CourtVO> getList(@Param("city") String city, @Param("rule") Integer rule,
                          @Param("keyword") String keyword,
                          @Param("px") Double px, @Param("py") Double py,
                          @Param("rows") long rows, @Param("offset") long offset);

    void update(Court record);

    void updated(Long id);

}