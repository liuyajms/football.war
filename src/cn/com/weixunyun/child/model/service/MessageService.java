package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.MessageMapper;
import cn.com.weixunyun.child.model.pojo.Message;

public interface MessageService extends MessageMapper {
	
	public void insertData(Message messageFrom, Message messageTo);
}
