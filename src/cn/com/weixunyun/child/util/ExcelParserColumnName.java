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
		m.put("name", "班级名称");
		m.put("year", "入学年份");
		m.put("num", "班级号数");
		m.put("teacherId", "班主任");
		m.put("description", "班级介绍");
		map.put("classes", m);

        m = new LinkedHashMap<String, String>();
        m.put("studentName", "学生姓名");
        m.put("studentNo", "学籍号");
        m.put("name", "成长名称");
        m.put("type", "考试类型");
        m.put("date", "考试日期");
        m.put("score","分数");
        map.put("course_score", m);

        m = new LinkedHashMap<String, String>();
        m.put("studentName", "学生姓名");
        m.put("studentNo", "学籍号");
        m.put("name", "成长名称");
        m.put("type", "成长类型");
        m.put("description", "成长内容");
        map.put("student_grow", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("classesName", "班级");
        m.put("courseName", "课程");
        m.put("name", "知识点");
        map.put("course_knowledge", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("classesName", "班级");
        m.put("courseName", "课程");
        m.put("name", "文件名称");
        map.put("course_classroom", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("classesName", "班级");
        m.put("courseName", "课程");
        m.put("studentName", "学生");
        m.put("praise", "今日表扬数");
        m.put("accuracy","答题正确率");
        m.put("description","评价内容");
        map.put("course_evaluation", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("classesName", "班级");
        m.put("name", "文件名称");
        m.put("name", "置顶天数");
        map.put("download", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("title", "标题");
        m.put("descriptionSummary", "内容摘要");
        map.put("news", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("description", "公告内容");
        m.put("pushParents", "推送至家长");
        m.put("pushTeacher", "推送至教师");
        map.put("notice", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("grade", "广播年级");
        m.put("title", "标题");
        m.put("description", "内容");
        map.put("broadcastGrade", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("classesName", "班级");
        m.put("title", "标题");
        m.put("description", "内容");
        map.put("broadcastClasses", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("name", "课程名称");
        m.put("teacherName", "教师姓名");
        m.put("date", "上课时间");
        m.put("num", "允许选课人数");
        m.put("grade", "允许选课年级");
        m.put("choosedCount", "已选人数");
        map.put("elective", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("classesName", "班级");
        m.put("description", "微博内容");
        map.put("weiboClasses", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("classesName", "班级");
        m.put("name", "姓名");
        map.put("star", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("name", "食谱名称");
        map.put("cook", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("name", "课程名称");
        map.put("course", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("electiveName", "课程名称");
        m.put("studentName", "学生姓名");
        m.put("score", "成绩");
        m.put("scoreDescription", "成绩评价");
        map.put("electiveStudent", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("classesName", "班级");
        m.put("type", "课程类型");
        map.put("curriculum", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("name", "主关键词");
        m.put("available", "是否可见");
        m.put("name0", "关键词1");
        m.put("name1", "关键词2");
        m.put("name2", "关键词3");
        m.put("name3", "关键词4");
        m.put("name4", "关键词5");
        m.put("name5", "关键词6");
        m.put("name6", "关键词7");
        m.put("name7", "关键词8");
        map.put("sensitive", m);
        
        m = new LinkedHashMap<String, String>();
        m.put("classesName", "班级");
        m.put("courseId", "课程");
        m.put("teacherId", "教师");
        map.put("classesTeacher", m);
	}

}
