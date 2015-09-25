package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.pojo.Log;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface LogMapper {

    @Insert("insert into log (user_id, time, url, method) values (#{userId}, #{time}, #{url}, #{method})")
    public void insert(Log log);

    public void insertBatch(List<Log> logList);

}
