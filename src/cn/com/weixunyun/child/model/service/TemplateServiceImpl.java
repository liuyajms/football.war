package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.TemplateMapper;
import cn.com.weixunyun.child.model.pojo.Template;

public class TemplateServiceImpl extends AbstractService implements TemplateService {

	@Override
	public Template select(String code, Long schoolId) {
		return super.getMapper(TemplateMapper.class).select(code, schoolId);
	}

	@Override
	public int selectAllCount(Long schoolId) {
		return super.getMapper(TemplateMapper.class).selectAllCount(schoolId);
	}

	@Override
	public List<Template> selectAll(Long offset, Long rows, Long schoolId) {
		return super.getMapper(TemplateMapper.class).selectAll(offset, rows, schoolId);
	}

	@Override
	public void insert(Template template) {
		super.getMapper(TemplateMapper.class).insert(template);
	}

	@Override
	public void update(Template template) {
		super.getMapper(TemplateMapper.class).update(template);
	}

}
