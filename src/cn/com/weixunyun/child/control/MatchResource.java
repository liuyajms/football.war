package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.ResultEntity;
import cn.com.weixunyun.child.model.bean.Match;
import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.service.MatchService;
import cn.com.weixunyun.child.model.service.TeamService;
import cn.com.weixunyun.child.model.vo.MatchVO;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.List;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/match")
@Produces(MediaType.APPLICATION_JSON)
@Description("球员")
public class MatchResource extends AbstractResource {

    @Autowired
    private MatchService service;

    @Autowired
    private TeamService teamService;

    public final static int MATCH_TRAIN = 1;//训练赛
    public final static int MATCH_FRIEND = 2;//友谊赛


    @GET
    @Description("列表（新增字段是否已加入）")
    public List<MatchVO> getList(@CookieParam("rsessionid") String rsessionid,
                                 @QueryParam("city") String city,
                                 @QueryParam("rule") Integer rule,
                                 @QueryParam("beginDate") String beginDate,
                                 @QueryParam("endDate") String endDate,
                                 @QueryParam("keyword") String keyword,
                                 @QueryParam("page") long page, @QueryParam("rows") long rows) {

        return service.getList(city, rule,
                StringUtils.isNotBlank(beginDate) ? Date.valueOf(beginDate) : null,
                StringUtils.isNotBlank(endDate) ? Date.valueOf(endDate) : null,
                keyword, rows, page * rows);
    }


    @GET
    @Path("player/{playerId}")
    @Description("查询某人的球赛列表，为0则查看我的球赛")
    public List<MatchVO> getMatchList(@CookieParam("rsessionid") String rsessionid,
                                      @PathParam("playerId") Long playerId,
                                      @QueryParam("type") Integer type, //友谊赛、训练赛
                                      @QueryParam("beginDate") String beginDate,
                                      @QueryParam("endDate") String endDate,
                                      @QueryParam("keyword") String keyword,
                                      @QueryParam("page") long page, @QueryParam("rows") long rows) {
        return service.getPlayerMatchList(playerId == 0 ? super.getAuthedId(rsessionid) : playerId, type,
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
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加球赛，（新建友谊赛时包含srcTeamId字段，新建训练赛时不需要该字段）")
    public ResultEntity insert(MultivaluedMap<String, String> map, @CookieParam("rsessionid") String rsessionid) {

        //字段校验
        if (!map.containsKey("type") || StringUtils.isBlank(map.getFirst("type"))) {
            return new ResultEntity(HttpStatus.SC_BAD_REQUEST, "球赛类型字段为空");
        }

        //先创建临时球队
        Team team = super.buildBean(Team.class, map, null);

        //创建球赛
        Match match = super.buildBean(Match.class, map, null);
        match.setCreatePlayerId(super.getAuthedId(rsessionid));
        match.setTeamId(team.getId());

        team.setCreatePlayerId(super.getAuthedId(rsessionid));
        team.setTmp(true);
        if (match.getType() == MATCH_FRIEND) {
            team.setName(teamService.get(team.getSrcTeamId()).getName());
        }

        service.insertMatch(team, match);

        return new ResultEntity(HttpStatus.SC_OK, "球赛创建成功");

    }


    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("加入挑战赛，(我要应战)")
    public ResultEntity addMatch(MultivaluedMap<String, String> map,
                                 @PathParam("id") Long id,
                                 @CookieParam("rsessionid") String rsessionid) {

        //先创建临时球队
        Team team = super.buildBean(Team.class, map, null);

        //加入球赛应战方
        Match match = service.get(id);
        match.setAcceptTeamId(team.getId());
        if (match.getType() != MATCH_FRIEND) {
            return new ResultEntity(HttpStatus.SC_BAD_REQUEST, "错误请求");
        }

        team.setCreatePlayerId(super.getAuthedId(rsessionid));
        team.setTmp(true);
        team.setName(teamService.get(team.getSrcTeamId()).getName());

        service.addMatch(team, match);

        return new ResultEntity(HttpStatus.SC_OK, "成功接受挑战");

    }


    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("修改球赛")
    public ResultEntity update(MultivaluedMap<String, String> map,
                               @PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid) {

        Long createId = service.get(id).getCreatePlayerId();
        if (!super.getAuthedId(rsessionid).equals(createId)) {
            return new ResultEntity(HttpStatus.SC_FORBIDDEN, "请检查是否有权限");
        }

        Match match = super.buildBean(Match.class, map, id);

        service.update(match);

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

        int n = super.getService(MatchService.class).delete(id);

        if (n > 0) {
            return new ResultEntity(HttpStatus.SC_OK, "删除成功");
        }
        return new ResultEntity(HttpStatus.SC_NOT_FOUND, "未找到删除项");
    }


}
