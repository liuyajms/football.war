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
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>微讯云通</title>
    <meta content="index,follow" name="Robots"/>
    <meta name="keywords" content="微讯云通"/>
    <meta name="description" content="微讯云通"/>
    <link href="<%=path%>/asset/header/css/base.css" rel="stylesheet" type="text/css"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
</head>
<body>
<!--hearder-->
<div class="hearder" style="box-sizing:content-box;-webkit-box-sizing:content-box">
    <div class="w">
        <a href="/" class="logo"></a>

        <div class="nav">
            <ul>
                <li><a href="/index.html">首页</a><span class="arrow"></span></li>
                <li><a href="/269.html">解决方案</a><span class="arrow"></span></li>
                <li class="on"><a href="#">服务</a><span class="arrow"></span></li>
                <li><a href="/271.html">合作</a><span class="arrow"></span></li>
            </ul>
        </div>
    </div>
</div>
<!--hearder end-->

<!--body-->
<div class="loc">
    <div class="w" style="box-sizing:content-box;-webkit-box-sizing:content-box">

        <h6><img src="<%=path%>/school/<%=school.getId()%>/logo.png" class="school-logo"
                 onerror='javascript:imgError()'/>
            <a href="<%=path%>/index.jsp"><%=school.getName()%></a>
        </h6>

        <div class="school-info" style="box-sizing:content-box;-webkit-box-sizing:content-box">
            <span class="school-name s3"><select class="combobox" id="classesId"></select></span>

            <!-- 设置栏目开始      -->
            <span class="user-info">

	             <span class="dropdown" id="stats" ><a href="#" class="dropdown-toggle s5" data-toggle="dropdown"> <fmt:message key="stats" />
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
                        </ul>
				</span>
            	<span class="dropdown" id="config"><a href="#" class="dropdown-toggle s5" data-toggle="dropdown"><fmt:message key="config" />
                    <b class="caret"></b></a>
					<ul class="dropdown-menu" style="left:auto;">
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
                        <li><a href="<%=path%>/module/health/health_standard.jsp">
                            <img src="<%=path%>/asset/application/images/icon_login.png" class="icon" />健康标准</a></li>
                    </ul>
				</span>
            	<a href="<%=path%>/password.jsp" class="s3"><%=teacher.getName()%></a>
            	<span class="line" >|</span>
            	<a href="javascript:quit();" class="s4"><fmt:message key="quit" /></a>
            </span>

            <!-- 设置栏目结束 -->
        </div>
    </div>
</div>

<div class="w">
    <div class="school-sidebar">

        <!-- 学校菜单项开始 -->
        <div id="menu">
            <ul class="school-nav">
                <li><a href="<%=path%>/teacher.jsp"><fmt:message key="teacher" /></a></li>
                <li><a href="<%=path%>/classes.jsp"><fmt:message key="classes" /></a></li>
            </ul>
            <ul class="school-nav">
                <!-- class="on" -->
                <li ><a href="<%=path%>/module/news/news.jsp?type=0">
                    <fmt:message key="news0" bundle="${news}" /></a></li>
                <li><a href="<%=path%>/module/news/news.jsp?type=3">
                    <fmt:message key="news3" bundle="${news}" /></a></li>
                <li><a href="<%=path%>/module/news/news.jsp?type=2">
                    <fmt:message key="news2" bundle="${news}" /></a></li>
                <li><a href="<%=path%>/module/news/news.jsp?type=1">
                    <fmt:message key="news1" bundle="${news}" /></a></li>
            </ul>
            <ul class="school-nav">
                <li><a href="<%=path%>/module/notice/notice.jsp"><fmt:message key="notice" /></a></li>
                <li><a href="<%=path%>/module/weibo/weiboSchool.jsp"><fmt:message key="weiboSchool" /></a></li>
                <li><a href="<%=path%>/module/broadcast/broadcastGrade.jsp"><fmt:message key="broadcast.grade" /></a></li>
            </ul>
            <ul class="school-nav">
                <li><a href="<%=path%>/module/elective/elective.jsp"><fmt:message key="elective" /></a></li>
                <li><a href="<%=path%>/module/cook/cook.jsp"><fmt:message key="cook" /></a></li>
            </ul>
            <ul class="school-nav">
                <li><a href="<%=path%>/module/marquee/marquee.jsp"><fmt:message key="marquee" /></a></li>
                <li><a href="<%=path%>/module/marquee/picture.jsp">新闻默认图片</a></li>
            </ul>
        </div>
        <!-- 学校菜单项结束 -->

        <!-- 班级菜单项开始 -->
        <div id="classes_menu">
            <ul class="school-nav">
                <li><a href="<%=path%>/module/broadcast/broadcastClasses.jsp"> <fmt:message key="broadcast" /></a></li>
                <li><a href="<%=path%>/module/weibo/weiboClasses.jsp"> <fmt:message key="weibo" /></a></li>
                <li><a href="<%=path%>/module/course/course_my.jsp"> <fmt:message key="course.my" /></a></li>
                <li><a href="<%=path%>/module/homework/classes_homework_check.jsp">
                    <fmt:message key="homework" /></a></li>
                <li><a href="<%=path%>/module/examine/examine.jsp">考试</a></li>
            </ul>
            <ul class="school-nav">
                <li><a href="<%=path%>/module/star/star.jsp"> <fmt:message key="star" /></a></li>
            </ul>
            <ul class="school-nav">
                <li><a href="<%=path%>/module/security/security.jsp"> <fmt:message key="security" /></a></li>
                <li><a href="<%=path%>/module/download/download.jsp"> <fmt:message key="download" /></a></li>
            </ul>
        </div>
        <!-- 班级结束 -->
        <div id="school" >
            <ul class="school-nav">
                <li><a href="<%=path%>/module/education/education.jsp">教育局</a></li>
                <li><a href="<%=path%>/school.jsp"><fmt:message key="school" /></a></li>
            </ul>
        </div>
        <div id="halt" >
            <ul class="school-nav">
                <li><a href="<%=path%>/halt.jsp"><fmt:message key="halt" /></a></li>
            </ul>
        </div>
        <div id="sensitive"  >
            <ul class="school-nav">
                <li><a href="<%=path%>/sensitive.jsp"><fmt:message key="sensitive" /></a></li>
            </ul>
        </div>
       <%-- <span id = "chengdu">
       <div id="sensitive"  >
           <ul class="school-nav">
               <li><a href="<%=path%>/module/menu/menu.jsp">菜单管理</a></li>
           </ul>
       </div>
       </span> --%>
       <span id = "chengdu">
       <div id="sensitive"  >
           <ul class="school-nav">
               <li><a href="<%=path%>/module/question/question.jsp">留言咨询</a></li>
           </ul>
       </div>
       </span>
    </div>

    <div class="school-con">
