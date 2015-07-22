package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.Session;
import com.cloopen.rest.sdk.CCPRestSDK;
import org.apache.commons.lang.StringUtils;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("verify")
@Produces(MediaType.APPLICATION_JSON)
@Description("验证手机")
public class VerifyResource extends AbstractResource {

    private static final String SERVER_IP = "app.cloopen.com";//sandboxapp.cloopen.com
    private static final String SERVER_PORT = "8883";

    private static final String ACCOUNT_SID = "aaf98f89499d24b50149ac4b43eb095b";
    private static final String AUTH_TOKEN = "2e8396b50b3346f6a32cea7e47c53964";

    private static final String APP_ID = "aaf98f894a038dc2014a08c861dc035d";

    private static final String VOICE_TIME = "2";

    private static final String VALID_TIME = "5";

    //key存放地区码，value存放配置项，依次为appId、registerTemplateId
    private static Map<String, String[]> configMap;

    static {
        configMap = new HashMap<>();
        //成都教育
        configMap.put("510100", new String[]{"8a48b5514de7ae4c014dec1fd515022d", "22707"});
        //新津教育
        configMap.put("510132", new String[]{"8a48b5514de7ae4c014dec1eee02022b", "22319"});
        //芦山教育
        configMap.put("511826", new String[]{"8a48b5514de7ae4c014dec1f3de8022c", "22320"});
    }

    public static boolean smsVerify(String region, String phone, String num) {
        HashMap<String, Object> result = null;

        if (StringUtils.isBlank(region) || configMap.get(region) == null) {
            region = "510100";//默认显示成都教育
        }

        CCPRestSDK restAPI = new CCPRestSDK();
        restAPI.init(SERVER_IP, SERVER_PORT);// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
        restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);// 初始化主帐号名称和主帐号令牌
        restAPI.setAppId(configMap.get(region)[0]);// 初始化应用ID
//        result = restAPI.voiceVerify(num, phone, null, VOICE_TIME, null);
//        result = restAPI.voiceVerify("验证码内容", "号码", "显示的号码", "3(播放次数)", "");

        result = restAPI.sendTemplateSMS(phone, configMap.get(region)[1], new String[]{num, VALID_TIME});

        System.out.println("SDKTestGetSubAccounts result=" + result);

        return "000000".equals(result.get("statusCode"));
/*        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
        }*/
    }


    public static boolean voiceVerify(String phone, String num) {
        HashMap<String, Object> result = null;

        CCPRestSDK restAPI = new CCPRestSDK();
        restAPI.init(SERVER_IP, SERVER_PORT);// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
        restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);// 初始化主帐号名称和主帐号令牌
        restAPI.setAppId(APP_ID);// 初始化应用ID
        result = restAPI.voiceVerify(num, phone, null, VOICE_TIME, null);
//        result = restAPI.voiceVerify("验证码内容", "号码", "显示的号码", "3(播放次数)", "");
        System.out.println("SDKTestGetSubAccounts result=" + result);

        return "000000".equals(result.get("statusCode"));
/*        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
        }*/
    }

    /**
     * 校验验证码与手机号是否相符合
     *
     * @param mobile     手机号
     * @param verifyCode 验证码
     * @return
     */
    public static Boolean verify(String mobile, String verifyCode) {
        try {
            String str = (String) Session.getMemcachedClient().get(mobile);
            return verifyCode.equals(str);
        } catch (Exception e) {

        }
        return false;
    }

    @POST
    @Path("{mobile}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加")
    public Boolean insert(@CookieParam("rsessionid") String rsessionid,
                          @CookieParam("code") String code,
                          @FormParam("region") String region,
                          @PathParam("mobile") String mobile)
            throws Exception {
        //校验code,非web端
        if (ClientResource.clients.containsKey(code)
                && !ClientResource.clients.get(code).toLowerCase().equals("web")) {
            Double d = Math.random() * 10000;
            String str = String.valueOf(d.intValue());

            //发送验证码
            if (smsVerify(region, mobile, str)) {
                //存储验证码
                Session.getMemcachedClient().set(mobile, 5 * 60, str);
                return true;
            }
        }

        return false;
    }


    public static void main(String[] args) {
        Double d = Math.random() * 10000;
        String s = String.valueOf(d.intValue());
        System.out.println(s);
    }
}
