import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpUtils {

	public static class Response {
		public Response(int code, String body) {
			this.code = code;
			this.body = body;
		}

		public int code;
		public String body;
	}

	public static Response get(String url, String sessionid, String... params) {
		GetMethod get = new GetMethod(url);
		if (sessionid != null) {
			get.addRequestHeader("Cookie", "rsessionid=" + sessionid);
		}
		if (params != null) {
			NameValuePair[] pairs = new NameValuePair[params.length / 2];
			for (int i = 0; i < params.length; i += 2) {
				pairs[i / 2] = new NameValuePair(params[i], params[i + 1]);
			}
			get.setQueryString(pairs);
		}
		HttpClient client = new HttpClient();
		try {
			return new Response(client.executeMethod(get), get.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			get.releaseConnection();
		}
	}

	public static Response post(String url, String sessionid, Map<String, List<File>> fileMap, String... params) {
		PostMethod post = new PostMethod(url);
		if (sessionid != null) {
			post.addRequestHeader("Cookie", "rsessionid=" + sessionid);
		}

		HttpClient client = new HttpClient();
		try {
			post.setRequestEntity(entity(fileMap, post.getParams(), params));
			return new Response(client.executeMethod(post), post.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			post.releaseConnection();
		}
	}

	public static Response put(String url, String sessionid, Map<String, List<File>> fileMap, String... params) {
		PutMethod put = new PutMethod(url);
		if (sessionid != null) {
			put.addRequestHeader("Cookie", "rsessionid=" + sessionid);
		}

		HttpClient client = new HttpClient();
		try {
			put.setRequestEntity(entity(fileMap, put.getParams(), params));
			return new Response(client.executeMethod(put), put.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			put.releaseConnection();
		}
	}

	public static Response delete(String url, String sessionid, String... params) {
		DeleteMethod delete = new DeleteMethod(url);
		if (sessionid != null) {
			delete.addRequestHeader("Cookie", "rsessionid=" + sessionid);
		}
		if (params != null) {
			NameValuePair[] pairs = new NameValuePair[params.length / 2];
			for (int i = 0; i < params.length; i += 2) {
				pairs[i / 2] = new NameValuePair(params[i], params[i + 1]);
			}
			delete.setQueryString(pairs);
		}

		HttpClient client = new HttpClient();
		try {
			return new Response(client.executeMethod(delete), delete.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			delete.releaseConnection();
		}
	}

	private static RequestEntity entity(Map<String, List<File>> fileMap, HttpMethodParams methodParams,
			String... params) {
		if (fileMap == null) {
			List<Part> partList = new ArrayList<Part>();
			if (params != null) {
				for (int i = 0; i < params.length; i += 2) {
					if (params[i + 1] != null) {
						partList.add(new StringPart(params[i], params[i + 1], "utf-8"));
					}
				}
			}

			Part[] parts = new Part[partList.size()];
			partList.toArray(parts);
			return new MultipartRequestEntity(parts, methodParams);
		} else {
			List<Part> partList = new ArrayList<Part>();
			if (params != null) {
				for (int i = 0; i < params.length; i += 2) {
					if (params[i + 1] != null) {
						partList.add(new StringPart(params[i], params[i + 1], "utf-8"));
					}
				}
			}
			if (fileMap != null) {
				for (Map.Entry<String, List<File>> entry : fileMap.entrySet()) {
					String fileName = entry.getKey();
					List<File> fileList = entry.getValue();
					if (fileList != null) {
						for (File file : fileList) {
							try {
								partList.add(new FilePart(fileName, file));
							} catch (FileNotFoundException e) {
							}
						}
					}
				}
			}
			Part[] parts = new Part[partList.size()];
			partList.toArray(parts);
			return new MultipartRequestEntity(parts, methodParams);
		}
	}
}
