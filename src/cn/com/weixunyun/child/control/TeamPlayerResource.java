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
                .getList(null, playerId == null ? super.getAuthedId(rsessionid) : playerId, false, keyword);
    }

    @GET
    @Path("{teamId}")
    @Description("查询球队的球员列表")
    public List<TeamPlayerVO> getPlayerList(@CookieParam("rsessionid") String rsessionid,
                                            @PathParam("teamId") Long teamId,
                                            @QueryParam("keyword") String keyword) {

        return super.getService(TeamPlayerService.class)
                .getList(teamId, null, null, keyword);
    }


    @Deprecated
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
                               @CookieParam("rsessionid") String rsessionid){

        TeamPlayer teamPlayer = new TeamPlayer();
        teamPlayer.setTeamId(teamId);

        /**
         * 审核暂改为环信发送消息审核
         */
/*        if (super.getAuthedId(rsessionid).equals(playerId) || playerId == 0) {//主动加入
            teamPlayer.setAgreed(false);
            teamPlayer.setPlayerId(super.getAuthedId(rsessionid));
        } else {//邀请加入，无需审核，但需要检查是否为好友
            if (super.getService(FriendService.class).isFriend(super.getAuthedId(rsessionid), playerId) == 0) {
                return new ResultEntity(HttpStatus.SC_FORBIDDEN, "请检查该球员是否为您的好友");
            }
            teamPlayer.setAgreed(true);
            teamPlayer.setPlayerId(playerId);
        }*/


        teamPlayer.setAgreed(true);
        teamPlayer.setPlayerId(playerId == 0 ? super.getAuthedId(rsessionid) : playerId);

        super.getService(TeamPlayerService.class).insert(teamPlayer);

        return new ResultEntity(HttpStatus.SC_OK, "加入球队申请成功");
    }


    @POST
    @Path("{teamId}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("1、球队队长拉人（多个），2、球赛中的球队拉人，3、主动加入球队（球赛）")
    public ResultEntity insertMulti(@PathParam("teamId") Long teamId,
                                    @FormParam("playerIds") String playerIds,
                                    @CookieParam("rsessionid") String rsessionid) {

        service.insertMulti(super.getAuthedId(rsessionid), teamId, playerIds.split(","));

        return new ResultEntity(HttpStatus.SC_OK, "申请加入球队成功");
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

        Long createPlayerId = super.getService(TeamService.class).get(teamId).getCreatePlayerId();

        boolean isCaptain = authedId.equals(createPlayerId);//是否队长
        boolean isMe = (playerId == 0) || (authedId.equals(playerId));//是否自己退出

        if(isCaptain && isMe){//队长退出,并指定下任
            service.deleteCreatePlayerId(teamId, playerId);
        }else if(isCaptain || isMe){//队长踢人 或 主动退赛
            if (service.delete(teamId, playerId) == 0) {
                return new ResultEntity(HttpStatus.SC_NOT_FOUND, "未找到删除数据");
            }
        }else{//非队长删除其他成员，禁止操作
            return new ResultEntity(HttpStatus.SC_FORBIDDEN, "您不是当前球队队长，无权删除该球员");
        }

/*
        if (authedId.equals(playerId)) {
            if (authedId.equals(createPlayerId)) {//队长退出,删除并指定下任
                service.deleteCreatePlayerId(teamId, playerId);
            }
        } else {
            if (!authedId.equals(createPlayerId)) {//非队长删除其他成员，禁止操作
                return new ResultEntity(HttpStatus.SC_FORBIDDEN, "您不是当前球队队长，无权删除该球员");
            }
        }

        if (service.delete(teamId, playerId) > 0) {
            return new ResultEntity(HttpStatus.SC_OK, "删除成功");
        }*/
        return new ResultEntity(HttpStatus.SC_OK, "删除成功");
    }


    /**
     * 删除多个队友，先判断删除列表中是否包含自身，若包含，则先删除其他队友，然后删除自己，并指定下任队长；否则，直接删除
     *
     * @param teamId
     * @param playerIds
     * @param rsessionid
     * @return
     */
    @DELETE
    @Path("{teamId}")
    @Description("删除多个队友")
    public ResultEntity deleteMulti(@PathParam("teamId") Long teamId,
                                    @QueryParam("playerIds") String playerIds,
                                    @CookieParam("rsessionid") String rsessionid) {

        Long createPlayerId = super.getService(TeamService.class).get(teamId).getCreatePlayerId();
        //身份校验
        if (!super.getAuthedId(rsessionid).equals(createPlayerId)) {//非队长删除其他成员，禁止操作
            return new ResultEntity(HttpStatus.SC_FORBIDDEN, "您不是当前球队队长，无权删除该球员");
        }

        service.deleteMulti(super.getAuthedId(rsessionid), teamId, playerIds.split(","));


        return new ResultEntity(HttpStatus.SC_OK, "删除成功");

    }
}
