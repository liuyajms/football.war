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

public class PushConsumer {

	private static String pushName;

	private static String pushKey;
	private static String pushSecret;

	private static int index = 0;

	public static void main(String[] args) throws Exception {
		if (args == null || args.length < 5) {
			System.out
					.println("usage: PushConsumer push.name push.key push.secret jms.url jms.destination jms.username jms.password");
			return;
		}

		pushName = args[0];

		pushKey = args[1];
		pushSecret = args[2];

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
				System.out.println(msg);
				try {
					MapMessage message = (MapMessage) msg;

					String tag = message.getString("tag");
					String title = message.getString("title");
					String description = message.getString("description");
					String module = message.getString("module");
					Map<String, Object> map = (Map<String, Object>) message.getObject("map");
					if (module != null) {
						if (map == null) {
							map = new HashMap<String, Object>();
						}
						map.put("module", module);
					}
					JPushClient jpush = new JPushClient(pushSecret, pushKey);
					jpush.sendNotificationWithTag(index++, tag, title == null ? pushName : title, description, 0, map);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
