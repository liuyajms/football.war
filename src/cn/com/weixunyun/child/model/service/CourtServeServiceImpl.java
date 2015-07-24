package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.model.dao.CourtServeMapper;
import cn.com.weixunyun.child.model.vo.CourtServeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by PC on 2015/7/24.
 */
public class CourtServeServiceImpl extends AbstractService implements CourtServeService {
    @Autowired
    private CourtServeMapper mapper;

    @Override
    public void insert(@Param("courtId") Long courtId, @Param("serveId") Long serveId) {
        mapper.insert(courtId, serveId);
    }

    @Override
    public void delete(@Param("courtId") Long courtId, @Param("serveId") Long serveId) {
        mapper.delete(courtId, serveId);
    }

    @Override
    public List<CourtServeVO> getList(@Param("courtId") Long courtId, @Param("keyword") String keyword) {
        return mapper.getList(courtId, keyword);
    }
}
