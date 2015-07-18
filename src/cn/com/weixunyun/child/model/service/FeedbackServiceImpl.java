package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.FeedbackMapper;
import cn.com.weixunyun.child.model.pojo.Feedback;

public class FeedbackServiceImpl extends AbstractService implements FeedbackService {

	@Override
	public void insert(Feedback feedback) {
		super.getMapper(FeedbackMapper.class).insert(feedback);
	}

	@Override
	public Feedback select(Long id) {
		return super.getMapper(FeedbackMapper.class).select(id);
	}

	@Override
	public int selectAllCount() {
		return super.getMapper(FeedbackMapper.class).selectAllCount();
	}

	@Override
	public List<Feedback> selectAll(Long offset, Long rows) {
		return super.getMapper(FeedbackMapper.class).selectAll(offset, rows);
	}

	@Override
	public void update(Feedback feedback) {
		super.getMapper(FeedbackMapper.class).update(feedback);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(FeedbackMapper.class).delete(id);
	}

}
