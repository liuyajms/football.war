package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.Session;
import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.CourseClasses;
import cn.com.weixunyun.child.model.bean.RolePopedom;
import cn.com.weixunyun.child.model.bean.User;
import cn.com.weixunyun.child.model.pojo.*;
import cn.com.weixunyun.child.model.service.*;
import cn.com.weixunyun.child.util.ValidateImageUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.codec.digest.DigestUtils;
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

    public static final int TEACHER = 0;
    public static final int PARENTS = 1;

    @GET
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
    @Path("teacher")
    @Description("教师登录")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Map<String, Object> insertTeacher(@CookieParam("rsessionid") String rsessionid,
                                             @Context HttpServletRequest request) {

        Long schoolId = Long.parseLong(request.getParameter("schoolId"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = super.getService(UserService.class)
                .schoolLogin(schoolId, 0, username, DigestUtils.md5Hex(password));
        if (user == null) {
            return null;
        }

        TeacherService teacherService = super.getService(TeacherService.class);
        Teacher teacher = teacherService.select(user.getId());

        if (teacher == null) {
            return null;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("teacher", teacher);

            if (teacher.getType() == 6) {
                ClassesService classesService = super.getService(ClassesService.class);
                List<Classes> classesList = classesService.selectAll(teacher.getSchoolId());
                map.put("classes", classesList);
            } else {
                List<CourseClasses> classesList = teacherService.getClassesList(teacher.getSchoolId(), teacher.getId());
                map.put("classes", classesList);
            }

            SchoolService schoolService = super.getService(SchoolService.class);
            School school = schoolService.select(schoolId);

            HashMap<Menu, List<Menu>> menuMap = null;
            // if (teacher.getType() == 6L || teacher.getType() == 9L) {
            // MenuService menuService = super.getService(MenuService.class);
            // menuMap = treeMenu(menuService.selectAll());
            // } else {
            // menuMap = treeMenu(teacherService.getMenuList(teacher.getId()));
            // }

            GlobalService globalService = super.getService(GlobalService.class);
            Global assetVersionGlobal = globalService.select(0L, "asset", "version");
            String assetVersion = assetVersionGlobal == null ? null : assetVersionGlobal.getValue();

            Global termDefaultGlobal = globalService.select(school.getId(), "term", "default");
            String termDefault = termDefaultGlobal == null ? null : termDefaultGlobal.getValue();
            Global termBeginGlobal = globalService.select(schoolId, "term", "begin");
            String termBegin = termBeginGlobal == null ? null : termBeginGlobal.getValue();

            Map<String, Object> globalMap = new HashMap<String, Object>();
            globalMap.put("asset.version", assetVersion);

            globalMap.put("term.default", termDefault);
            globalMap.put("term.begin", termBegin);

            if (rsessionid == null) {
                rsessionid = UUID.randomUUID().toString();
            } map.put("rsessionid", rsessionid);
            map.put("school", school);
            map.put("global", globalMap);

            HashMap<String, Integer> popedomMap = null;

            Session.getInstance(rsessionid).set("authed", true).set("school", school).set("teacher", teacher)
                    .set("menu", menuMap).set("popedom", popedomMap);

            // PushProducer.sendSchoolNotification(teacher.getSchoolId(),
            // super.getAuthedSchool(rsessionid).getName(),
            // new String[] { "学校概况", "学校新闻", "教学新闻", "学校师资"
            // }[news.getType().intValue()] + "：" + news.getTitle(),
            // "news" + news.getType(), notificationMap);

            return map;
        }
    }

    @POST
    @Path("parents")
    @Description("家长登录")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Map<String, Object> insertParents(@Context HttpServletRequest request) {
        Long schoolId = Long.parseLong(request.getParameter("schoolId"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = super.getService(UserService.class)
                .schoolLogin(schoolId, 1, username, DigestUtils.md5Hex(password));
        if (user == null) {
            return null;
        }

        ParentsService parentsService = super.getService(ParentsService.class);
        Parents parents = parentsService.select(user.getId());
        if (parents == null) {
            return null;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("parents", parents);

            List<ClassesStudent> studentList = parentsService.getStudentList(schoolId, parents.getId());
            map.put("student", studentList);

            SchoolService schoolService = super.getService(SchoolService.class);
            School school = schoolService.select(schoolId);

            GlobalService globalService = super.getService(GlobalService.class);
            Global assetVersionGlobal = globalService.select(0L, "asset", "version");
            String assetVersion = assetVersionGlobal == null ? null : assetVersionGlobal.getValue();

            Global termDefaultGlobal = globalService.select(school.getId(), "term", "default");
            String termDefault = termDefaultGlobal == null ? null : termDefaultGlobal.getValue();
            Global termBeginGlobal = globalService.select(schoolId, "term", "begin");
            String termBegin = termBeginGlobal == null ? null : termBeginGlobal.getValue();

            Global electiveBeginGlobal = globalService.select(schoolId, "elective", "begin");
            String electiveBegin = termBeginGlobal == null ? null : electiveBeginGlobal.getValue();
            Global electiveEndGlobal = globalService.select(schoolId, "elective", "end");
            String electiveEnd = termBeginGlobal == null ? null : electiveEndGlobal.getValue();

            Map<String, Object> globalMap = new HashMap<String, Object>();
            globalMap.put("asset.version", assetVersion);

            globalMap.put("term.default", termDefault);
            globalMap.put("term.begin", termBegin);
            globalMap.put("elective.begin", electiveBegin);
            globalMap.put("elective.end", electiveEnd);

            String rsessionid = UUID.randomUUID().toString();
            map.put("rsessionid", rsessionid);
            map.put("school", school);
            map.put("global", globalMap);

            Session.getInstance(rsessionid).set("authed", true).set("school", school).set("parents", parents);

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

        SchoolService schoolService = super.getService(SchoolService.class);
        School school = schoolService.select(schoolId);

        GlobalService globalService = super.getService(GlobalService.class);
        Global assetVersionGlobal = globalService.select(0L, "asset", "version");
        String assetVersion = assetVersionGlobal == null ? null : assetVersionGlobal.getValue();

        Map<String, Object> globalMap = new HashMap<String, Object>();
        globalMap.put("asset.version", assetVersion);

        String rsessionid = UUID.randomUUID().toString();
        map.put("rsessionid", rsessionid);
        map.put("school", school);
        map.put("global", globalMap);

        Session.getInstance(rsessionid).set("authed", true).set("school", school);

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
    @Path("teacher/school")
    @Description("教师管理信息")
    public List<School> selectTeacherSchool(@QueryParam("mobile") String mobile, @QueryParam("password") String password) {
        TeacherService service = super.getService(TeacherService.class);
        return service.getSchoolList(mobile, DigestUtils.md5Hex(password));
    }

    @POST
    @Path("teacher/school")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("教师管理登录")
    public Map<String, Object> insertTeacherSchool(MultivaluedMap<String, String> formData) {
        Long schoolId = Long.parseLong(formData.getFirst("schoolId"));
        String mobile = formData.getFirst("mobile");
        String password = formData.getFirst("password");

        TeacherService teacherService = super.getService(TeacherService.class);
        Teacher teacher = teacherService.selectTeacherMobile(schoolId, mobile);
        if (teacher != null && teacher.getPassword().equals(DigestUtils.md5Hex(password))) {
            SchoolService schoolService = super.getService(SchoolService.class);
            School school = schoolService.select(teacher.getSchoolId());

            // HashMap<Menu, List<Menu>> menuMap = null;
            // if (teacher.getType() == 6L || teacher.getType() == 9L) {
            // MenuService menuService = super.getService(MenuService.class);
            // menuMap = treeMenu(menuService.selectAll());
            // } else {
            // menuMap = treeMenu(teacherService.getMenuList(teacher.getId()));
            // }

            HashMap<String, Object> map = new HashMap<String, Object>();
            String rsessionid = UUID.randomUUID().toString();
            map.put("rsessionid", rsessionid);
            map.put("school", school);
            map.put("teacher", teacher);

            Session.getInstance(rsessionid).set("authed", true).set("school", school).set("teacher", teacher)
                    // .set("menu", menuMap)
                    .set("popedom", popedom(teacher.getRoleId()));

            return map;
        } else {
            return null;
        }
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
