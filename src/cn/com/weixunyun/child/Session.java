package cn.com.weixunyun.child;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("unchecked")
public class Session {

    public static Session getInstance(String id) {
        return new Session(id);
    }

    private Session(String id) {
        this.id = id;
    }

    private String id;
    private static final int EXP = 30 * 3600 * 24;
    private static MemcachedClient memcachedClient = null;

    static {
        String host = PropertiesListener.getProperty("session.memcached.host", "localhost");
        int port = Integer.parseInt(PropertiesListener.getProperty("session.memcached.port", "11211"));
        try {
            MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(host + ":" + port));
            builder.setConnectionPoolSize(5);
            //默认如果连接超过5秒没有任何IO操作发生即认为空闲并发起心跳检测, 设置为10秒;
//            builder.getConfiguration().setSessionIdleTimeout(10000);
            memcachedClient = builder.build();
            //如果你对心跳检测不在意，也可以关闭心跳检测，减小系统开销
            memcachedClient.setEnableHeartBeat(false);
            //这个关闭，仅仅是关闭了心跳的功能，客户端仍然会去统计连接是否空闲，禁止统计可以通过：
            builder.getConfiguration().setStatisticsServer(false);
            //setOpTimeout设置的是全局的等待时间
            memcachedClient.setOpTimeout(5000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MemcachedClient getMemcachedClient() {
        return memcachedClient;
    }

    public static void setMemcachedClient(MemcachedClient memcachedClient) {
        Session.memcachedClient = memcachedClient;
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
        Map<String, T> map = null;
        try {
            map = (Map<String, T>) memcachedClient.get(getId());
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
        if (map == null) {
            map = new HashMap<String, T>();
        }
        if (v == null) {
            map.remove(k);
        } else {
            map.put(k, v);
        }
        try {
            memcachedClient.set(getId(), EXP, map);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public <T extends Serializable> T get(String k) {
        if (getId() == null) {
            return null;
        }
        Map<String, T> map = null;
        try {
            map = (Map<String, T>) memcachedClient.get(getId());
            if (map == null) {
                return null;
            } else {
                memcachedClient.set(getId(), EXP, map);
                return (T) map.get(k);
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T extends Serializable> T remove(String k) {
        if (getId() == null) {
            return null;
        }
        Map<String, T> map = null;
        try {
            map = (Map<String, T>) memcachedClient.getAndTouch(getId(), EXP);
            if (map == null) {
                return null;
            } else {
                T v = map.remove(k);
                memcachedClient.set(getId(), EXP, map);
                return v;
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
        return null;
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
