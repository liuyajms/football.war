package cn.com.weixunyun.child.module.serve;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.model.service.AbstractService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by PC on 2015/7/24.
 */
public class ServeServiceImpl extends AbstractService implements ServeService {

    @Autowired
    private ServeMapper mapper;

    @Override
    public void delete(Long id) {
        mapper.delete(id);
    }

    @Override
    public void insert(Serve record) {
        mapper.insert(record);
    }

    @Override
    public Serve get(Long id) {
        return mapper.get(id);
    }

    @Override
    public List<Serve> getList(@Param("keyword") String keyword, @Param("rows") long rows, @Param("offset") long offset) {
        return mapper.getList(keyword, rows, offset);
    }

    @Override
    public void update(Serve record) {
        mapper.update(record);
    }

    @Override
    public void updated(Long id) {
        mapper.updated(id);
    }
}
