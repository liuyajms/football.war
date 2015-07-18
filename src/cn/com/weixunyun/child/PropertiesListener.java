package cn.com.weixunyun.child;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class PropertiesListener implements ServletContextListener {

	private static Properties properties;

	public static String getProperty(String key) {
		return properties.getProperty(key, null);
	}

	public static String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			properties = new Properties();
			properties.load(event.getServletContext().getResourceAsStream("/WEB-INF/application.properties"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		properties.clear();
		properties = null;
	}

}
