package cn.com.weixunyun.child.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BeanBuilder {
	private Connection conn = null; // 保存链接路径
	private Statement stmt = null; // 建立连接
	private ResultSetMetaData meta = null; // 保存表属性信息
	private ResultSet rs = null; // 查询结果集
	private File f = null; // 建立的文件
	private OutputStreamWriter osw = null;
	private BufferedWriter bw = null;
	private FileOutputStream fos = null;
	private static StringBuffer coding = new StringBuffer(); // 字符串缓冲区
	private String packageName = null; // 数据库包名
	private String url = null; // 路径名
	private String table = null; // 表空间名
	private String password = null; // 密码
	private String tableName = null;
	private String className = null;

	public BeanBuilder(String packageName, String url, String table, String password, String tableName) {
		this.packageName = packageName;
		this.url = url;
		this.table = table;
		this.password = password;
		this.tableName = tableName;
		this.className = replaceChar(tableName.substring(0, 1).toUpperCase()
				+ tableName.substring(1, tableName.length()));
		f = new File("D:/adt-bundle-windows-x86-20130522/workspace/child.war/src/com/zxcx/child/model/pojo/" + this.className + ".java");
		if (!f.exists()) { // 如果文件不存在则建立文件
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getCoding(StringBuffer code) {
		return code.toString();
	}

	private StringBuffer generate(String prop, String type, String firstUpper) {
		coding.append("public void set" + firstUpper + "(" + type + " " + prop + "){\n");
		coding.append("this." + prop + "=" + prop + ";\n");
		coding.append("}\n");
		coding.append("public " + type + " get" + firstUpper + "(){\n");
		coding.append("return " + prop + ";\n");
		coding.append("}\n\n");
		return coding;

	}

	private void destroy() {
		/*
		 * 关闭与数据库的所有链接
		 */
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}

			if (conn != null) {
				conn.close();
				conn = null;
			}

			if (bw != null) {
				bw.close();
				bw = null;
			}
			if (fos != null) {
				fos.close();
				fos = null;
			}
			if (osw != null) {
				osw.close();
				osw = null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	private void connect() {
		/*
		 * 数据库连接发生异常就关闭链接
		 */
		try {
			Class.forName(packageName);
			conn = DriverManager.getConnection(url, table, password);
			stmt = conn.createStatement();

			rs = stmt.executeQuery("select  * from " + tableName); // 查询下确定结果集是那个表的
			meta = rs.getMetaData(); // 调用结果集的记录表信息的方法
			// System.out.println("a a a ");
			// rs.next();
			// System.out.println(rs.getString("name"));
		} catch (Exception e) {
			e.printStackTrace();
			destroy();
		}
	}

	private Map<String, String> getColumenName() {
		/*
		 * 得到表的所有列名以字符串数组的形式返回
		 */
		int count;
		String name;
		String strType;
		String[] s = null;
		Map<String, String> nameType = new LinkedHashMap<String, String>();
		try {
			count = meta.getColumnCount();
			ResultSetMetaData rsd = rs.getMetaData();
			String[] strColumenName = new String[count];
			for (int i = 1; i <= count; i++) {
				name = meta.getColumnName(i);
				int precision = rsd.getPrecision(i);
				strType = this.getObjectType(rsd.getColumnType(i), precision);
				nameType.put(name, strType);
				// System.out.println(str);
			}
			s = strColumenName;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nameType;
	}

	public String getObjectType(int type, int precision) {
		switch (type) {
		case Types.ARRAY:
			return null;
		case Types.BIGINT:
			return "Long";
		case Types.BINARY:
			return null;
		case Types.BIT:
			return "Boolean";
		case Types.BLOB:
			return "Blob";
		case Types.BOOLEAN:
			return "Boolean";
		case Types.CHAR:
			return "String";
		case Types.CLOB:
			return "Clob";
		case Types.DATALINK:
			return null;
		case Types.DATE:
			return "java.sql.Date";
		case Types.DECIMAL:
			return "Double";
		case Types.DISTINCT:
			return null;
		case Types.DOUBLE:
			return "Double";
		case Types.FLOAT:
			return "Float";
		case Types.INTEGER:
			return "Integer";
		case Types.NUMERIC:
			if (precision > 0) {
				return "Double";
			} else {
				return "Long";
			}
		case Types.JAVA_OBJECT:
			return null;
		case Types.LONGVARBINARY:
			return null;
		case Types.LONGVARCHAR:
			return null;
		case Types.NULL:
			return null;
		case Types.OTHER:
			return null;
		case Types.REAL:
			return null;
		case Types.REF:
			return null;
		case Types.SMALLINT:
			return "Short";
		case Types.STRUCT:
			return null;
		case Types.TIME:
			return "Time";
		case Types.TIMESTAMP:
			return "java.sql.Timestamp";
		case Types.TINYINT:
			return "Short";
		case Types.VARBINARY:
			return null;
		case Types.VARCHAR:
			return "String";
		default:
			return "String";
		}

	}

	// 将带‘-’的表名变成骆驼写法
	public String replaceChar(String name) {
		String newString = "";
		int index = 0;
		for (int i = 0; i < name.length(); i++) {
			char sub = name.charAt(i);
			if (sub == '_') {
				String n = name.substring(i, i + 2).toUpperCase().substring(1, 2);
				System.out.println(n);
				newString += name.substring(index, i) + n;
				index = i + 2;
			}
		}
		newString += name.substring(index, name.length());
		return newString;
	}

	private void writeData(String message) {

		try {
			fos = new FileOutputStream("D:/adt-bundle-windows-x86-20130522/workspace/child.war/src/com/zxcx/child/model/pojo/" + className + ".java");
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			bw.write(message);
			bw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// String[] tables = { "broadcast", "classes", "classes_teacher",
		// "contact", "cook", "course", "curriculum", "dictionary_field",
		// "dictionary_table", "dictionary_value", "download", "global",
		// "homework", "homework_complete", "menu", "notice", "news",
		// "parents", "performance", "phrase", "popedom", "region",
		// "role", "role_menu", "role_popedom", "school", "student",
		// "teacher" };
		String[] tables = { "popedom" };
		for (int i = 0; i < tables.length; i++) {
			BeanBuilder ta = new BeanBuilder("org.postgresql.Driver", "jdbc:postgresql://192.168.2.250:5432/child",
					"postgres", "1234", tables[i]);
			ta.connect();
			coding.append("package com.zxcx.child.model.pojo;\n");
			coding.append("import java.io.Serializable;\n");
			coding.append("public class " + ta.className + " implements Serializable {\n");
			Map<String, String> nameType;
			nameType = ta.getColumenName();
			Set<String> names = nameType.keySet();
			for (String n : names) {
				String type = nameType.get(n);
				String name = ta.replaceChar(n);
				coding.append("private " + type + " " + name + ";\n");
			}
			for (String n : names) {
				String type = nameType.get(n);
				String name = ta.replaceChar(n);
				String firstUpper = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
				ta.generate(name, type, firstUpper);
			}
			// ta.writeData(ta.getCoding());
			coding.append("}");
			ta.writeData(ta.getCoding(coding));
			coding.delete(0, coding.length());
			ta.destroy();
			// System.out.println(s[0]);

		}
		System.out.println("success");
	}
}
