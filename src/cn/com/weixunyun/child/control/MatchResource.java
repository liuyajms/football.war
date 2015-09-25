package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.Constants;
import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.ResultEntity;
import cn.com.weixunyun.child.model.bean.Match;
import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.bean.TeamPlayer;
import cn.com.weixunyun.child.model.service.FriendService;
import cn.com.weixunyun.child.model.service.MatchService;
import cn.com.weixunyun.child.model.service.TeamPlayerService;
import cn.com.weixunyun.child.model.service.TeamService;
import cn.com.weixunyun.child.model.vo.MatchVO;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.wink.common.annotations.Workspace;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/match")
@Produces(MediaType.APPLICATION_JSON)
@Description("球员")
public class MatchResource extends AbstractResource {

    @Autowired
    private MatchService service;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamPlayerService teamPlayerService;


    @GET
    @Description("列表")
    public List<MatchVO> getList(@CookieParam("rsessionid") String rsessionid,
                                 @QueryParam("city") String city,
                                 @QueryParam("rule") Integer rule,
                                 @QueryParam("beginDate") String beginDate,
                                 @QueryParam("endDate") String endDate,
                                 @QueryParam("keyword") String keyword,
                                 @QueryParam("px") Double px, @QueryParam("py") Double py,
                                 @QueryParam("page") long page, @QueryParam("rows") long rows) {

        return service.getList(city, rule,
                StringUtils.isNotBlank(beginDate) ? Date.valueOf(beginDate) : null,
                StringUtils.isNotBlank(endDate) ? Date.valueOf(endDate) : null,
                keyword, px, py, rows, page * rows);
    }


    @GET
    @Path("player/{playerId}")
    @Description("查询某人的球赛列表，为0则查看我的球赛")
    public List<MatchVO> getPlayerMatchList(@CookieParam("rsessionid") String rsessionid,
                                            @PathParam("playerId") Long playerId,
                                            @QueryParam("type") Integer type, //友谊赛、训练赛
                                            @QueryParam("beginDate") String beginDate,
                                            @QueryParam("endDate") String endDate,
                                            @QueryParam("keyword") String keyword,
                                            @QueryParam("page") int page, @QueryParam("rows") int rows) {
        return service.getMatchList(playerId == 0 ? super.getAuthedId(rsessionid) : playerId, null, null, type,
                StringUtils.isNotBlank(beginDate) ? Date.valueOf(beginDate) : null,
                StringUtils.isNotBlank(endDate) ? Date.valueOf(endDate) : null,
                keyword, rows, page * rows);
    }


    @GET
    @Path("court/{courtId}")
    @Description("球场相关的球赛列表")
    public List<MatchVO> getCourtMatchList(@CookieParam("rsessionid") String rsessionid,
                                           @PathParam("courtId") Long courtId,
                                           @QueryParam("type") Integer type, //友谊赛、训练赛
                                           @QueryParam("beginDate") String beginDate,
                                           @QueryParam("endDate") String endDate,
                                           @QueryParam("keyword") String keyword,
                                           @QueryParam("page") int page, @QueryParam("rows") int rows) {
        return service.getMatchList(null, courtId, null, type,
                StringUtils.isNotBlank(beginDate) ? Date.valueOf(beginDate) : null,
                StringUtils.isNotBlank(endDate) ? Date.valueOf(endDate) : null,
                keyword, rows, page * rows);
    }


    @GET
    @Path("team/{teamId}")
    @Description("球队相关的球赛列表")
    public List<MatchVO> getTeamMatchList(@CookieParam("rsessionid") String rsessionid,
                                          @PathParam("teamId") Long teamId,
                                          @QueryParam("type") Integer type, //友谊赛、训练赛
                                          @QueryParam("beginDate") String beginDate,
                                          @QueryParam("endDate") String endDate,
                                          @QueryParam("keyword") String keyword,
                                          @QueryParam("page") int page, @QueryParam("rows") int rows) {
        return service.getMatchList(null, null, teamId, type,
                StringUtils.isNotBlank(beginDate) ? Date.valueOf(beginDate) : null,
                StringUtils.isNotBlank(endDate) ? Date.valueOf(endDate) : null,
                keyword, rows, page * rows);
    }

    @GET
    @Path("{id}")
    @Description("详情")
    public MatchVO select(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
        try {
            return service.get(id);

        } catch (NullPointerException e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }


    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("新建球赛，（新建友谊赛时包含srcTeamId字段，新建训练赛时不需要该字段）,可预先邀请队员，字段为playerIds")
    public ResultEntity insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
            throws IOException {
        Map<String, PartField> map = super.partMulti(request);
        //字段校验
        if (!map.containsKey("type") || StringUtils.isBlank(map.get("type").getValue())) {
            return new ResultEntity(HttpStatus.SC_BAD_REQUEST, "球赛类型字段为空");
        }

        //先创建临时球队
        Team team = super.buildBean(Team.class, map, null);

        //创建球赛
        Match match = super.buildBean(Match.class, map, null);
        match.setCreatePlayerId(super.getAuthedId(rsessionid));
        match.setTeamId(team.getId());
        match.setOpen(match.getOpen() == null ? true : match.getOpen());

        team.setCreatePlayerId(super.getAuthedId(rsessionid));
        team.setTmp(true);
        if (match.getType() == Constants.MATCH_FRIEND) {
            team.setName(teamService.get(team.getSrcTeamId()).getName());
        }

        updateImage(map, team.getId(), "team");

        //判断是否有邀请的球员
        String[] playerIds = null;
        if (map.containsKey("playerIds") && StringUtils.isNotBlank(map.get("playerIds").getValue().toString())) {
            playerIds = map.get("playerIds").getValue().split(",");
        }


        service.insertMatch(match, team, playerIds);

        return new ResultEntity(HttpStatus.SC_OK, "球赛创建成功", service.get(match.getId()));

    }


    @PUT
    @Path("{id}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("修改球赛")
    public ResultEntity update(@Context HttpServletRequest request, @PathParam("id") Long id,
                               @CookieParam("rsessionid") String rsessionid) throws IOException {

        checkMatch(id, null);

        Match matchVO = service.get(id);
        if (!super.getAuthedId(rsessionid).equals(matchVO.getCreatePlayerId())) {
            return new ResultEntity(HttpStatus.SC_FORBIDDEN, "请检查是否有权限");
        }

        Map<String, PartField> map = super.partMulti(request);
        Match match = super.buildBean(Match.class, map, id);

        Team team = super.buildBean(Team.class, map, matchVO.getTeamId());

        updateImage(map, match.getTeamId(), "team");

        service.updateMatch(match, team);

        return new ResultEntity(HttpStatus.SC_OK, "修改球赛成功");

    }


    @DELETE
    @Path("{id}")
    @Description("删除")
    public ResultEntity delete(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid) {

        //判断当前登陆者是否有权删除
        Long createId = service.get(id).getCreatePlayerId();
        if (!super.getAuthedId(rsessionid).equals(createId)) {
            return new ResultEntity(HttpStatus.SC_FORBIDDEN, "请检查是否有权限删除");
        }

        int n = super.getService(MatchService.class).deleteMatch(id);

        if (n > 0) {
            return new ResultEntity(HttpStatus.SC_OK, "删除成功");
        }
        return new ResultEntity(HttpStatus.SC_NOT_FOUND, "未找到删除项");
    }

    /**
     * 以下为业务功能部分
     */

    @POST
    @Path("{id}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("接受挑战赛，(我要应战,如果选择我的球队，则需要srcTeamId字段)，可预先邀请队员，字段为playerIds")
    public ResultEntity acceptMatch(@Context HttpServletRequest request,
                                    @PathParam("id") Long id,
                                    @CookieParam("rsessionid") String rsessionid) throws IOException {
        checkMatch(id, null);

        //先创建临时球队
        Map<String, PartField> map = super.partMulti(request);
        Team team = super.buildBean(Team.class, map, null);

        //加入球赛应战方
        Match match = service.get(id);
        match.setAcceptTeamId(team.getId());
        if (match.getType() != Constants.MATCH_FRIEND) {
            return new ResultEntity(HttpStatus.SC_BAD_REQUEST, "错误请求");
        }

        team.setCreatePlayerId(super.getAuthedId(rsessionid));
        team.setTmp(true);
        team.setName(teamService.get(team.getSrcTeamId()).getName());

        updateImage(map, team.getId(), "team");

        //判断是否有邀请的球员
        String[] playerIds = null;
        if (map.containsKey("playerIds") && StringUtils.isNotBlank(map.get("playerIds").getValue().toString())) {
            playerIds = map.get("playerIds").getValue().split(",");
        }

        service.acceptMatch(match, team, playerIds);

        return new ResultEntity(HttpStatus.SC_OK, "成功接受挑战");

    }


    @POST
    @Path("{id}/{teamId}/{playerId}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("申请或邀请加入球赛，（加入球赛双方中的一队,playerId=0则为申请加入某场球赛）")
    public ResultEntity addMatch(@PathParam("id") Long id,
                                 @PathParam("teamId") Long teamId,
                                 @PathParam("playerId") Long playerId,
                                 @CookieParam("rsessionid") String rsessionid) {

        checkMatch(id, teamId);

        TeamPlayer teamPlayer = new TeamPlayer();
        teamPlayer.setTeamId(teamId);

        if (playerId.equals(super.getAuthedId(rsessionid)) || playerId == 0) {//主动加入
            teamPlayer.setAgreed(true);
            teamPlayer.setPlayerId(super.getAuthedId(rsessionid));
        } else {//队长邀请人参与，需要检查是否与该球员为好友
            if (super.getService(FriendService.class).isFriend(super.getAuthedId(rsessionid), playerId) == 0) {
                return new ResultEntity(HttpStatus.SC_FORBIDDEN, "请检查该球员是否为您的好友");
            }
            teamPlayer.setAgreed(false);
            teamPlayer.setPlayerId(playerId);
        }

        teamPlayerService.insert(teamPlayer);

        return new ResultEntity(HttpStatus.SC_OK, "加入球赛成功");

    }


    @PUT
    @Path("{id}/{teamId}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("同意加入球赛")
    public ResultEntity agreedMatch(@PathParam("id") Long id, @PathParam("teamId") Long teamId,
                                    @CookieParam("rsessionid") String rsessionid) {

        checkMatch(id, teamId);
        teamPlayerService.agreed(teamId, super.getAuthedId(rsessionid));

        return new ResultEntity(HttpStatus.SC_OK, "同意加入球赛");

    }

    @DELETE
    @Path("{id}/{teamId}/{playerId}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("（拒绝邀请或退赛）或（球赛队长踢人）")
    public ResultEntity refusedMatch(@PathParam("id") Long id, @PathParam("teamId") Long teamId,
                                     @PathParam("playerId") Long playerId,
                                     @CookieParam("rsessionid") String rsessionid) {

        checkMatch(id, teamId);
//        teamPlayerService.delete(teamId, super.getAuthedId(rsessionid));

//        return new ResultEntity(HttpStatus.SC_OK, "操作成功");
        return new TeamPlayerResource().delete(teamId, playerId, rsessionid);

    }

    private void checkMatch(Long id, Long teamId) {
        Match match = service.select(id);
        if (match == null ||
                (teamId != null && !match.getTeamId().equals(teamId)
                        && (match.getAcceptTeamId() == null || !match.getAcceptTeamId().equals(teamId)))) {
            throw new WebApplicationException(new IllegalArgumentException("请求参数错误"), HttpStatus.SC_FORBIDDEN);
        } else if (match.getBeginTime() != null &&
                match.getBeginTime().getTime() < System.currentTimeMillis()) {
            throw new WebApplicationException(new IllegalArgumentException("球赛已过期"), HttpStatus.SC_FORBIDDEN);
        }
    }
}
