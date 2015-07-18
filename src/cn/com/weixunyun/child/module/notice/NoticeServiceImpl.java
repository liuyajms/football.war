package cn.com.weixunyun.child.module.notice;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class NoticeServiceImpl extends AbstractService implements NoticeService {

	@Override
	public TeacherNotice get(Long id) {
		return super.getMapper(NoticeMapper.class).get(id);
	}

	@Override
	public void insert(Notice Notice) {
		super.getMapper(NoticeMapper.class).insert(Notice);
	}

	@Override
	public void update(Notice Notice) {
		super.getMapper(NoticeMapper.class).update(Notice);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(NoticeMapper.class).delete(id);
	}

	@Override
	public List<TeacherNotice> getList(@Param("offset") Long offset, @Param("rows") Long rows,
			@Param("schoolId") Long schoolId, @Param("teacher") boolean teacher, @Param("parents") boolean parents,
			String keyword) {
		return super.getMapper(NoticeMapper.class).getList(offset, rows, schoolId, teacher, parents, keyword);
	}

	@Override
	public int getListCount(Long schoolId, @Param("teacher") boolean teacher, @Param("parents") boolean parents,
			String keyword) {
		return super.getMapper(NoticeMapper.class).getListCount(schoolId, teacher, parents, keyword);
	}

	@Override
	public void updateImage(Long id, Long userId, int pic) {
		super.getMapper(NoticeMapper.class).updateImage(id, userId, pic);
	}

	@Override
	public void deleteNotices(Long schoolId) {
		super.getMapper(NoticeMapper.class).deleteNotices(schoolId);
	}

	@Override
	public int insertNotices(Long schoolId, Long userId, int del, List<Map<String, Object>> list) {
		NoticeMapper mapper = super.getMapper(NoticeMapper.class);
		if (del == 1) {
			mapper.deleteNotices(schoolId);
		}
		
		Notice notice = new Notice();
		for (Map<String, Object> m : list) {
			notice.setId(super.getMapper(SequenceMapper.class).sequence());
			notice.setSchoolId(schoolId);
			notice.setCreateUserId(userId);
			notice.setCreateTime(new Timestamp(System.currentTimeMillis()));
			notice.setCreateUserId(userId);
			notice.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			notice.setDescription(m.get("description")==null?null:m.get("description").toString());
			notice.setPushParents(m.get("pushParents") == null ? null : (Boolean) m.get("pushParents"));
			notice.setPushTeacher(m.get("pushTeacher") == null ? null : (Boolean) m.get("pushTeacher"));
			mapper.insert(notice);
		}
		return list.size();
	}

}
