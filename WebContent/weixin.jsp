<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="org.apache.commons.httpclient.HttpClient"%>
<%@page import="org.apache.commons.httpclient.methods.PostMethod"%>
<%@page import="org.apache.commons.httpclient.methods.GetMethod"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.xml.transform.stream.StreamResult"%>
<%@page import="java.io.StringWriter"%>
<%@page import="javax.xml.transform.dom.DOMSource"%>
<%@page import="javax.xml.transform.Transformer"%>
<%@page import="javax.xml.transform.TransformerFactory"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="javax.xml.parsers.DocumentBuilder"%>
<%@page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="java.io.IOException"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.security.MessageDigest"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%!private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	public static String SHA1(String inStr) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance("SHA1");
			byte[] digest = md.digest(inStr.getBytes());
			outStr = bytetoString(digest);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return outStr;
	}

	public static String bytetoString(byte[] digest) {
		int len = digest.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(digest[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[digest[j] & 0x0f]);
		}
		return buf.toString();

	}

	public String checkJoin(HttpServletRequest request) throws Exception {
		System.out.println("checkJoin");
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(request.getInputStream());

		String fromUsername = document.getElementsByTagName("FromUserName").item(0).getTextContent();
		String toUsername = document.getElementsByTagName("ToUserName").item(0).getTextContent();
		String keyword = document.getElementsByTagName("Content").item(0).getTextContent();
		String msgId = document.getElementsByTagName("MsgId").item(0).getTextContent();
		String time = new Date().getTime() + "";
		String textTpl = "<xml>" + "<ToUserName><![CDATA[%1$s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%2$s]]></FromUserName>" + "<CreateTime>%3$s</CreateTime>"
				+ "<MsgType><![CDATA[%4$s]]></MsgType>" + "<Content><![CDATA[%5$s]]></Content>"
				+ "<FuncFlag>0</FuncFlag>" + "</xml>";

		System.out.println(keyword);
		if (null != keyword && !keyword.equals("")) {
			String msgType = "text";
			String contentStr = keyword + " <fmt:message key="weixin.str" />";

			String resultStr = String.format(textTpl, fromUsername, toUsername, time, msgType, contentStr);

			/* 				weixin.setFromUserName(fromUsername);
			 weixin.setCreateTime(time);
			 weixin.setContent(contentStr);
			 weixin.setMsgType(msgType);
			 weixin.setToUserName(toUsername);
			 weixin.setMsgId(msgId);
			 */
			return resultStr;
		} else {
			return "";
		}

	}

	public static class News {
		private String title;
		private String description;
		private String picUrl;
		private String url;

		public News(String title, String description, String picUrl, String url) {
			this.title = title;
			this.description = description;
			this.picUrl = picUrl;
			this.url = url;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

	public static String replySubscribe(String to, String from) throws Exception {

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.newDocument();

		Element root = document.createElement("xml");
		document.appendChild(root);

		Element toUserName = document.createElement("ToUserName");
		toUserName.setTextContent(to);
		root.appendChild(toUserName);

		Element fromUserName = document.createElement("FromUserName");
		fromUserName.setTextContent(from);
		root.appendChild(fromUserName);

		Element createTime = document.createElement("CreateTime");
		createTime.setTextContent(Long.toString(new Date().getTime()));
		root.appendChild(createTime);

		Element msgType = document.createElement("MsgType");
		msgType.setTextContent("text");
		root.appendChild(msgType);

		Element content = document.createElement("Content");
		content.setTextContent("<fmt:message key="weixin.welcome" />");
		root.appendChild(content);

		StringWriter sw = new StringWriter();

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(document), new StreamResult(sw));

		return sw.toString();
	}

	public static String replyNews(String to, String from, List<News> newsList) throws Exception {

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.newDocument();

		Element root = document.createElement("xml");
		document.appendChild(root);

		Element toUserName = document.createElement("ToUserName");
		toUserName.setTextContent(to);
		root.appendChild(toUserName);

		Element fromUserName = document.createElement("FromUserName");
		fromUserName.setTextContent(from);
		root.appendChild(fromUserName);

		Element createTime = document.createElement("CreateTime");
		createTime.setTextContent(Long.toString(new Date().getTime()));
		root.appendChild(createTime);

		Element msgType = document.createElement("MsgType");
		msgType.setTextContent("news");
		root.appendChild(msgType);

		Element articleCount = document.createElement("ArticleCount");
		articleCount.setTextContent(Integer.toString(newsList.size()));
		root.appendChild(articleCount);

		Element articles = document.createElement("Articles");
		root.appendChild(articles);

		for (News news : newsList) {
			Element item = document.createElement("item");
			articles.appendChild(item);

			Element title = document.createElement("Title");
			title.setTextContent(news.getTitle());
			item.appendChild(title);

			Element description = document.createElement("Description");
			description.setTextContent(news.getDescription());
			item.appendChild(description);

			Element picUrl = document.createElement("PicUrl");
			picUrl.setTextContent(news.getPicUrl());
			item.appendChild(picUrl);

			Element url = document.createElement("Url");
			url.setTextContent(news.getUrl());
			item.appendChild(url);
		}

		StringWriter sw = new StringWriter();

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(document), new StreamResult(sw));

		return sw.toString();
	}%>
<%
	String signature = request.getParameter("signature");
	String timestamp = request.getParameter("timestamp");
	String nonce = request.getParameter("nonce");

	String TOKEN = "123456789";

	java.util.Set<String> set = new java.util.TreeSet<String>();
	set.add(TOKEN);
	set.add(timestamp);
	set.add(nonce);

	System.out.println(set);
	StringBuilder sb = new StringBuilder();
	for (String s : set) {
		sb.append(s);
	}
	System.out.println(sb.toString());
	System.out.println(SHA1(sb.toString()));
	System.out.println(signature);
	System.out.println(request.getParameter("echostr"));

	if (SHA1(sb.toString()).equals(signature)) {
		out.clear();
		if (request.getParameter("echostr") != null) {
			out.println(request.getParameter("echostr"));
			return;
		} else {
			try {
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document document = builder.parse(request.getInputStream());

				String fromUserName = document.getElementsByTagName("FromUserName").item(0).getTextContent();
				String toUserName = document.getElementsByTagName("ToUserName").item(0).getTextContent();
				String msgType = document.getElementsByTagName("MsgType").item(0).getTextContent();
				if ("event".equals(msgType)) {
					String event = document.getElementsByTagName("Event").item(0).getTextContent();
					if ("CLICK".equals(event)) {
						String eventKey = document.getElementsByTagName("EventKey").item(0).getTextContent();

						PostMethod post = new PostMethod("http://125.70.9.211:6080/child/rest/auth/anonymous");
						post.addParameter("schoolId", "1176");

						HttpClient client = new HttpClient();
						client.executeMethod(post);

						String postString = post.getResponseBodyAsString();
						JSONObject postObject = new JSONObject(postString);
						String rsessionid = postObject.getString("rsessionid");
						System.out.println(rsessionid);

						int type = 0;
						if ("news0".equals(eventKey)) {
							type = 0;
						} else if ("news1".equals(eventKey)) {
							type = 1;
						} else if ("news2".equals(eventKey)) {
							type = 2;
						} else if ("news3".equals(eventKey)) {
							type = 3;
						}
						GetMethod get = new GetMethod("http://125.70.9.211:6080/child/rest/news?type=" + type
								+ "&page=0&rows=10");
						get.addRequestHeader("Cookie", "rsessionid=" + rsessionid);
						client.executeMethod(get);

						List<News> newsList = new ArrayList<News>();

						String s = IOUtils.toString(get.getResponseBodyAsStream(), "utf-8");

						System.out.println(s);
						JSONArray array = new JSONArray(s);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.getJSONObject(i);
							newsList.add(new News(object.getString("title"), object
									.getString("descriptionSummary"),
									"http://125.70.9.211:6080/child/file/1176/news/" + object.getString("id")
											+ ".png", "http://www.whatsoa.com/child/weixin_news_detail.jsp?id="
											+ object.getString("id")));
						}

						System.out.println(eventKey);
						System.out.println(fromUserName);
						System.out.println(toUserName);

						out.println(replyNews(fromUserName, toUserName, newsList));
					} else if ("subscribe".equals(event)) {
						out.println(replySubscribe(fromUserName, toUserName));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}
%>