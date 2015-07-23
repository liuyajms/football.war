package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.model.bean.Court;
import cn.com.weixunyun.child.model.dao.CourtMapper;
import cn.com.weixunyun.child.model.vo.CourtVO;

import java.util.List;

/**
 * Created by PC on 2015/7/24.
 */
public class CourtServiceImpl extends AbstractService implements CourtService {

    @Autowired
    private CourtMapper mapper;

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
}
