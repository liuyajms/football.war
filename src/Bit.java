import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class Bit {

	public static void main(String args[]) throws Exception {
		for (int p = 1; p <= 10; p++) {
			GetMethod getMethod = new GetMethod(
					"http://www.sczfcg.com/CmsNewsController.do?method=recommendBulletinList&moreType=provincebuyBulletinMore&channelCode=cggg&rp=25&page="
							+ p);
			HttpClient client = new HttpClient();
			int result = client.executeMethod(getMethod);

			String response = getMethod.getResponseBodyAsString();
			String[] ss = response.split("<a href=\"/view/staticpags/gkzbcggg/");
			for (int i = 1; i < ss.length; i++) {
				String a = ss[i].substring(0, ss[i].indexOf("</a>"));
				String html = a.substring(0, a.indexOf("\""));
				String name = a.substring(a.indexOf("target=\"_blank\">") + "target=\"_blank\">".length());
				if (name.contains("软件")) {
					System.out.println("http://www.sczfcg.com/view/staticpags/gkzbcggg/" + html + " : " + name);
				}
			}
		}
	}
}
