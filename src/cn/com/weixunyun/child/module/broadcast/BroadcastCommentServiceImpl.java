package cn.com.weixunyun.child.module.broadcast;

import java.util.List;

import cn.com.weixunyun.child.model.bean.UserBroadcastComment;
import cn.com.weixunyun.child.model.service.AbstractService;

public class BroadcastCommentServiceImpl extends AbstractService implements BroadcastCommentService {

	@Override
	public void delete(Long id) {
		super.getMapper(BroadcastCommentMapper.class).delete(id);
	}

	@Override
	public int getCommentsCount(Long id) {
		return super.getMapper(BroadcastCommentMapper.class).getCommentsCount(id);
	}

	@Override
	public void insert(BroadcastComment comment) {
		super.getMapper(BroadcastCommentMapper.class).insert(comment);
	}

	@Override
	public List<UserBroadcastComment> getAllBroadCastComments(Long offset, Long rows, Long broadcastId, String keyword) {
		return super.getMapper(BroadcastCommentMapper.class)
				.getAllBroadCastComments(offset, rows, broadcastId, keyword);
	}

	@Override
	public int getAllBroadCastCommentsCount(Long broadcastId, String keyword) {

		return super.getMapper(BroadcastCommentMapper.class).getAllBroadCastCommentsCount(broadcastId, keyword);
	}

	@Override
	public BroadcastComment select(Long id) {
		return super.getMapper(BroadcastCommentMapper.class).select(id);
	}

	@Override
	public void update(BroadcastComment comment) {
		super.getMapper(BroadcastCommentMapper.class).update(comment);

	}

}
