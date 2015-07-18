package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.bean.TeacherParentsChat;
import cn.com.weixunyun.child.model.dao.ChatMapper;
import cn.com.weixunyun.child.model.pojo.Chat;

public class ChatServiceImpl extends AbstractService implements ChatService {

	@Override
	public List<TeacherParentsChat> getList(Long offset, Long rows, Long schoolId, Long classesId, String time) {
		return super.getMapper(ChatMapper.class).getList(offset, rows, schoolId, classesId, time);
	}

	@Override
	public void insert(Chat chat) {
		super.getMapper(ChatMapper.class).insert(chat);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(ChatMapper.class).delete(id);
	}

	@Override
	public int getListCount(Long schoolId, Long classesId, String time) {
		return super.getMapper(ChatMapper.class).getListCount(schoolId, classesId, time);
	}

	@Override
	public int unreaded(Long schoolId, Long classesId, Long userId, String time) {
		return super.getMapper(ChatMapper.class).unreaded(schoolId, classesId, userId, time);
	}

}
