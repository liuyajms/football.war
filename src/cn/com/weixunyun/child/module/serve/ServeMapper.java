package cn.com.weixunyun.child.module.serve;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServeMapper {
    void delete(Long id);

    void insert(Serve record);

    Serve get(Long id);

    List<Serve> getList(@Param("keyword") String keyword, @Param("rows") long rows, @Param("offset") long offset);

    void update(Serve record);

    void updated(Long id);

}