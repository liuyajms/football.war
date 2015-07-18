package cn.com.weixunyun.child.module.personal.photo;

import cn.com.weixunyun.child.model.service.AbstractService;
import cn.com.weixunyun.child.module.personal.photo.Photo;
import cn.com.weixunyun.child.module.personal.photo.PhotoMapper;
import cn.com.weixunyun.child.module.personal.photo.PhotoService;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public class PhotoServiceImpl extends AbstractService implements PhotoService {

	@Override
	public Photo select(Long id) {
		return super.getMapper(PhotoMapper.class).select(id);
	}

	@Override
	public int getListCount(@Param(value = "userId") Long userId, @Param(value = "keyword") String keyword) {
		return super.getMapper(PhotoMapper.class).getListCount(userId, keyword);
	}

	@Override
	public List<UserPhoto> getList(@Param(value = "offset") int offset, @Param(value = "rows") int rows,
			@Param(value = "userId") Long userId, @Param(value = "keyword") String keyword) {
		return super.getMapper(PhotoMapper.class).getList(offset, rows, userId, keyword);
	}

	@Override
	public void insert(Photo photo) {
		super.getMapper(PhotoMapper.class).insert(photo);
	}

	@Override
	public void update(Photo photo) {
		super.getMapper(PhotoMapper.class).update(photo);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(PhotoMapper.class).delete(id);
	}

	@Override
	public void updateImage(Long id, Long userId) {
		super.getMapper(PhotoMapper.class).updateImage(id, userId);
	}

}
