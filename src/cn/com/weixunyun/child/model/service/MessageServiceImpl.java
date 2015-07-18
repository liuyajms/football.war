package cn.com.weixunyun.child.model.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.com.weixunyun.child.model.bean.TeacherParentsMessage;
import cn.com.weixunyun.child.model.dao.MessageMapper;
import cn.com.weixunyun.child.model.pojo.Message;

public class MessageServiceImpl extends AbstractService implements MessageService {

	@Override
	public int queryListCount(Long id0, Long id1) {

		return super.getMapper(MessageMapper.class).queryListCount(id0, id1);
	}

	@Override
	public List<TeacherParentsMessage> queryList(int offset, int rows, Long id0, Long id1) {
		return super.getMapper(MessageMapper.class).queryList(offset, rows, id0, id1);
	}

	@Override
	public void insert(Message message) {

		super.getMapper(MessageMapper.class).insert(message);
	}

	@Override
	public void update(Message message, Long userId) {

		super.getMapper(MessageMapper.class).update(message, userId);
	}

	@Override
	public void delete(Long id) {

		super.getMapper(MessageMapper.class).delete(id);
	}

	@Override
	public void insertData(Message messageFrom, Message messageTo) {

		insert(messageFrom);
		insert(messageTo);
	}

	@Override
	public List<Map<String, ?>> getSessionList(@Param("id") Long id) {
		return super.getMapper(MessageMapper.class).getSessionList(id);
	}

	@Override
	public int unreaded(@Param("id") Long id) {
		return super.getMapper(MessageMapper.class).unreaded(id);
	}

}
