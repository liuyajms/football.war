package cn.com.weixunyun.child.util;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import cn.com.weixunyun.child.PropertiesListener;

public class PushProducer {

	private static Session session;
	private static Map<String, MessageProducer> destinationMap = new HashMap<String, MessageProducer>();
	static {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					PropertiesListener.getProperty("push.jms.username"),
					PropertiesListener.getProperty("push.jms.password"), PropertiesListener.getProperty("push.jms.url"));
			Connection connection = null;
			connection = connectionFactory.createConnection();
			connection.start();

			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static MessageProducer getMessageProducer(Long schoolId) {
		String destinationName = null;
		if (schoolId == null) {
			destinationName = "child";
		} else {
			destinationName = "child." + schoolId;
		}
		MessageProducer messageProducer = destinationMap.get(destinationName);
		if (messageProducer == null) {
			try {
				messageProducer = session.createProducer(session.createQueue(destinationName));
				messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

				destinationMap.put(destinationName, messageProducer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return messageProducer;
	}

	/**
	 * 发送系统通知
	 */
	public static boolean sendGlobalNotification(String title, String description, String module,
			Map<String, Object> map) {
		if (session == null) {
			return false;
		}
		try {
			MapMessage message = session.createMapMessage();
			message.setString("tag", "child");
			message.setString("title", title);
			message.setString("description", description);
			message.setString("module", module);
			message.setObject("map", map);

			MessageProducer messageProducer = getMessageProducer(null);
			messageProducer.send(message);

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

		try {
			MapMessage message = session.createMapMessage();
			message.setString("tag", tag);
			message.setString("title", title);
			message.setString("description", description);
			message.setString("module", module);
			message.setObject("map", map);

			MessageProducer messageProducer = getMessageProducer(schoolId);
			messageProducer.send(message);
			session.commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean sendClassesNotification(Long schoolId, Long classesId, String title, String description,
			String module, Map<String, Object> map) {
		return sendClassesNotification(schoolId, classesId, null, title, description, module, map);
	}

	/**
	 * 发送班级通知
	 */
	public static boolean sendClassesNotification(Long schoolId, Long classesId, Integer type, String title,
			String description, String module, Map<String, Object> map) {
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

			MessageProducer messageProducer = getMessageProducer(schoolId);
			messageProducer.send(message);

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
	public static boolean sendPersonalNotification(Long schoolId, Long id, String title, String description,
			String module, Map<String, Object> map) {
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

			MessageProducer messageProducer = getMessageProducer(schoolId);
			messageProducer.send(message);

			session.commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
