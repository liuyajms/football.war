package cn.com.weixunyun.child.third.simiyun;

import java.net.URLEncoder;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.wink.client.RestClient;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.weixunyun.child.PropertiesListener;

public class SimiyunHelper {

	private static String simiyunUrl = PropertiesListener.getProperty("simiyun.url", null);

	//添加联系人
	public static long addUser(Long authedId, Long userId, String nickName) {
		String pwd = password(userId);

		RestClient client = new RestClient();
		String path = getUrl("/add_user", "username", userId, "password", pwd, "nick", nickName);

		org.apache.wink.client.Resource resource = client.resource(path);

		String s = resource.get(String.class);
		Long uid = null;
		if (s != null && !"".equals(s)) {
			try {
				JSONObject object = new JSONObject(s);
				if (!object.isNull("uid")) {
					uid = (long) object.getInt("uid");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return uid;
	}

	//往群组里面添加用户
	public static void addGroupUsers(String userId, Long authedId, Long classesId, boolean manager) {
		RestClient client = new RestClient();
		String path = getUrl("/groupEdu/users", "user_name", userId, "username", authedId, "type", "inviteUser",
				"group_name", classesId, "role_id", manager == true ? 3 : 5);
		org.apache.wink.client.Resource resource = client.resource(path);
		resource.get();
	}
	
	public static long addGroupManager(Long userId, Long authedId, Long classesId) {
		RestClient client = new RestClient();
		String path = getUrl("/groupEdu/users", "user_name", userId, "username", authedId, "type", "inviteUser",
				"group_name", classesId, "role_id", 3);
		org.apache.wink.client.Resource resource = client.resource(path);
		
		String s1 = resource.get(String.class);
		Long uid = null;
		if (s1 != null && !"".equals(s1)) {
			try {
				JSONObject object1 = new JSONObject(s1);
				if (!object1.isNull("uid")) {
					uid = object1.getLong("uid");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return uid;
	}

	//删除群组里面的用户
	public static void delGroupUsers(Long userId, Long authedId, Long classesId) {
			RestClient client = new RestClient();
			String path = getUrl("/groupsEdu/users", "user_name", userId, "username", authedId, "type", "delete",
					"group_name", classesId);
			org.apache.wink.client.Resource resource = client.resource(path);
			resource.get();
		}
	
	//删除用户信息
	public static void delUser(Long userId) {
		RestClient client = new RestClient();
		String path = getUrl("/update_user", "username", userId, "action", "del");
		org.apache.wink.client.Resource resource = client.resource(path);
		resource.get();
	}

	//添加群组
	public static String addGroup(Long classesId, Long authedId, String desc) {
		RestClient client = new RestClient();
		String path = getUrl("/groupsEdu/create", "group_name", classesId, "username", authedId, "group_type", 1, "group_summary", desc, "search_ok", 0);
		org.apache.wink.client.Resource resource = client.resource(path);

		String s = resource.get(String.class);
		String uid = null;
		if (s != null && !"".equals(s)) {
			try {
				JSONObject object = new JSONObject(s);
				if (!object.isNull("uid")) {
					uid = object.getString("uid");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return uid;
	}

	//删除群组
	public static void delGroup(Long classesId, Long authedId) {
		RestClient client = new RestClient();
		String path = getUrl("/groupsEdu/delete", "type", "delete", "group_name", classesId, "username", authedId);
		org.apache.wink.client.Resource resource = client.resource(path);
		resource.get();
	}

	private static String password(Long s) {
		return DigestUtils.md5Hex(s.toString()).substring(0, 5);
	}

	private static String getUrl(String url, Object... ss) {
		StringBuilder sb = new StringBuilder(simiyunUrl);
		sb.append(url + "?");
		if (ss != null && ss.length % 2 == 0) {
			try {
				for (int i = 0; i < ss.length; i += 2) {
					sb.append(ss[i] + "=" + URLEncoder.encode(ss[i + 1].toString(), "utf8"));
					if (i != ss.length - 2) {
						sb.append("&");
					}
				}
			} catch (Exception e) {

			}
		}
		return sb.toString();
	}

	public static void main(String args[]) {
		System.out.println("1".equals(null));
	}
}
