package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.ResultEntity;
import cn.com.weixunyun.child.UserType;
import cn.com.weixunyun.child.model.bean.Player;
import cn.com.weixunyun.child.model.service.FriendService;
import cn.com.weixunyun.child.model.service.PlayerService;
import cn.com.weixunyun.child.model.vo.PlayerVO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.wink.common.annotations.Workspace;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/player")
@Produces(MediaType.APPLICATION_JSON)
@Description("球员")
public class PlayerResource extends AbstractResource {

    @Autowired
    private PlayerService service;

    @GET
    @Description("列表")
    public List<PlayerVO> getList(@CookieParam("rsessionid") String rsessionid,
                                  @QueryParam("city") String city,
                                  @QueryParam("role") Integer role,
                                  @QueryParam("beginAge") Integer beginAge,
                                  @QueryParam("endAge") Integer endAge,
                                  @QueryParam("keyword") String keyword,
                                  @QueryParam("px") Double px, @QueryParam("py") Double py,
                                  @QueryParam("page") long page, @QueryParam("rows") long rows) {
        return super.getService(PlayerService.class)
                .getList(city, role, beginAge, endAge, keyword, px, py, rows, page * rows);
    }

    @GET
    @Path("{id}")
    @Description("详情")
    public PlayerVO select(@CookieParam("rsessionid") String rsessionid, @PathParam("id") long id) {

        PlayerVO playerVO = service.get(id);
        //判断当前登录人是否与查询的球员为同一人
        Long myId = super.getAuthedId(rsessionid);
        if (myId != id) {
            //判断是否为好友
            playerVO.setIsFriend(super.getService(FriendService.class).isFriend(myId, id) > 0);
        }
        return playerVO;
    }


    /**
     * @param map 手机号/密码等信息必填
     * @return
     * @throws Exception
     */
    @POST
    @Path("register")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("注册添加用户")
    public ResultEntity register(MultivaluedMap<String, String> map) throws Exception {

        Player player = super.buildBean(Player.class, map, null);

        if (StringUtils.isBlank(player.getMobile())) {
            return new ResultEntity(HttpStatus.SC_EXPECTATION_FAILED, "手机号为空");
        }
        if (StringUtils.isBlank(player.getPassword())) {
            return new ResultEntity(HttpStatus.SC_EXPECTATION_FAILED, "密码为空");
        }

//        String verifyCode = map.getFirst("verifyCode");
//        if (!VerifyResource.verify(player.getMobile(), verifyCode)) {
//            return new ResultEntity(HttpStatus.SC_BAD_REQUEST, "验证码错误");
//        }

        if (StringUtils.isBlank(player.getName())) {
            player.setName(player.getMobile());
        }

        player.setPassword(DigestUtils.md5Hex(map.getFirst("password")));
        player.setEnabled(true);

        PlayerService service = super.getService(PlayerService.class);

        if (service.login(player.getMobile(), null) != null) {
            return new ResultEntity(HttpStatus.SC_EXPECTATION_FAILED, "该手机号已被注册");
        }

        service.insert(player);

        return new ResultEntity(HttpStatus.SC_OK, "注册成功!");
    }

    @Deprecated
    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("添加")
    public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
            throws Exception {
        Map<String, PartField> map = super.partMulti(request);

        Integer role = null;
        if (map.containsKey("role")) {
            String roleValue = map.get("role").getValue();
            if (roleValue != null && !roleValue.equals("")) {
                role = 0;
                for (String str : roleValue.split(",")) {
                    role += Integer.parseInt(str);
                }
                map.remove("role");
            }
        }

        Player player = super.buildBean(Player.class, map, null);

        if (map.get("type") == null) {
            player.setType(UserType.USER);
        }

        player.setRole(role);
        player.setEnabled(true);
        player.setPassword(DigestUtils.md5Hex(map.get("password").getValue()));

        updateImage(map, player.getId());

        super.getService(PlayerService.class).insert(player);

    }


    @PUT
    @Path("{id}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("修改")
    public void update(@Context HttpServletRequest request, @PathParam("id") Long id,
                       @CookieParam("rsessionid") String rsessionid) throws Exception {
        Map<String, PartField> map = super.partMulti(request);

        //处理role场上位置
        Integer role = null;
        if (map.containsKey("role")) {
            String roleValue = map.get("role").getValue();
            if (roleValue != null && !roleValue.equals("")) {
                role = 0;
                for (String str : roleValue.split(",")) {
                    role += Integer.parseInt(str);
                }
                map.remove("role");
            }
        }

        Player player = super.buildBean(Player.class, map, id);
        player.setRole(role);

        if (map.get("type") == null) {
            player.setType(UserType.USER);
        }

        updateImage(map, player.getId());

        super.getService(PlayerService.class).update(player);

    }

    @PUT
    @Path("{id}/image")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("修改头像")
    public void updateImage(@Context HttpServletRequest request, @PathParam("id") Long id,
                            @CookieParam("rsessionid") String rsessionid) throws Exception {
        updateImage(super.partMulti(request), id);
    }

    @PUT
    @Path("{id}/password")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("修改密码")
    public Boolean password(MultivaluedMap<String, String> form, @PathParam("id") Long id) {
        Map<String, PartField> map = super.part(form);
        String passwordOld = map.get("passwordOld").getValue();
        String passwordNew = map.get("passwordNew").getValue();

        PlayerService service = super.getService(PlayerService.class);
        PlayerVO player = service.get(id);

        if (player != null && player.getPassword().equals(DigestUtils.md5Hex(passwordOld))) {
            int n = service.updateInfo(id, DigestUtils.md5Hex(passwordNew));
            return true;
        } else {
            return false;
        }

    }

    @PUT
    @Path("{id}/username")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("修改帐号")
    public void username(MultivaluedMap<String, String> form, @PathParam("id") Long id,
                         @CookieParam("rsessionid") String rsessionid) {
       /* Map<String, PartField> map = super.part(form);
        String username = map.get("username").getValue();

        PlayerService teacherService = super.getService(PlayerService.class);
        School school = super.getAuthedSchool(rsessionid);
        Teacher teacher = teacherService.selectTeacherMobile(school.getId(), username);

        if (teacher == null) {
            teacherService.username(id, username);
            return true;
        } else {
            return false;
        }*/
    }


    @DELETE
    @Path("{id}")
    @Description("删除")
    public void delete(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid) {

        new PicResource().delete("/player/" + id + ".png");
        super.getService(PlayerService.class).delete(id);

    }

    @PUT
    @Path("findPassword")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("找回密码")
    public DMLResponse findPassword(@FormParam("mobile") String mobile,
                                    @FormParam("newPassword") String newPassword,
                                    @FormParam("verifyCode") String verifyCode) {

        if (!VerifyResource.verify(mobile, verifyCode)) {
            return new DMLResponse(false, "验证码错误");
        }

        // 修改密码
        PlayerService service = super.getService(PlayerService.class);
        int n = service.findPassword(mobile, DigestUtils.md5Hex(newPassword));

        if (n > 0) {
            return new DMLResponse(true, "修改密码成功");
        } else {
            return new DMLResponse(false, "修改失败，请检查账号是否正确");
        }
    }
}
