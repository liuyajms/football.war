package cn.com.weixunyun.child.util;

import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import cn.com.weixunyun.child.PropertiesListener;

public class SimiyunProducer {

	private static Session session;
	private static MessageProducer producer;
	static {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					PropertiesListener.getProperty("simiyun.jms.username"),
					PropertiesListener.getProperty("simiyun.jms.password"), PropertiesListener.getProperty("simiyun.jms.url"));
			Connection connection = null;
			connection = connectionFactory.createConnection();
			connection.start();

			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

			Destination destination = session.createQueue(PropertiesListener.getProperty("simiyun.jms.destination"));

			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 添加用户：教师
	 */
	public static boolean createSimiyunUser(String url, Map<String, Object> map) {
		if (session == null) {
			return false;
		}
		try {
			MapMessage message = session.createMapMessage();
			message.setString("tag", "simiyun");
			message.setString("url", url);
			/*message.setString("username", username);
			message.setString("password", password);
			message.setString("nick", nick);
			message.setString("module", module);*/
			message.setObject("map", map);

			producer.send(message);

			session.commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 添加群组
	 */
	public static boolean createSimiyunGroup(String url, Map<String, Object> map) {
		if (session == null) {
			return false;
		}
		try {
			MapMessage message = session.createMapMessage();
			message.setString("tag", "simiyun");
			message.setString("url", url);
			//message.setString("module", module);
			message.setObject("map", map);

			producer.send(message);

			session.commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 添加群组成员
	 */
	public static boolean createSimiyunGroupUsers(String url, String group_name, String user_name, String type, 
			String user_id, String role_id, String module, Map<String, Object> map) {
		if (session == null) {
			return false;
		}
		try {
			MapMessage message = session.createMapMessage();
			message.setString("tag", "simiyun");
			message.setString("url", url);
			message.setString("group_name", group_name);
			message.setString("user_name", user_name);
			message.setString("type", type);
			message.setString("user_id", user_id);
			message.setString("role_id", role_id);
			message.setString("module", module);
			message.setObject("map", map);

			producer.send(message);

			session.commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean sendSchoolNotification(Long schoolId, String title, String description, String module,
			Map<String, Object> map) {
		return sendSchoolNotification(schoolId, null, title, description, module, map);
	}

	/**
	 * 发送学校通知
	 */
	public static boolean sendSchoolNotification(Long schoolId, Integer type, String title, String description,
			String module, Map<String, Object> map) {
		if (session == null) {
			return false;
		}
		String tag = null;
		if (type == null) {
			tag = "child_school_" + schoolId;
		} else {
			switch (type) {
			case 0:
				tag = "child_school_" + schoolId + "_teacher";
				break;
			case 1:
				tag = "child_school_" + schoolId + "_parents";
				break;
			default:
			}
		}

		System.out.println("sending message...." + tag);
		try {
			MapMessage message = session.createMapMessage();
			message.setString("tag", tag);
			message.setString("title", title);
			message.setString("description", description);
			message.setString("module", module);
			message.setObject("map", map);

			producer.send(message);

			session.commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean sendClassesNotification(Long classesId, String title, String description, String module,
			Map<String, Object> map) {
		return sendClassesNotification(classesId, null, title, description, module, map);
	}

	/**
	 * 发送班级通知
	 */
	public static boolean sendClassesNotification(Long classesId, Integer type, String title, String description,
			String module, Map<String, Object> map) {
		if (session == null) {
			return false;
		}
		String tag = null;
		if (type == null) {
			tag = "child_classes_" + classesId;
		} else {
			switch (type) {
			case 0:
				tag = "child_classes_" + classesId + "_teacher";
				break;
			case 1:
				tag = "child_classes_" + classesId + "_parents";
				break;
			default:
			}
		}
		try {
			MapMessage message = session.createMapMessage();
			message.setString("tag", tag);
			message.setString("title", title);
			message.setString("description", description);
			message.setString("module", module);
			message.setObject("map", map);

			producer.send(message);

			session.commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 发送个人消息
	 */
	public static boolean sendPersonalNotification(Long id, String title, String description, String module,
			Map<String, Object> map) {
		if (session == null) {
			return false;
		}
		try {
			MapMessage message = session.createMapMessage();
			message.setString("tag", "child_" + id);
			message.setString("title", title);
			message.setString("description", description);
			message.setString("module", module);
			message.setObject("map", map);

			producer.send(message);

			session.commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
