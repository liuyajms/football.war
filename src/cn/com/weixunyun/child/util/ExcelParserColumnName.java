package cn.com.weixunyun.child.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelParserColumnName {
	
	public static Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
	static {
		Map<String, String> m = new LinkedHashMap<String, String>();
		m.put("name", "学生");
		m.put("gender", "性别");
		m.put("birthday", "生日");
		m.put("code", "学籍号");
		m.put("card", "卡号");
		m.put("address", "联系地址");
		m.put("description", "备注");
		m.put("parentsName", "家长姓名");
		m.put("parentsMobile", "家长手机号");
		m.put("parentsUsername", "家长用户名");
		m.put("parentsType", "与学生关系");
		m.put("parentsPta", "是否家委会成员");
		map.put("student", m);
		
		m = new LinkedHashMap<String, String>();
		m.put("name", "姓名");
		m.put("gender", "性别");
		m.put("mobile", "手机号码");
		m.put("username", "用户名");
		m.put("card", "编号");
		m.put("code", "卡号");
		m.put("title", "职称");
		m.put("email", "邮箱");
		m.put("remark", "教师介绍");
		m.put("description", "备注");
		m.put("type", "是否管理员");
		map.put("teacher", m);

        m = new LinkedHashMap<String, String>();
        m.put("name", "球场名称");
        m.put("rule", "场地类型");
        m.put("mobile", "联系电话");
        m.put("address", "注册地");
        m.put("detailAddress", "具体地址");
        m.put("px", "经度");
        m.put("py", "纬度");
        m.put("openTime", "营业时间");
        m.put("description", "备注");
        map.put("court", m);
	}

}
