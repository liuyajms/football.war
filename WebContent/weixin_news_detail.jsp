<%@page import="java.io.InputStream"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.apache.commons.httpclient.HttpClient"%>
<%@page import="org.apache.commons.httpclient.methods.PostMethod"%>
<%@page import="org.apache.commons.httpclient.methods.GetMethod"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta name="viewport" content="width=device-width,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<meta name="format-detection" content="telephone=no" />
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
</head>
<body>
	<%
		PostMethod post = new PostMethod("http://125.70.9.211:6080/child/rest/auth/anonymous");
		post.addParameter("schoolId", "1176");

		HttpClient client = new HttpClient();
		client.executeMethod(post);

		String postString = post.getResponseBodyAsString();
		JSONObject postObject = new JSONObject(postString);
		String rsessionid = postObject.getString("rsessionid");
		System.out.println(rsessionid);

		GetMethod get = new GetMethod("http://125.70.9.211:6080/child/rest/news/" + request.getParameter("id"));
		get.addRequestHeader("Cookie", "rsessionid=" + rsessionid);
		client.executeMethod(get);

		InputStream is = get.getResponseBodyAsStream();
		if (is != null) {
			String s = IOUtils.toString(is, "utf-8");

			System.out.println(s);
			JSONObject object = new JSONObject(s);
			out.println("<h3>" + object.getString("title") + "</h3>");

			String time = new SimpleDateFormat("<fmt:message key="weixin.dateformat" />").format(new Date(object.getLong("createTime")));
			out.println("<h4 style=\"color:#666;\">" + time + "</h4>");
			if (object.getBoolean("pic")) {
				out.println("<div style=\"height:200px;overflow:hidden;\"><img style=\"width:100%;\" src=\"http://125.70.9.211:6080/child/file/1176/news/" + object.getString("id")
						+ ".png\" /></div>");
			}
			out.println("<div>" + object.getString("description") + "</div>");
		}
	%>
</body>
</html>