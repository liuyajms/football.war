package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.vo.Near;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by PC on 2015/9/10.
 */
public interface NearMapper {

    public List<Near> getList(@Param("city") String city,
                              @Param("px") Double px, @Param("py") Double py,
                              @Param("rows") int rows, @Param("offset") int offset);
}
