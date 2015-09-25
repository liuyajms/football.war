package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.LogMapper;
import cn.com.weixunyun.child.model.pojo.Log;

import java.util.List;

public class LogServiceImpl extends AbstractService implements LogService {

    @Override
    public void insert(Log log) {
        super.getMapper(LogMapper.class).insert(log);
    }

    @Override
    public void insertBatch(List<Log> logList) {
        super.getMapper(LogMapper.class).insertBatch(logList);
    }

}
