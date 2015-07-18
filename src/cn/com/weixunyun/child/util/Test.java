package cn.com.weixunyun.child.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
	private List<Map<String, String>> list;
	
	public void getCols(String tableName) throws Exception {
		list = new ArrayList<Map<String, String>>();
		Map<String, String> map ;
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.2.250:5432/child", "postgres", "1234");
		
		String sql = "select * from " + tableName;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData data = rs.getMetaData();
		for(int i = 1 ; i<= data.getColumnCount() ; i++){ 
			map = new HashMap<String, String>();
			map.put(data.getColumnName(i), data.getColumnClassName(i));
//			System.out.println("name:"+data.getColumnName(i));
//			System.out.println("type:"+data.getColumnTypeName(i));
//			System.out.println(data.getColumnClassName(i));
			list.add(map);
		}
		System.out.println("list:"+list);
	}
	
	public int gradeParser(String str) {
		str = "1、2年级";
		str = str.substring(0, str.length()-2);
		String attr[] = str.split("、");
		System.out.println(attr[0] + " " + attr[1]);
		
		int attr2[] = {0, 0, 0, 0, 0, 0, 0, 0};
		
		for (int i = 0; i < attr.length; i++) {
			attr2[Integer.parseInt(attr[i])-1] = 1;
		}
		
		int grade = 0;
		for (int i = 0; i < 8; i++) {
			if (attr2[i] == 1) {
				grade += (1 << i);
			}
		}
		
		System.out.println(grade);
		return 0;
	}
	
	public int grade(String str) {
		str = str.substring(0, str.length()-2);
		String attr[] = str.split("、");
		
		int attr2[] = {0, 0, 0, 0, 0, 0, 0, 0};
		
		for (int i = 0; i < attr.length; i++) {
			attr2[Integer.parseInt(attr[i])-1] = 1;
		}
		
		int grade = 0;
		for (int i = 0; i < 8; i++) {
			if (attr2[i] == 1) {
				grade += (1 << i);
			}
		}
		
		return grade;
	}
	
	public static void main(String[] args) {
		/*try {
			//new Test().getCols("classes");
			java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-08-09");
			System.out.println(new Date(date.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		System.out.println(new Test().gradeParser(""));
		
		System.out.println("grade:"+new Test().grade("4、5年级"));
	}
}
