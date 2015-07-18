package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.bean.TableDictionaryField;
import cn.com.weixunyun.child.model.dao.DictionaryFieldMapper;
import cn.com.weixunyun.child.model.pojo.DictionaryField;

public class DictionaryFieldServiceImpl extends AbstractService implements DictionaryFieldService{

	@Override
	public List<DictionaryField> getAllFields(String code) {
		return super.getMapper(DictionaryFieldMapper.class).getAllFields(code);
	}

	@Override
	public List<TableDictionaryField> list() {
		return super.getMapper(DictionaryFieldMapper.class).list();
	}

	@Override
	public int listCount() {
		return super.getMapper(DictionaryFieldMapper.class).listCount();
	}

}
