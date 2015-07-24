package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.model.bean.Court;
import cn.com.weixunyun.child.model.dao.CourtMapper;
import cn.com.weixunyun.child.model.dao.CourtServeMapper;
import cn.com.weixunyun.child.model.vo.CourtVO;

import java.util.List;

/**
 * Created by PC on 2015/7/24.
 */
public class CourtServiceImpl extends AbstractService implements CourtService {

    @Autowired
    private CourtMapper mapper;

    @Autowired
    private CourtServeMapper serveMapper;

    @Override
    public void delete(Long id) {
        mapper.delete(id);
    }

    @Override
    public void insert(Court record) {
        mapper.insert(record);
    }

    @Override
    public CourtVO get(Long id) {
        CourtVO courtVO = mapper.get(id);

        courtVO.setRuleList(super.getDicValueList("team", "rule", courtVO.getRule()));
        courtVO.setCourtServeList(super.getMapper(CourtServeMapper.class).getList(id, null));

        return mapper.get(id);
    }

    @Override
    public List<CourtVO> getList(String city, Integer rule, String keyword, long rows, long offset) {
        return mapper.getList(city, rule, keyword, rows, offset);
    }

    @Override
    public void update(Court record) {
        mapper.update(record);
    }

    @Override
    public void updated(Long id) {
        mapper.updated(id);
    }


    @Override
    public void insertServe(Court court, String[] serveIds) {
        mapper.insert(court);
        serveMapper.delete(court.getId(), null);
        for (String serveId : serveIds) {
            serveMapper.insert(court.getId(), Long.parseLong(serveId));
        }
    }

    @Override
    public void updateServe(Court court, String[] serveIds) {
        mapper.update(court);
        serveMapper.delete(court.getId(), null);
        for (String serveId : serveIds) {
            serveMapper.insert(court.getId(), Long.parseLong(serveId));
        }
    }
}
