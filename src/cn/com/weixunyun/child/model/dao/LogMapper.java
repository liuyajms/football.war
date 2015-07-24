package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.pojo.Log;
import org.apache.ibatis.annotations.Insert;

public interface LogMapper {

    // @SelectProvider(type = LogMapperProvider.class, method = "getAllParents")
    // public List<StudentParents> selectAll(@Param(value = "offset") Long
    // offset, @Param(value = "rows") Long rows,
    // @Param(value = "schoolId") Long schoolId, @Param(value = "studentId")
    // Long studentId,
    // @Param(value = "keyword") String keyword);

    @Insert("insert into log (user_id, time, url, method) values (#{userId}, #{time}, #{url}, #{method})")
    public void insert(Log log);

}
