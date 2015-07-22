package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.DictionaryValueMapper;
import cn.com.weixunyun.child.model.pojo.DictionaryValue;

import java.util.List;

public class DictionaryValueServiceImpl extends AbstractService implements DictionaryValueService {

    @Override
    public List<DictionaryValue> getDicValues(String code, int offset, int rows) {
        return super.getMapper(DictionaryValueMapper.class).getDicValues(code, offset, rows);
    }

    @Override
    public int selectAllCount(String code) {
        return super.getMapper(DictionaryValueMapper.class).selectAllCount(code);
    }

    @Override
    public void insert(DictionaryValue dictionaryValue) {
        super.getMapper(DictionaryValueMapper.class).insert(dictionaryValue);
    }

    @Override
    public void update(DictionaryValue dictionaryValue) {
        super.getMapper(DictionaryValueMapper.class).update(dictionaryValue);
    }

    @Override
    public void delete(DictionaryValue d) {
        super.getMapper(DictionaryValueMapper.class).delete(d);
    }

    @Override
    public List<DictionaryValue> getValueList(String tableCode, String fieldCode) {
        return super.getMapper(DictionaryValueMapper.class).getValueList(tableCode, fieldCode);
    }

    @Override
    public DictionaryValue get(String table, String field, String code) {
        return super.getMapper(DictionaryValueMapper.class).get(table, field, code);
    }

    @Override
    public int getValueListCount(String tableCode, String fieldCode) {
        return super.getMapper(DictionaryValueMapper.class).getValueListCount(tableCode, fieldCode);
    }

    @Override
    public List<DictionaryValue> getList() {
        return super.getMapper(DictionaryValueMapper.class).getList();
    }

}
