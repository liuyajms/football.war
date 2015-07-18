package cn.com.weixunyun.child.model.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class ServiceFactory {

	@SuppressWarnings("unchecked")
	public static <T> T getService(Class<T> cls) {
		try {
			return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[] { cls }, new ServiceInvocationHandler(
					Class.forName(cls.getName() + "Impl").newInstance()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static SqlSessionFactory getSession(){
		return ServiceInvocationHandler.sessionFactory;
	}
	
	static class ServiceInvocationHandler implements InvocationHandler {

		private static SqlSessionFactory sessionFactory = null;

		static {
			try {
				sessionFactory = new SqlSessionFactoryBuilder().build(Resources
						.getResourceAsStream("mybatis_config.xml"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private Object obj;

		public ServiceInvocationHandler(Object obj) {
			this.obj = obj;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			SqlSession session = null;
			try {
				session = sessionFactory.openSession();
				obj.getClass().getMethod("setSession", SqlSession.class).invoke(obj, session);
				Object result = method.invoke(obj, args);
				session.commit();
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				session.rollback();
				throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				session.close();
			}
		}

	}
}
