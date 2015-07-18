package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.ClientMapper;
import cn.com.weixunyun.child.model.pojo.Client;

import java.util.List;
import java.util.Map;


public class ClientServiceImpl extends AbstractService implements ClientService {
    @Override
    public Client select(String code) {
        return super.getMapper(ClientMapper.class).select(code);
    }

    @Override
    public int count(Map<String, Object> params) {
        return super.getMapper(ClientMapper.class).count(params);
    }

    @Override
    public List<Client> list(Map<String, Object> params) {
        return super.getMapper(ClientMapper.class).list(params);
    }

    @Override
    public List<Client> listAvailable() {
        return super.getMapper(ClientMapper.class).listAvailable();
    }

    @Override
    public void insert(Client record) {
        super.getMapper(ClientMapper.class).insert(record);
    }

    @Override
    public void update(Client record) {
        super.getMapper(ClientMapper.class).update(record);
    }

    @Override
    public void delete(String code) {
        super.getMapper(ClientMapper.class).delete(code);
    }
}
