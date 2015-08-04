package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.ResultEntity;
import cn.com.weixunyun.child.model.bean.TeamPlayer;
import cn.com.weixunyun.child.model.service.FriendService;
import cn.com.weixunyun.child.model.service.TeamPlayerService;
import cn.com.weixunyun.child.model.service.TeamService;
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

    @Autowired
    private TeamPlayerService service;

    @GET
    @Description("查询自己参与的球队列表")
    public List<TeamPlayerVO> getTeamList(@CookieParam("rsessionid") String rsessionid,
                                          @QueryParam("playerId") Long playerId,
                                          @QueryParam("keyword") String keyword) {

        return super.getService(TeamPlayerService.class)
                .getList(null, playerId == null ? super.getAuthedId(rsessionid) : playerId, keyword);
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


    /**
     * 如果该球员为当前登录者，则无须校验；
     * 否则，判断该球员是否为当前登录人的好友，如果不是，则禁止加入该球队（待校验：当前登录者是否为该球队队长）
     *
     * @param teamId
     * @param playerId
     * @param rsessionid
     * @return
     * @throws Exception
     */
    @POST
    @Path("{teamId}/{playerId}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("球员加入某个球队（playerId为0则申请加入球队）")
    public ResultEntity insert(@PathParam("teamId") Long teamId,
                               @PathParam("playerId") Long playerId,
                               @CookieParam("rsessionid") String rsessionid)
            throws Exception {

        TeamPlayer teamPlayer = new TeamPlayer();
        teamPlayer.setTeamId(teamId);

        if (super.getAuthedId(rsessionid).equals(playerId) || playerId == 0) {//主动加入
            teamPlayer.setAgreed(false);
            teamPlayer.setPlayerId(super.getAuthedId(rsessionid));
        } else {//邀请加入，无需审核，但需要检查是否为好友
            if (super.getService(FriendService.class).isFriend(super.getAuthedId(rsessionid), playerId) == 0) {
                return new ResultEntity(HttpStatus.SC_FORBIDDEN, "请检查该球员是否为您的好友");
            }
            teamPlayer.setAgreed(true);
            teamPlayer.setPlayerId(playerId);
        }


        super.getService(TeamPlayerService.class).insert(teamPlayer);

        return new ResultEntity(HttpStatus.SC_OK, "加入球队申请成功");
    }


    /**
     * 如果不是自己申请取消加入该球队，则校验当前操作者的身份，是否为该队队长
     *
     * @param teamId
     * @param playerId
     * @param rsessionid
     * @return
     */
    @DELETE
    @Path("{teamId}/{playerId}")
    @Description("删除(playerId为0则删除自身)")
    public ResultEntity delete(@PathParam("teamId") Long teamId,
                               @PathParam("playerId") Long playerId,
                               @CookieParam("rsessionid") String rsessionid) {

        Long authedId = super.getAuthedId(rsessionid);
        playerId = playerId == 0 ? super.getAuthedId(rsessionid) : playerId;

        if (authedId != playerId) {
            if (super.getService(TeamService.class).get(teamId).getCreatePlayerId() != authedId) {
                return new ResultEntity(HttpStatus.SC_FORBIDDEN, "您不是当前球队队长，无权删除该球员");
            }
        }

        if (service.delete(teamId, playerId) > 0) {
            return new ResultEntity(HttpStatus.SC_OK, "删除成功");
        }
        return new ResultEntity(HttpStatus.SC_NOT_FOUND, "未找到删除数据");
    }


}
