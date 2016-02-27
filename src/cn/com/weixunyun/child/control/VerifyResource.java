package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.Session;
import cn.com.weixunyun.child.util.DateUtil;
import com.cloopen.rest.sdk.CCPRestSDK;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("verify")
@Produces(MediaType.APPLICATION_JSON)
@Description("验证手机")
public class VerifyResource extends AbstractResource {

    private static final String SERVER_IP = "app.cloopen.com";//sandboxapp.cloopen.com
    private static final String SERVER_PORT = "8883";

    private static final String ACCOUNT_SID = "aaf98f894fd44d15014fdb5bc19c08db";
    private static final String AUTH_TOKEN = "05bbb39a02bf45f7b35edee0d8284393";

    private static final String APP_ID = "8a48b5514fd49643014fdb6026271608";

    private static final String VOICE_TIME = "2";

    private static final String VALID_TIME = "5";

    private static final String REGISTER_TEMPLATE_Id = "37914";
    private static final String FINDPASSWORD_TEMPLATE_Id = "37933";


    public static boolean smsVerify(String templateId, String phone, String num) {
        HashMap<String, Object> result = null;

        CCPRestSDK restAPI = new CCPRestSDK();
        restAPI.init(SERVER_IP, SERVER_PORT);// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
        restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);// 初始化主帐号名称和主帐号令牌
        restAPI.setAppId(APP_ID);// 初始化应用ID
//        result = restAPI.voiceVerify(num, phone, null, VOICE_TIME, null);
//        result = restAPI.voiceVerify("验证码内容", "号码", "显示的号码", "3(播放次数)", "");

        result = restAPI.sendTemplateSMS(phone, templateId, new String[]{num, VALID_TIME});

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
    @Path("{mobile}/register")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加")
    public Boolean insert(@CookieParam("code") String code,
                          @PathParam("mobile") String mobile)
            throws Exception {
        //校验code,非web端
      /*  if (ClientResource.clients.containsKey(code)
                && !ClientResource.clients.get(code).toLowerCase().equals("web")) {
            Double d = Math.random() * 10000;
            String str = String.valueOf(d.intValue());

            //发送验证码
            if (smsVerify(REGISTER_TEMPLATE_Id, mobile, str)) {
                //存储验证码
                Session.getMemcachedClient().set(mobile, 5 * 60, str);
                return true;
            }
        }*/

        return send(REGISTER_TEMPLATE_Id, mobile);

    }


    @POST
    @Path("{mobile}/findPassword")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加")
    public Boolean findPassword(@CookieParam("code") String code,
                                @PathParam("mobile") String mobile)
            throws Exception {

        return send(FINDPASSWORD_TEMPLATE_Id, mobile);
    }

    private boolean send(String templateId, String mobile) throws TimeoutException, InterruptedException, MemcachedException {
//        Double d = Math.random() * 10000;
//        String str = String.valueOf(d.intValue());
        String str = DateUtil.getRandomStr(4);
        //发送验证码
        if (smsVerify(templateId, mobile, str)) {
            //存储验证码
            Session.getMemcachedClient().set(mobile, 5 * 60, str);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Double d = Math.random() * 10000;
        String s = String.valueOf(d.intValue());
        System.out.println(s);
    }
}
