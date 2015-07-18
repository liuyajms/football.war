package cn.com.weixunyun.child.module.weibo;

import java.util.List;

import cn.com.weixunyun.child.model.service.AbstractService;

public class WeiboCommentServiceImpl extends AbstractService implements WeiboCommentService {

	@Override
	public void delete(Long id) {
		super.getMapper(WeiboCommentMapper.class).delete(id);
	}

	@Override
	public void insert(WeiboComment WeiboComments) {
		super.getMapper(WeiboCommentMapper.class).insert(WeiboComments);
	}

	@Override
	public List<UserWeiboComment> queryList(Long weiboId) {
		return super.getMapper(WeiboCommentMapper.class).queryList(weiboId);
	}

	@Override
	public int queryListCount(Long weiboId) {
		return super.getMapper(WeiboCommentMapper.class).queryListCount(weiboId);
	}

	@Override
	public List<UserWeiboComment> queryComments(Long offset, Long rows, Long weiboId, String keyword) {
		return super.getMapper(WeiboCommentMapper.class).queryComments(offset, rows, weiboId, keyword);
	}

	@Override
	public int queryCommentsCount(Long weiboId, String keyword) {
		return super.getMapper(WeiboCommentMapper.class).queryCommentsCount(weiboId, keyword);
	}

	@Override
	public void update(WeiboComment weiboComment) {
		super.getMapper(WeiboCommentMapper.class).update(weiboComment);

	}

	@Override
	public WeiboComment select(Long id) {
		return super.getMapper(WeiboCommentMapper.class).select(id);

	}

}
