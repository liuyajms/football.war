package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.ResultEntity;
import cn.com.weixunyun.child.model.bean.TeamPlayer;
import cn.com.weixunyun.child.model.service.TeamPlayerService;
import cn.com.weixunyun.child.model.vo.TeamPlayerVO;
import org.apache.http.HttpStatus;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/teamPlayer")
@Produces(MediaType.APPLICATION_JSON)
@Description("球队成员")
public class TeamPlayerResource extends AbstractResource {

    @GET
    @Description("查询自己参与的球队列表")
    public List<TeamPlayerVO> getTeamList(@CookieParam("rsessionid") String rsessionid,
                                          @QueryParam("keyword") String keyword) {

        return super.getService(TeamPlayerService.class)
                .getList(null, super.getAuthedId(rsessionid), keyword);
    }

    @GET
    @Path("{teamId}")
    @Description("查询球队的球员列表")
    public List<TeamPlayerVO> getPlayerList(@CookieParam("rsessionid") String rsessionid,
                                            @PathParam("teamId") Long teamId,
                                            @QueryParam("keyword") String keyword) {

        return super.getService(TeamPlayerService.class)
                .getList(teamId, null, keyword);
    }


    @POST
    @Path("{teamId}/{playerId}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("球员加入某个球队")
    public ResultEntity insert(@PathParam("teamId") Long teamId,
                               @PathParam("playerId") Long playerId,
                               @CookieParam("rsessionid") String rsessionid)
            throws Exception {
        TeamPlayer teamPlayer = new TeamPlayer();
        teamPlayer.setPlayerId(super.getAuthedId(rsessionid));
        teamPlayer.setTeamId(teamId);

        super.getService(TeamPlayerService.class).insert(teamPlayer);

        return new ResultEntity(HttpStatus.SC_OK, "加入球队成功");
    }


    @DELETE
    @Path("{teamId}/{playerId}")
    @Description("删除(playerId为0则删除自身)")
    public void delete(@PathParam("teamId") Long teamId,
                       @PathParam("playerId") Long playerId,
                       @CookieParam("rsessionid") String rsessionid) {
        super.getService(TeamPlayerService.class)
                .delete(playerId == 0 ? super.getAuthedId(rsessionid) : playerId, teamId);
    }


}