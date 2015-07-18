package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.DictionaryValueMapper;
import cn.com.weixunyun.child.model.pojo.DictionaryValue;

public class DictionaryValueServiceImpl extends AbstractService implements DictionaryValueService{

	@Override
	public List<DictionaryValue> getDicValues(Long schoolId, String code, int offset, int rows) {
		return super.getMapper(DictionaryValueMapper.class).getDicValues(schoolId, code, offset, rows);
	}

	@Override
	public int selectAllCount(Long schoolId, String code) {
		return super.getMapper(DictionaryValueMapper.class).selectAllCount(schoolId, code);
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
	public List<DictionaryValue> getValueList(Long schoolId, String tableCode, String fieldCode) {
		return super.getMapper(DictionaryValueMapper.class).getValueList(schoolId, tableCode, fieldCode);
	}

	@Override
	public DictionaryValue get(Long schoolId, String table, String field, String code) {
		return super.getMapper(DictionaryValueMapper.class).get(schoolId, table, field, code);
	}

	@Override
	public int getValueListCount(Long schoolId, String tableCode, String fieldCode) {
		return super.getMapper(DictionaryValueMapper.class).getValueListCount(schoolId, tableCode, fieldCode);
	}

	@Override
	public List<DictionaryValue> getList(Long schoolId) {
		return super.getMapper(DictionaryValueMapper.class).getList(schoolId);
	}

}
