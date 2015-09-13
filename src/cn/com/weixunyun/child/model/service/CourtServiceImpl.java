package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.model.bean.Court;
import cn.com.weixunyun.child.model.bean.Favorite;
import cn.com.weixunyun.child.model.dao.CourtMapper;
import cn.com.weixunyun.child.model.dao.CourtServeMapper;
import cn.com.weixunyun.child.model.dao.FavoriteMapper;
import cn.com.weixunyun.child.model.vo.CourtVO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 2015/7/24.
 */
public class CourtServiceImpl extends AbstractService implements CourtService {

    @Autowired
    private CourtMapper mapper;

    @Autowired
    private CourtServeMapper serveMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public void delete(Long id) {
        mapper.delete(id);
    }

    @Override
    public void insert(Court record) {
        mapper.insert(record);

        //加入我的球队收藏
        Favorite f = new Favorite();
        f.setPlayerId(record.getCreatePlayerId());
        f.setCourtId(record.getId());
        f.setCreateTime(new Timestamp(System.currentTimeMillis()));

        favoriteMapper.insert(f);
    }

    @Override
    public CourtVO get(Long id) {
        CourtVO courtVO = mapper.get(id);

        courtVO.setRuleList(super.getDicValueList("team", "rule", courtVO.getRule()));
        courtVO.setCourtServeList(super.getMapper(CourtServeMapper.class).getList(id, null));

        return courtVO;
    }

    @Override
    public List<CourtVO> getList(String city, Integer rule, String keyword, Double px, Double py, long rows, long offset) {
        List<CourtVO> list = mapper.getList(city, rule, keyword, px, py, rows, offset);

        for (CourtVO courtVO : list) {
            courtVO.setRuleList(super.getDicValueList("team", "rule", courtVO.getRule()));
        }

        return list;
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
        this.insert(court);
        serveMapper.delete(court.getId(), null);
        for (String serveId : serveIds) {
            serveMapper.insert(court.getId(), Long.parseLong(serveId));
        }
    }

    @Override
    public void updateServe(Court court, String[] serveIds) {
        mapper.update(court);
        //更新服务信息关联表
        serveMapper.delete(court.getId(), null);
        for (String serveId : serveIds) {
            serveMapper.insert(court.getId(), Long.parseLong(serveId));
        }
    }

    @Override
    public int insertMulti(int del, List<Map<String, Object>> list) {
        //删除所有数据
//        if (del == 1) {
//            mapper.deleteAll(params);
//        }
        //插入数据
        for (Map<String, Object> dataMap : list) {
            Court court = super.buildEntity(Court.class, dataMap);
            mapper.insert(court);
        }

        return list.size();
    }
}
