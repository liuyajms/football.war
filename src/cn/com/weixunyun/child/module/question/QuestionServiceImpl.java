package cn.com.weixunyun.child.module.question;

import cn.com.weixunyun.child.model.service.AbstractService;

import java.util.List;
import java.util.Map;


public class QuestionServiceImpl extends AbstractService implements QuestionService {
    @Override
    public Question select(Long id) {
        return super.getMapper(QuestionMapper.class).select(id);
    }

    @Override
    public int count(Map<String, Object> params) {
        return super.getMapper(QuestionMapper.class).count(params);
    }

    @Override
    public List<Question> list(Map<String, Object> params) {
        return super.getMapper(QuestionMapper.class).list(params);
    }

    @Override
    public void insert(Question record) {
        super.getMapper(QuestionMapper.class).insert(record);
    }

    @Override
    public void update(Question record) {
        super.getMapper(QuestionMapper.class).update(record);
    }

    @Override
    public void delete(Long id) {
        super.getMapper(QuestionMapper.class).delete(id);
    }

	@Override
	public void answer(Question record) {
		super.getMapper(QuestionMapper.class).answer(record);
		
	}

}
