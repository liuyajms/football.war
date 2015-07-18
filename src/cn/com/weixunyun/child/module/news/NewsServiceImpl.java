package cn.com.weixunyun.child.module.news;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;
import cn.com.weixunyun.child.module.download.Download;
import cn.com.weixunyun.child.module.download.DownloadMapper;

public class NewsServiceImpl extends AbstractService implements NewsService {

	@Override
	public News select(Long id) {
		return super.getMapper(NewsMapper.class).select(id);
	}

	@Override
	public int queryListCount(Long schoolId, String keyword, Long type, Boolean pic) {
		return super.getMapper(NewsMapper.class).queryListCount(schoolId, keyword, type, pic);
	}

	@Override
	public void insert(News news) {
		super.getMapper(NewsMapper.class).insert(news);
	}

	@Override
	public void update(News news) {
		super.getMapper(NewsMapper.class).update(news);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(NewsMapper.class).delete(id);
	}

	@Override
	public List<News> queryList(Long schoolId, Long offset, Long rows, String keyword, Long type, Boolean pic) {
		return super.getMapper(NewsMapper.class).queryList(schoolId, offset, rows, keyword, type, pic);
	}

	@Override
	public void updateImage(@Param("id") Long id, @Param("image") boolean image) {
		super.getMapper(NewsMapper.class).updateImage(id, image);
	}

	@Override
	public void deleteNews(Long type) {
		super.getMapper(NewsMapper.class).deleteNews(type);
	}

	@Override
	public int insertNews(Long schoolId, Long type, Long userId, int del, List<Map<String, Object>> list) {
		NewsMapper mapper = super.getMapper(NewsMapper.class);
		if (del == 1) {
			mapper.deleteNews(type);
		}
		
		News news = new News();
		for (Map<String, Object> m : list) {
			news.setId(super.getMapper(SequenceMapper.class).sequence());
			news.setSchoolId(schoolId);
			news.setCreateTeacherId(userId);
			news.setCreateTime(new Timestamp(System.currentTimeMillis()));
			news.setUpdateTeacherId(userId);
			news.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			news.setComment(false);
			news.setPic(false);
			news.setUp(false);
			news.setType(type);
			news.setTitle(m.get("title").toString());
			news.setDescriptionSummary(m.get("descriptionSummary")==null?null:m.get("descriptionSummary").toString());
			mapper.insert(news);
		}
		return list.size();
	}

}
