package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.Session;
import cn.com.weixunyun.child.model.bean.RolePopedom;
import cn.com.weixunyun.child.model.bean.User;
import cn.com.weixunyun.child.model.pojo.Global;
import cn.com.weixunyun.child.model.pojo.Menu;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.*;
import cn.com.weixunyun.child.model.vo.PlayerVO;
import cn.com.weixunyun.child.util.ValidateImageUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wink.common.annotations.Workspace;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Description("鉴权")
public class AuthResource extends AbstractResource {

    @GET
    @Deprecated
    @Description("预登陆")
    public List<User> select(@CookieParam("rsessionid") String rsessionid,
                             @CookieParam("code") String code,
                             @Context HttpServletRequest request) {
        /*return super.getService(UserService.class).login(username, DigestUtils.md5Hex(password));*/
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String platform = ClientResource.clients.get(code);

            if (platform != null && platform.equals("web")) {//判断是否web端
                if (rsessionid == null || "".equals(rsessionid.trim())) {//是否已传值
                    return null;
                } else {//判断验证码是否正确
                    String imgCode = Session.getInstance(rsessionid).get("imgCode");
                    if (!request.getParameter("imgCode").toUpperCase().equals(imgCode)) {
                        return null;
                    }
                }
            }

            UserService service = super.getService(UserService.class);
            List<User> list = service.login(username, DigestUtils.md5Hex(password));

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @POST
    @Description("登录")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Map<String, Object> insert(@CookieParam("rsessionid") String rsessionid,
                                      @Context HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return null;
        }

        PlayerVO playerVO = super.getService(PlayerService.class)
                .login(username, DigestUtils.md5Hex(password));

        if (playerVO == null || playerVO.getId() == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("player", playerVO);

            GlobalService globalService = super.getService(GlobalService.class);
            Global assetVersionGlobal = globalService.select("asset", "version");
            String assetVersion = assetVersionGlobal == null ? null : assetVersionGlobal.getValue();

            Map<String, Object> globalMap = new HashMap<String, Object>();
            globalMap.put("asset.version", assetVersion);

            if (rsessionid == null) {
                rsessionid = UUID.randomUUID().toString();
            }
            map.put("rsessionid", rsessionid);
            map.put("global", globalMap);

            HashMap<String, Integer> popedomMap = null;

            Session.getInstance(rsessionid).set("authed", true).set("player", playerVO).set("popedom", popedomMap);

            return map;
        }
    }


    @POST
    @Path("anonymous")
    @Description("游客登录")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Map<String, Object> insertAnonymous(MultivaluedMap<String, String> form) {
        Long schoolId = Long.parseLong(form.getFirst("schoolId"));

        Map<String, Object> map = new HashMap<String, Object>();

        GlobalService globalService = super.getService(GlobalService.class);
        Global assetVersionGlobal = globalService.select("asset", "version");
        String assetVersion = assetVersionGlobal == null ? null : assetVersionGlobal.getValue();

        Map<String, Object> globalMap = new HashMap<String, Object>();
        globalMap.put("asset.version", assetVersion);

        String rsessionid = UUID.randomUUID().toString();
        map.put("rsessionid", rsessionid);
        map.put("global", globalMap);

        Session.getInstance(rsessionid).set("authed", true);

        return map;
    }

    private HashMap<String, Integer> popedom(long roleId) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        RoleService roleService = super.getService(RoleService.class);
        List<RolePopedom> popedomList = roleService.getPopedomList(roleId);
        for (RolePopedom popedom : popedomList) {
            map.put(popedom.getCode(), popedom.getAction());
        }
        return map;
    }

    private HashMap<Menu, List<Menu>> treeMenu(List<Menu> menuList) {
        List<Menu> parentMenuList = new ArrayList<Menu>();
        HashMap<Long, List<Menu>> menuLongMap = new HashMap<Long, List<Menu>>();

        for (Menu menu : menuList) {
            if (menu.getIdParent() == null) {
                parentMenuList.add(menu);
            } else {
                List<Menu> list = menuLongMap.get(menu.getIdParent());
                if (list == null) {
                    list = new ArrayList<Menu>();
                    menuLongMap.put(menu.getIdParent(), list);
                }
                list.add(menu);
            }
        }

        HashMap<Menu, List<Menu>> menuMap = new LinkedHashMap<Menu, List<Menu>>();
        for (Menu menu : parentMenuList) {
            menuMap.put(menu, menuLongMap.get(menu.getId()));
        }
        return menuMap;
    }

    @DELETE
    @Description("登出")
    public void delete(@CookieParam("rsessionid") String rsessionid) {
        Session.getInstance(rsessionid).destroy();
    }


    @GET
    @Path("imgCode")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Description("获取图片验证码")
    public Response imgCode(@CookieParam("rsessionid") String rsessionid) {
        final String str = cn.com.weixunyun.child.util.ValidateImageUtil.getRandomString();
        Session.getInstance(rsessionid).set("imgCode", str);

        final StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                BufferedImage bi = ValidateImageUtil.getBufferedImage(str);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
                encoder.encode(bi); //Encode a BufferedImage as a JPEG data stream.
            }
        };
        return Response.ok(stream, "image/jpeg").build();
    }

}
