package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.service.TeamService;
import cn.com.weixunyun.child.model.vo.TeamVO;
import cn.com.weixunyun.child.util.ImageUtils;
import org.apache.commons.io.FileUtils;
import org.apache.wink.common.annotations.Workspace;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/team")
@Produces(MediaType.APPLICATION_JSON)
@Description("球队")
public class TeamResource extends AbstractResource {


    @GET
    @Description("列表")
    public List<TeamVO> getList(@CookieParam("rsessionid") String rsessionid,
                                @QueryParam("city") String city,
                                @QueryParam("rule") Integer rule,//赛制
                                @QueryParam("beginAge") Integer beginAge,
                                @QueryParam("endAge") Integer endAge,
                                @QueryParam("keyword") String keyword,
                                @QueryParam("sourceId") Long sourceId,
                                @QueryParam("page") long page, @QueryParam("rows") long rows) {
        return super.getService(TeamService.class)
                .getList(city, rule, beginAge, endAge, keyword, sourceId, rows, page * rows);
    }

    @GET
    @Path("{id}")
    @Description("详情")
    public TeamVO select(@PathParam("id") long id) {
        return super.getService(TeamService.class).get(id);
    }


    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("添加")
    public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
            throws Exception {
        Map<String, PartField> map = super.partMulti(request);

        Integer rule = getParamValue(map, "rule");
        Integer color = getParamValue(map, "color");

        Team team = super.buildBean(Team.class, map, null);

        team.setRule(rule);
        team.setColor(color);
        team.setCreatePlayerId(super.getAuthedId(rsessionid));

        updateImage(map, team.getId());

        super.getService(TeamService.class).insert(team);

    }

    private Integer getParamValue(Map<String, PartField> map, String field) {
        Integer rule = null;
        if (map.containsKey(field)) {
            String roleValue = map.get(field).getValue();
            if (roleValue != null && !roleValue.equals("")) {
                rule = 0;
                for (String str : roleValue.split(",")) {
                    rule += Integer.parseInt(str);
                }
                map.remove(field);
            }
        }
        return rule;
    }


    @PUT
    @Path("{id}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("修改")
    public void update(@Context HttpServletRequest request, @PathParam("id") Long id,
                       @CookieParam("rsessionid") String rsessionid) throws Exception {
        Map<String, PartField> map = super.partMulti(request);

        Integer rule = getParamValue(map, "rule");
        Integer color = getParamValue(map, "color");

        Team team = super.buildBean(Team.class, map, id);

        team.setRule(rule);
        team.setColor(color);

        updateImage(map, team.getId());

        super.getService(TeamService.class).update(team);
    }

    private void updateImage(Map<String, PartField> map, Long id) throws IOException {
        PartField imageField = map.get("image");
        if (imageField != null) {
            File file = imageField.getFile();
            if (file != null) {
                FileUtils.copyFile(file, new File(super.getFilePath(), "team/" + id
                        + "@l.png"));
                ImageUtils.zoom(file, new File(super.getFilePath(), "team/" + id
                        + ".png"));
            }
        }
    }


    @PUT
    @Path("{id}/image")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("修改头像")
    public void updateImage(@Context HttpServletRequest request, @PathParam("id") Long id,
                            @CookieParam("rsessionid") String rsessionid) throws Exception {
        updateImage(super.partMulti(request), id);
    }


    @DELETE
    @Path("{id}")
    @Description("删除")
    public void delete(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid) {

        new PicResource().delete("/team/" + id + ".png");
        super.getService(TeamService.class).delete(id);

    }


}
