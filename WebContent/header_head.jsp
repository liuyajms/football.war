<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();

cn.com.weixunyun.child.model.pojo.Teacher teacher = null;
cn.com.weixunyun.child.model.pojo.School school = null;
java.util.Map<cn.com.weixunyun.child.model.pojo.Menu, java.util.List<cn.com.weixunyun.child.model.pojo.Menu>> menuMap = null;
String rsessionid = cn.com.weixunyun.child.util.ServletUtils.getCookie(request, "rsessionid");
if (rsessionid != null) {
	school = cn.com.weixunyun.child.Session.getInstance(rsessionid).get("school");
	teacher = cn.com.weixunyun.child.Session.getInstance(rsessionid).get("teacher");
	menuMap = cn.com.weixunyun.child.Session.getInstance(rsessionid).get("menu");
}
if (teacher == null) {
	out.println("<script>location.href='" + path + "/login.jsp';</script>");
	return;
}
%>
<fmt:setLocale value="zh_CN" />

<%
	if (school.getType() == 0) {
%>
<fmt:setBundle basename="cn.com.weixunyun.child.school" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.news.news" var="news" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.notice.notice" var="notice" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.cook.cook" var="cook" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.broadcast.broadcast" var="broadcast" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.course.course" var="course" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.curriculum.curriculum" var="curriculum" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.download.download" var="download" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.elective.elective" var="elective" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.homework.homework" var="homework" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.security.security" var="security" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.star.star" var="star" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.weibo.weibo" var="weibo" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.weibo.weiboClasses" var="weiboClasses" />
<fmt:setBundle basename="cn.com.weixunyun.child.module.weibo.weiboComment" var="weiboComment" />
<%
	} else if (school.getType() == 1) {
%>
<fmt:setBundle basename="hospital" />
<%
	}
%>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="renderer" content="webkit" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<script src="<%=path%>/asset/jquery/jquery-1.11.1.min.js"></script>
<script src="<%=path%>/asset/jquery/jquery.cookie.js"></script>
<script src="<%=path%>/asset/jquery/jquery.form.js"></script>

<script src="<%=path%>/asset/application/GridEdit.js"></script>

<link rel="stylesheet" href="<%=path%>/asset/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" href="<%=path%>/asset/bootstrap/bootstrap-theme.min.css" />

<script src="<%=path%>/asset/bootstrap/bootstrap.min.js"></script>
<script src="<%=path%>/asset/bootstrap/jqBootstrapValidation.js"></script>

<script src="<%=path%>/asset/jquery/jquery.pagination.js"></script>

<script src="<%=path%>/asset/jquery.noty/jquery.noty.js"></script>
<script src="<%=path%>/asset/jquery.noty/jquery.noty.default.js"></script>
<script src="<%=path%>/asset/jquery.noty/jquery.noty.top.js"></script>

<script src="<%=path%>/asset/application/application.js"></script>

<script src="<%=path%>/asset/kindeditor/kindeditor-min.js"></script>
<script src="<%=path%>/asset/kindeditor/zh_CN.js"></script>

<script src="<%=path%>/asset/bootstrap/bootstrap-datetimepicker.min.js"></script>

<script src="<%=path%>/asset/application/rest.js"></script>


<script type="text/javascript"
	src="<%=path%>/asset/datepicker/bootstrap-datepicker.js"></script>
<script type="text/javascript"
	src="<%=path%>/asset/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/asset/validate/messages_zh.js"></script>

<!-- 
<style>
.icon {
	width: 16px;
	height: 16px;
	margin-right: 8px;
}

.bs-docs-nav .navbar-toggle .icon-bar {
	background-color: #4E8120;
}
</style>
 -->
 
 <style>
.icon {display:inline-block;width:24px;height:24px;background:url(../img/icon-s.png) no-repeat; vertical-align:-6px;}
.icon-add {background-position:0 0;}
.icon-phone {background-position:-24px 0;}
.icon-email {background-position:-48px 0;}
 </style>
 
<script>
		var username = <%=teacher.getId()%>;
		//var pwd = "<%=teacher.getPassword()%>";

	$(document).ready(function() {
		var type =<%=teacher.getType()%>;
		var schoolId =<%=school.getId()%>;
		
		if (type == 9 || schoolId == 0) {
			$("#school").attr("style", "display:''");
			$("#halt").attr("style", "display:''");
			$("#sensitive").attr("style", "display:''");
			$("#menu").attr("style", "display:none");
			$("#stats").attr("style", "display:none");
			$("#config").hide();
			$("#education").hide();
		} else {
			$("#school").attr("style", "display:none");
			$("#halt").attr("style", "display:none");
			$("#sensitive").attr("style", "display:none");
			$("#menu").attr("style", "display:''");
			$("#stats").attr("style", "display:none");
		}
		
		if (type == 6) {
			$("#menu").attr("style", "display:''");
			$("#config").attr("style", "display:''");
			$("#stats").attr("style", "display:''");
		} else if (type == 0) {
			$("#classes0").show();
			$("#menu").attr("style", "display:none");
			$("#config").attr("style", "display:none");
			$("#stats").attr("style", "display:none");
		}
		
		var classesId = $("#classesId");
		classesId.change(function() {
			$.cookie("classesId", $(this).children("option:selected").val());
			window.location = window.location;
		});
		
		initClassesSelect(classesId);
		if ($.cookie("classesId") != null) {
			classesId.val($.cookie("classesId"));
		}
		
		if (classesId.val() == null) {
			$("#classes_menu").hide();
		}
	});

	function quit() {
		if (confirm("<fmt:message key="quit.confirm" />")) {
			restDelete("<%=path%>/rest/auth", function() {
				location.href = "<%=path%>/login.jsp";
			});
		}
	}

	function download() {
		window.open("http://125.70.9.211:5080/index.php/site/nulllogin?userName="+8630+"&password=8630" );
	}
</script>
<title><%=school.getName()%></title>
</head>
<body>
<header class="navbar navbar-static-top bs-docs-nav" id="top" role="banner">
	<div class="container">
		<div class="navbar-header">
			<button class="navbar-toggle" type="button" data-toggle="collapse" data-target=".bs-navbar-collapse">
				<span class="sr-only"><fmt:message key="menu" /></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a href="<%=path%>/index.jsp" class="navbar-brand" style="color: #4E8120;"><%=school.getName()%></a>
		</div>
		<nav class="collapse navbar-collapse bs-navbar-collapse" role="navigation">
			<ul class="nav navbar-nav">
				<li class="dropdown" id="education"><a href="#" class="dropdown-toggle" data-toggle="dropdown">教育局 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="<%=path%>/module/notice/notice_education.jsp"><img
								src="<%=path%>/asset/application/images/icon_broadcast.png" class="icon" /> 教育公告</a></li>
						<li><a href="<%=path%>/module/question/question.jsp"><img
								src="<%=path%>/asset/application/images/icon_weibo.png" class="icon" /> 留言咨询</a></li>
					</ul></li>
					
				<li id="classes0" style="display:none"><a href="<%=path%>/classes_0.jsp">班级管理</a></li>
				
				<li class="dropdown active"><a href="#" class="dropdown-toggle" data-toggle="dropdown" id="menu"> <fmt:message
							key="school" /> <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="<%=path%>/teacher.jsp"><img src="<%=path%>/asset/application/images/icon_broadcast.png"
								class="icon" /> <fmt:message key="teacher" /></a></li>
						<li><a href="<%=path%>/classes.jsp"><img src="<%=path%>/asset/application/images/icon_weibo.png"
								class="icon" /> <fmt:message key="classes" /></a></li>
						<li class="divider"></li>
						<li><a href="<%=path%>/module/news/news.jsp?type=0"><img
								src="<%=path%>/asset/application/images/icon_news0.png" class="icon" /> <fmt:message key="news0"
									bundle="${news}" /></a></li>
						<li><a href="<%=path%>/module/news/news.jsp?type=3"><img
								src="<%=path%>/asset/application/images/icon_news3.png" class="icon" /> 名师课堂</a></li>
						<li><a href="<%=path%>/module/news/news.jsp?type=2"><img
								src="<%=path%>/asset/application/images/icon_news2.png" class="icon" /> <fmt:message key="news2"
									bundle="${news}" /></a></li>
						<li><a href="<%=path%>/module/news/news.jsp?type=1"><img
								src="<%=path%>/asset/application/images/icon_news1.png" class="icon" /> <fmt:message key="news1"
									bundle="${news}" /></a></li>
						<li class="divider"></li>
						<li><a href="<%=path%>/module/notice/notice.jsp"><img
								src="<%=path%>/asset/application/images/icon_cook.png" class="icon" /> <fmt:message key="notice" /></a></li>
						<li><a href="<%=path%>/module/weibo/weiboSchool.jsp"><img
								src="<%=path%>/asset/application/images/icon_weibo.png" class="icon" /> <fmt:message key="weiboSchool" /></a></li>
						<li style="display: none;"><a href="<%=path%>/module/broadcast/broadcastSchool.jsp"><img
								src="<%=path%>/asset/application/images/icon_broadcast.png" class="icon" /> <fmt:message key="broadcast.school" /></a></li>
						<li><a href="<%=path%>/module/broadcast/broadcastGrade.jsp"><img
								src="<%=path%>/asset/application/images/icon_broadcast.png" class="icon" /> <fmt:message key="broadcast.grade" /></a></li>
						<li class="divider"></li>
						<li><a href="<%=path%>/module/elective/elective.jsp"><img
								src="<%=path%>/asset/application/images/icon_curriculum.png" class="icon" /> <fmt:message key="elective" /></a></li>
						<li><a href="<%=path%>/module/cook/cook.jsp"><img src="<%=path%>/asset/application/images/icon_cook.png"
								class="icon" /> <fmt:message key="cook" /></a></li>

						<li class="divider"></li>
						<li><a href="<%=path%>/module/marquee/marquee.jsp"><img
								src="<%=path%>/asset/application/images/icon_cook.png" class="icon" /> <fmt:message key="marquee" /></a></li>
					</ul></li>
				<li class="dropdown" id="classes_menu"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><fmt:message
							key="classes" /> <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="<%=path%>/module/broadcast/broadcastClasses.jsp"><img
								src="<%=path%>/asset/application/images/icon_broadcast.png" class="icon" /> <fmt:message key="broadcast" /></a></li>
						<li><a href="<%=path%>/module/weibo/weiboClasses.jsp"><img
								src="<%=path%>/asset/application/images/icon_weibo.png" class="icon" /> <fmt:message key="weibo" /></a></li>
						<li><a href="<%=path%>/module/course/course_my.jsp"><img
								src="<%=path%>/asset/application/images/icon_weibo.png"
								class="icon" /> <fmt:message key="course.my" /></a></li>
						<%-- <li><a href="<%=path%>/module/homework/classes_homework_check.jsp"><img
								src="<%=path%>/asset/application/images/icon_weibo.png" class="icon" />
							<fmt:message key="homework" /></a></li> --%>
						<li class="divider"></li>
						<li><a href="<%=path%>/module/star/star.jsp"><img src="<%=path%>/asset/application/images/icon_star.png"
								class="icon" /> <fmt:message key="star" /></a></li>
						<li class="divider"></li>
						<li><a href="<%=path%>/module/security/security.jsp"><img
								src="<%=path%>/asset/application/images/icon_curriculum.png" class="icon" /> <fmt:message key="security" /></a></li>
						<li><a href="<%=path%>/module/download/download.jsp"><img
								src="<%=path%>/asset/application/images/icon_download.png" class="icon" /> <fmt:message key="download" /></a></li>
						<li class="divider"></li>
						<li><a href="<%=path%>/module/examine/examine.jsp"><img
								src="<%=path%>/asset/application/images/icon_curriculum.png" class="icon" /> 考试信息</a></li>
						<li class="divider"></li>
						<li><a href="<%=path%>/module/audit/audit.jsp"><img
								src="<%=path%>/asset/application/images/icon_person.png" class="icon" /> 家长审核</a></li>
					</ul></li>
			</ul>
			<select class="combobox" id="classesId" style="margin: 15px;"></select>
			<ul class="nav navbar-nav navbar-right">
				<li id="school"><a href="<%=path%>/school.jsp"><fmt:message key="school" /></a></li>
				<li id="halt"><a href="<%=path%>/halt.jsp"><fmt:message key="halt" /></a></li>
				<li id="sensitive"><a href="<%=path%>/sensitive.jsp"><fmt:message key="sensitive" /></a></li>
				<li class="dropdown" id="stats"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <fmt:message key="stats" />
						<b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="<%=path%>/module/stats/stats_school.jsp"><img src="<%=path%>/asset/application/images/icon_news0.png"
								class="icon" /> <fmt:message key="stats.school" /></a></li>
						<li><a href="<%=path%>/module/stats/stats_class.jsp"><img src="<%=path%>/asset/application/images/icon_news0.png"
								class="icon" /> <fmt:message key="stats.class" /></a></li>
						<li><a href="<%=path%>/module/stats/stats_login.jsp"><img src="<%=path%>/asset/application/images/icon_news0.png"
								class="icon" /> <fmt:message key="stats.login" /></a></li>
						<li><a href="<%=path%>/module/stats/pie_login.jsp"><img src="<%=path%>/asset/application/images/icon_news0.png"
								class="icon" /> <fmt:message key="stats.pie" /></a></li>
						</ul></li>
				<li class="dropdown" id="config"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><fmt:message key="config" />
						<b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="<%=path%>/global.jsp"><img src="<%=path%>/asset/application/images/icon_global.png"
								class="icon" /> <fmt:message key="global" /></a></li>
						<li><a href="<%=path%>/dictionary.jsp"><img src="<%=path%>/asset/application/images/icon_dictionary.png"
								class="icon" /> <fmt:message key="dictionary" /></a></li>
						<li style="display: none;"><a href="role.jsp"><fmt:message key="role" /></a></li>
						<li><a href="<%=path%>/template.jsp"><img src="<%=path%>/asset/application/images/icon_template.png"
								class="icon" /> <fmt:message key="template" /></a></li>
						<li><a href="<%=path%>/course.jsp"><img src="<%=path%>/asset/application/images/icon_classmate.png"
								class="icon" /> <fmt:message key="course" /></a></li>
						<li><a href="<%=path%>/multi.jsp"><img src="<%=path%>/asset/application/images/icon_multi.png"
								class="icon" /> <fmt:message key="multi" /></a></li>
						<li><a href="<%=path%>/module/contact/contact.jsp"><img
								src="<%=path%>/asset/application/images/icon_contact.png" class="icon" /> <fmt:message key="contact" /></a></li>
						<li><a href="<%=path%>/module/download/download_global.jsp">
							<img src="<%=path%>/asset/application/images/icon_download.png" class="icon" /><fmt:message key="downloadGlobal" /></a></li>
						<li><a href="<%=path%>/module/marquee/picture.jsp">
							<img src="<%=path%>/asset/application/images/icon_download.png" class="icon" />默认图片</a></li>
						<li><a href="<%=path%>/module/health/health_standard.jsp">
                            <img src="<%=path%>/asset/application/images/icon_login.png" class="icon" />健康标准</a></li>
					</ul></li>
				<li><a href="<%=path%>/password.jsp"><fmt:message key="welcome" /> <%=teacher.getName()%></a></li>
				<li><a href="javascript:quit();"><fmt:message key="quit" /></a></li>
			</ul>
		</nav>
	</div>
</header>

<div id="container" class="container">