package cn.com.weixunyun.child.util;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import cn.jpush.api.JPushClient;

public class SimiyunConsumer {

	private static String simiyunName;

	private static String simiyunKey;
	private static String simiyunSecret;

	private static int index = 0;

	public static void main(String[] args) throws Exception {
		if (args == null || args.length < 5) {
			System.out
					.println("usage: simiyunConsumer simiyun.name simiyun.key simiyun.secret jms.url jms.destination jms.username jms.password");
			return;
		}

		simiyunName = args[0];

		simiyunKey = args[1];
		simiyunSecret = args[2];

		String username = null;
		String password = null;
		if (args.length >= 6) {
			username = args[5];
		}
		if (args.length >= 7) {
			password = args[6];
		}
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, args[3]);
		Connection connection = connectionFactory.createConnection();
		connection.start();

		Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(args[4]);
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message msg) {
				System.out.println(Thread.currentThread().getId() + " " + new java.util.Date() + " " + msg);
				try {
					MapMessage message = (MapMessage) msg;

					String tag = message.getString("tag");
					System.out.println(tag);
					String url = message.getString("url");
					/*String description = message.getString("description");
					String module = message.getString("module");*/
					Map<String, Object> map = (Map<String, Object>) message.getObject("map");
					/*if (module != null) {
						if (map == null) {
							map = new HashMap<String, Object>();
						}
						map.put("module", module);
					}*/
					System.out.println("sending.....");
					JPushClient jpush = new JPushClient(simiyunSecret, simiyunKey);
					jpush.sendNotificationWithTag(index++, tag, simiyunName, "", 0, map);
					System.out.println("sended.....");
					// jpush.sendNotificationWithAppKey((int) sendNo, title ==
					// null ? NAME : title, description, 0, map);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
