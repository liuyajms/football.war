package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.DictionaryTableMapper;
import cn.com.weixunyun.child.model.pojo.DictionaryTable;

public class DictionaryTableServiceImpl extends AbstractService implements DictionaryTableService{

	@Override
	public List<DictionaryTable> getDicTables() {
		return super.getMapper(DictionaryTableMapper.class).getDicTables();
	}

	@Override
	public int getDicTablesCount() {
		return super.getMapper(DictionaryTableMapper.class).getDicTablesCount();
	}

	@Override
	public DictionaryTable get(String code) {
		return super.getMapper(DictionaryTableMapper.class).get(code);
	}

}
