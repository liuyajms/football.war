package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.bean.TeamPlayer;
import cn.com.weixunyun.child.model.service.TeamService;
import cn.com.weixunyun.child.model.vo.TeamVO;
import org.apache.commons.lang.StringUtils;
import org.apache.wink.common.annotations.Workspace;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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
                                @QueryParam("srcTeamId") Long srcTeamId,
                                @QueryParam("page") long page, @QueryParam("rows") long rows) {
        return super.getService(TeamService.class)
                .getList(city, rule, beginAge, endAge, keyword, srcTeamId, rows, page * rows);
    }

    @GET
    @Path("{id}")
    @Description("详情")
    public TeamVO select(@PathParam("id") long id, @CookieParam("rsessionid") String rsessionid) {
        TeamVO teamVO = super.getService(TeamService.class).get(id);
        teamVO.setIsJoined(false);
        for (TeamPlayer teamPlayer : teamVO.getTeamPlayerList()) {
            if (teamPlayer.getPlayerId().equals(super.getAuthedId(rsessionid))) {
                teamVO.setIsJoined(true);
            }
        }
        return teamVO;
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

        //判断是否有预制球员
        String[] playerIds = null;
        if(map.containsKey("playerIds") && StringUtils.isNotBlank(map.get("playerIds").getValue().toString())){

            playerIds = map.get("playerIds").getValue().split(",");

        }
//        super.getService(TeamService.class).insert(team);
        super.getService(TeamService.class).insertPlayer(team, playerIds);

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
