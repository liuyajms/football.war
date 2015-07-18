package cn.com.weixunyun.child;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import net.spy.memcached.MemcachedClient;

@SuppressWarnings("unchecked")
public class Session {

	public static Session getInstance(String id) {
		return new Session(id);
	}

	private Session(String id) {
		this.id = id;
	}

	private String id;
	private static final int EXP = 30 * 60;
	private static MemcachedClient memcachedClient = null;
	static {
		String host = PropertiesListener.getProperty("session.memcached.host", "localhost");
		int port = Integer.parseInt(PropertiesListener.getProperty("session.memcached.port", "11211"));
		try {
			memcachedClient = new MemcachedClient(new InetSocketAddress(host, port));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public <T extends Serializable> Session set(String k, T v) {
		if (getId() == null) {
			return null;
		}
		Map<String, T> map = (Map<String, T>) memcachedClient.get(getId());
		if (map == null) {
			map = new HashMap<String, T>();
		}
		if (v == null) {
			map.remove(k);
		} else {
			map.put(k, v);
		}
		memcachedClient.set(getId(), EXP, map);
		return this;
	}

	public <T extends Serializable> T get(String k) {
		if (getId() == null) {
			return null;
		}
		Map<String, T> map = (Map<String, T>) memcachedClient.get(getId());
		if (map == null) {
			return null;
		} else {
			memcachedClient.set(getId(), EXP, map);
			return (T) map.get(k);
		}
	}

	public <T extends Serializable> T remove(String k) {
		if (getId() == null) {
			return null;
		}
		Map<String, T> map = (Map<String, T>) memcachedClient.getAndTouch(getId(), EXP);
		if (map == null) {
			return null;
		} else {
			T v = map.remove(k);
			memcachedClient.set(getId(), EXP, map);
			return v;
		}
	}

	public void destroy() {
		if (getId() == null) {
			return;
		}
		try {
			memcachedClient.delete(getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
