package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.GlobalMapper;
import cn.com.weixunyun.child.model.pojo.Global;

import java.util.List;

public class GlobalServiceImpl extends AbstractService implements GlobalService {

    @Override
    public Global select(String codeParent, String code) {
        return super.getMapper(GlobalMapper.class).select(codeParent, code);
    }

    @Override
    public int selectAllCount() {
        return super.getMapper(GlobalMapper.class).selectAllCount();
    }

    @Override
    public List<Global> selectAll() {
        return super.getMapper(GlobalMapper.class).selectAll();
    }

    @Override
    public void insert(Global global) {
        super.getMapper(GlobalMapper.class).insert(global);
    }

    @Override
    public void update(Global global) {
        super.getMapper(GlobalMapper.class).update(global);
    }

    @Override
    public void delete(Global global) {
        super.getMapper(GlobalMapper.class).delete(global);
    }

    @Override
    public List<Global> getList() {
        return super.getMapper(GlobalMapper.class).getList();
    }

}
