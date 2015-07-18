package cn.com.weixunyun.child.model.service;

import org.apache.ibatis.session.SqlSession;

public abstract class AbstractService {

	private SqlSession session;

	public SqlSession getSession() {
		return session;
	}

	public void setSession(SqlSession session) {
		this.session = session;
	}

	public <T> T getMapper(Class<T> cls) {
		return session.getMapper(cls);
	}

}
