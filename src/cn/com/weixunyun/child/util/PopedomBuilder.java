package cn.com.weixunyun.child.util;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.WinkApplication;
import cn.com.weixunyun.child.module.news.NewsResource;

public class PopedomBuilder {

	static int ACTIONS = 16;

	public static void main(String[] args) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into popedom (id, id_parent, code, name, menu, idx");
		for (int i = 0; i < ACTIONS; i++) {
			sb.append(",action" + i);
			sb.append(",action" + i + "_name");
		}
		sb.append(") values (?, null, ?, ?, true, ?");
		for (int i = 0; i < ACTIONS; i++) {
			sb.append(",?");
			sb.append(",?");
		}
		sb.append(")");

		Class.forName("org.postgresql.Driver");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://192.168.2.250:5432/child", "postgres", "1234");
			conn.createStatement().executeUpdate("delete from popedom ");
			PreparedStatement ps = conn.prepareStatement(sb.toString());

			WinkApplication wa = new WinkApplication();
			int i = 0;
			for (Class c : wa.getClasses()) {
				Path path = (Path) c.getAnnotation(Path.class);
				ps.setLong(1, i + 1);
				ps.setString(2, path.value());

				if (c.isAnnotationPresent(Description.class)) {
					Description description = (Description) c.getAnnotation(Description.class);
					ps.setString(3, description.value());
				} else {
					ps.setString(3, path.value());
				}

				ps.setLong(4, i + 1);
				
				System.out.println(path.value());

				Map<String, String> methodMap = new TreeMap<String, String>();
				for (Method method : c.getDeclaredMethods()) {
					String code = null;
					if (method.isAnnotationPresent(GET.class)) {
						code = "GET";
					} else if (method.isAnnotationPresent(POST.class)) {
						code = "POST";
					} else if (method.isAnnotationPresent(PUT.class)) {
						code = "PUT";
					} else if (method.isAnnotationPresent(DELETE.class)) {
						code = "DELETE";
					} else {
						continue;
					}
					boolean isAnn = method.isAnnotationPresent(Path.class);
					if (isAnn) {
						code += " " + method.getAnnotation(Path.class).value();
					}

					String name = null;

					if (method.isAnnotationPresent(Description.class)) {
						Description description = method.getAnnotation(Description.class);
						name = description.value();
					} else {
						name = code;

					}
					methodMap.put(code, name);
				}
				
				int j = 0;
				for (Map.Entry<String, String> entry : methodMap.entrySet()) {
					ps.setString(5 + j * 2, entry.getKey());
					ps.setString(5 + j * 2 + 1, entry.getValue());
					j++;
				}
				for (int k = j; k < ACTIONS; k++) {
					ps.setString(5 + k * 2, null);
					ps.setString(5 + k * 2 + 1, null);
				}
				ps.executeUpdate();
				i++;
			}
			ps.close();
		} finally {
			try {
				conn.close();
			} finally {
				conn = null;
			}
		}

	}

}
