package cn.com.weixunyun.child.module.weibo;

import cn.com.weixunyun.child.model.service.AbstractService;

public class WeiboFavoritServiceImpl extends AbstractService implements WeiboFavoritService {

	@Override
	public void insert(WeiboFavorit weiboFavorite) {
		super.getMapper(WeiboFavoritMapper.class).insert(weiboFavorite);
	}

	@Override
	public void delete(Long weiboId, Long userId) {
		super.getMapper(WeiboFavoritMapper.class).delete(weiboId, userId);
	}

}
