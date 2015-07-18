package cn.com.weixunyun.child.control;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.com.weixunyun.child.PropertiesListener;
import cn.com.weixunyun.child.Session;
import cn.com.weixunyun.child.model.pojo.Parents;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.GlobalService;
import cn.com.weixunyun.child.model.service.SequenceService;
import cn.com.weixunyun.child.model.service.ServiceFactory;

public abstract class AbstractResource {

	protected <T> T getService(Class<T> cls) {
		return ServiceFactory.getService(cls);
	}

	protected School getAuthedSchool(String rsessionid) {
		return Session.getInstance(rsessionid).get("school");
	}

	protected Long getAuthedSchoolId(String rsessionid) {
		return getAuthedSchool(rsessionid).getId();
	}

	protected Long getAuthedId(String rsessionid) {
		Teacher teacher = getAuthedTeacher(rsessionid);
		if (teacher == null) {
			Parents parents = getAuthedParents(rsessionid);
			return parents == null ? null : parents.getId();
		} else {
			return teacher.getId();
		}
	}

	protected String getAuthedName(String rsessionid) {
		Teacher teacher = getAuthedTeacher(rsessionid);
		if (teacher == null) {
			Parents parents = getAuthedParents(rsessionid);
			return parents == null ? null : parents.getName();
		} else {
			return teacher.getName();
		}
	}

	protected Teacher getAuthedTeacher(String rsessionid) {
		return Session.getInstance(rsessionid).get("teacher");
	}

	protected Parents getAuthedParents(String rsessionid) {
		return Session.getInstance(rsessionid).get("parents");
	}

	protected Map<String, PartField> part(MultivaluedMap<String, String> form) {
		Map<String, PartField> map = new HashMap<String, PartField>();
		try {
			for (String name : form.keySet()) {
				PartField field = new PartField();
				field.setName(name);
				for (String value : form.get(name)) {
					field.addValue(URLDecoder.decode(value, "UTF-8"));
				}

				map.put(name, field);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	protected Map<String, PartField> partMulti(HttpServletRequest request) {
		Map<String, PartField> map = new HashMap<String, PartField>();
		if (true || ServletFileUpload.isMultipartContent(request)) {
			ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());
			fileUpload.setHeaderEncoding("utf-8");
			List<FileItem> items = null;
			try {
				items = fileUpload.parseRequest(request);
				for (FileItem item : items) {
					PartField field = map.get(item.getFieldName());
					if (field == null) {
						map.put(item.getFieldName(), field = new PartField());
					}
					field.setType(item.getContentType());
					field.setName(item.getFieldName());
					if (item.isFormField()) {
						field.addValue(item.getString("utf-8"));
					} else if (item.getSize() > 0) {
						field.addFile(new PartFieldFile(item));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	protected String getFilePath() {
		return PropertiesListener.getProperty("path", null) + "/files/";
	}

	/**
	 * 有文件上传的反射方法
	 * 
	 * @param cls
	 * @param map
	 * @return
	 * @deprecated
	 * @see #buildBean(Class, Map, Long)
	 */
	protected <T> T buildBean(Class<T> cls, Map<String, PartField> map) {
		try {
			T t = cls.newInstance();
			Method[] methods = cls.getMethods();
			String name = "";
			// 反射方法调用
			for (Method method : methods) {
				name = method.getName();
				if (name.startsWith("set")) {
					name = name.substring(3).replaceFirst(name.substring(3, 4), name.substring(3, 4).toLowerCase());
					PartField obj = map.get(name);
					if (obj != null) {
						if (method.getParameterTypes()[0] == String.class) {
							method.invoke(t, URLDecoder.decode(obj.getValue(), "UTF-8"));
						} else if (method.getParameterTypes()[0] == Boolean.class) {
							method.invoke(t, true);
						} else if (method.getParameterTypes()[0] == Long.class) {
							if (!"".equals(obj.getValue())) {
								method.invoke(t, Long.valueOf(obj.getValue()));
							}
						}
					} else {
						if (method.getParameterTypes()[0] == Timestamp.class) {
							method.invoke(t, new Timestamp(System.currentTimeMillis()));
						}
					}
				}
			}
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected <T> T buildBean(Class<T> cls, MultivaluedMap<String, String> map, Long id) {
		return buildBean(cls, part(map), id);
	}

	protected <T> T buildBean(Class<T> cls, Map<String, PartField> map, Long id) {
		try {
			T t = cls.newInstance();
			Method[] methods = cls.getMethods();
			String name = "";
			Boolean flag = id == null;
			if (flag) {
				id = getService(SequenceService.class).sequence();
			}

			for (Method method : methods) {
				name = method.getName();
				if (name.startsWith("set")) {
					name = name.substring(3).replaceFirst(name.substring(3, 4), name.substring(3, 4).toLowerCase());
					String str = null;
					if ((null != map.get(name)) && !"".equals(map.get(name))) {
						str = map.get(name).getValue();
					}
					Class<?> type = method.getParameterTypes()[0];
					if (type == String.class) {
						method.invoke(t, str);
					} else if (type == Boolean.class) {
						if ("1".equals(str)) {
							method.invoke(t, true);
						} else {
							method.invoke(t, false);
						}
					} else if (type == Timestamp.class) {
						if (flag) {
							method.invoke(t, new Timestamp(System.currentTimeMillis()));
						} else {
							if (!"createTime".equals(name)) {
								method.invoke(t, new Timestamp(System.currentTimeMillis()));
							}
						}

					} else if (type == Long.class) {
						if ("id".equals(name)) {
							method.invoke(t, id);
						} else if ((str != null) && (!"".equals(str))) {
							method.invoke(t, Long.valueOf(str));
						}

					} else if (type == int.class) {
						if ("id".equals(name)) {
							method.invoke(t, id);
						} else if ((str != null) && (!"".equals(str))) {
							method.invoke(t, Integer.valueOf(str));
						}

					} else if (type == Double.class) {
						if ((str != null) && (!"".equals(str))) {
							method.invoke(t, Double.valueOf(str));
						}
					}
				}
			}
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static class PartField {
		private String name;
		private String type;
		private List<String> valueList;
		private List<PartFieldFile> fileList;

		public PartFieldFile getFile() {
			return fileList == null || fileList.size() == 0 ? null : fileList.get(0);
		}

		public void addFile(PartFieldFile file) {
			if (fileList == null) {
				fileList = new ArrayList<PartFieldFile>();
			}
			fileList.add(file);
		}

		public List<PartFieldFile> getFileList() {
			return fileList;
		}

		public void setFileList(List<PartFieldFile> fileList) {
			this.fileList = fileList;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isValueNull() {
			return valueList == null || valueList.size() == 0 || (valueList.size() == 1 && "".equals(valueList.get(0)));
		}

		public String getValue() {
			return valueList == null || valueList.size() == 0 ? null : valueList.get(0);
		}

		public void addValue(String value) {
			if (valueList == null) {
				valueList = new ArrayList<String>();
			}
			valueList.add(value);
		}

		public List<String> getValueList() {
			return valueList;
		}

		public void setValueList(List<String> valueList) {
			this.valueList = valueList;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

	public static class PartFieldFile extends java.io.File {

		public PartFieldFile(FileItem item) {
			super(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
			if (this.getParentFile().exists() == false) {
				this.getParentFile().mkdirs();
			}

			try {
				item.write(this);

				oriName = URLDecoder.decode(item.getName(), "utf-8");
				size = item.getSize();
				contentType = item.getContentType();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public PartFieldFile(File arg0, String arg1) {
			super(arg0, arg1);
		}

		public PartFieldFile(String arg0, String arg1) {
			super(arg0, arg1);
		}

		public PartFieldFile(String arg0) {
			super(arg0);
		}

		public PartFieldFile(URI arg0) {
			super(arg0);
		}

		private long size;
		private String contentType;
		private String oriName;

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getOriName() {
			return oriName;
		}

		public void setOriName(String oriName) {
			this.oriName = oriName;
		}

	}

	protected class DMLResponse {
		public DMLResponse() {

		}

		public DMLResponse(boolean success, String message) {
			this.success = success;
			this.message = message;
		}

		private boolean success;
		private String message;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	protected String getCurrentTerm(Long schoolId) {
		GlobalService globalService = getService(GlobalService.class);
		return globalService.select(schoolId, "term", "default").getValue();
	}

	/**
	 * 
	 * @param schoolId
	 *            学校
	 * @param grade
	 *            年级，如2表示3年级
	 * @return 班级入学年份，如2012年
	 */
	protected int getClassesYear(Long schoolId, int grade) {
		GlobalService globalService = getService(GlobalService.class);
		String currentTerm = globalService.select(schoolId, "term", "default").getValue();
		return getClassesYear(currentTerm, grade);
	}

	/**
	 * 
	 * @param currentTerm
	 *            当前学期，如20140代表2014年上学期，20141代表2014年下学期
	 * @param grade
	 *            年级，如2表示3年级
	 * @return 班级入学年份，如2012年
	 */
	protected int getClassesYear(String currentTerm, int grade) {
		return Integer.parseInt(currentTerm.substring(0, 4)) - grade;
	}

	/**
	 * 
	 * @param schoolId
	 *            学校
	 * @param classesYear
	 *            班级入学年份，如2012年
	 * @return 年级，如2表示3年级
	 */
	protected int getGrade(Long schoolId, int classesYear) {
		GlobalService globalService = getService(GlobalService.class);
		String currentTerm = globalService.select(schoolId, "term", "default").getValue();
		return getGrade(currentTerm, classesYear);
	}

	/**
	 * 
	 * @param currentTerm
	 *            当前学期，如20140代表2014年上学期，20141代表2014年下学期
	 * @param classesYear
	 *            班级入学年份，如2012年
	 * @return 年级，如2表示3年级
	 */
	protected int getGrade(String currentTerm, int classesYear) {
		return Integer.parseInt(currentTerm.substring(0, 4)) - classesYear;
	}

}
