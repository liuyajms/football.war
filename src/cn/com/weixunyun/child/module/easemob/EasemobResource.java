package cn.com.weixunyun.child.module.easemob;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.ResultEntity;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.bean.Player;
import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.service.PlayerService;
import cn.com.weixunyun.child.model.service.TeamPlayerService;
import cn.com.weixunyun.child.model.service.TeamService;
import cn.com.weixunyun.child.model.vo.PlayerVO;
import cn.com.weixunyun.child.model.vo.TeamPlayerVO;
import cn.com.weixunyun.child.model.vo.TeamVO;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/easemob")
@Produces(MediaType.APPLICATION_JSON)
@Description("环信同步")
public class EasemobResource extends AbstractResource {


    /**
     * 一、查询所有的球员，创建环信账号
     * 二、查询所有的球队，创建球队群组；
     * 三、将所有的球员加入聊天群组
     *
     * @param rsessionid
     * @throws Exception
     */
    @POST
    @Path("player")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加成员")
    public Map<String, List<Player>> initPlayer(@CookieParam("rsessionid") String rsessionid)
            throws Exception {

        List<PlayerVO> playerList = super.getService(PlayerService.class)
                .getList(null, null, null, null, null, null, null, 0, 0);

        Map<String, List<Player>> map = new HashMap<>();
        List<Player> successList = new ArrayList<>();
        List<Player> failedList = new ArrayList<>();

        for (Player player : playerList) {
            try {
                EasemobHelper.createUser(player.getId());
                successList.add(player);
            } catch (Exception e) {

                e.printStackTrace();
                failedList.add(player);
            }
        }

        map.put("success", successList);
        map.put("failed", failedList);

        return map;
    }


    @POST
    @Path("team")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加群组,同时将群组中的成员加入该组")
    public Map<String, List<TeamVO>> initTeam(@CookieParam("rsessionid") String rsessionid)
            throws Exception {

        TeamService teamService = super.getService(TeamService.class);
        TeamPlayerService teamPlayerService = super.getService(TeamPlayerService.class);

        List<TeamVO> teamList = teamService.getAllList(null, 0, 0);

        Map<String, List<TeamVO>> map = new HashMap<>();
        List<TeamVO> successList = new ArrayList<>();
        List<TeamVO> failedList = new ArrayList<>();

        for (TeamVO team : teamList) {
            try {

                //如果组ID非空，则代表已经初始化，则忽略掉
                if (StringUtils.isNotBlank(team.getGroupId())) {
                    break;
                }

                List<TeamPlayerVO> playerVOList = teamPlayerService.getList(team.getId(), null, null);

                if (playerVOList.size() > 0) {//球队有球员时才创建群组
                    Long[] playerIds = new Long[playerVOList.size()];

                    for (int i = 0; i < playerIds.length; i++) {
                        playerIds[i] = playerVOList.get(i).getPlayerId();
                    }
                    String groupId = EasemobHelper.createGroup(team.getName(), playerIds);
                    team.setGroupId(groupId);
                    //保存组ID
                    teamService.update(team);
                    successList.add(team);
                }
            } catch (Exception e) {

                e.printStackTrace();
                failedList.add(team);
            }
        }

        map.put("success", successList);
        map.put("failed", failedList);

        return map;
    }


    @POST
    @Path("team/{teamId}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加群组,同时将群组中的成员加入该组")
    public ResultEntity initTeam(@CookieParam("rsessionid") String rsessionid, @PathParam("teamId") Long teamId)
            throws Exception {

        TeamService teamService = super.getService(TeamService.class);
        TeamPlayerService teamPlayerService = super.getService(TeamPlayerService.class);

        Team team = teamService.select(teamId);

        try {
            List<TeamPlayerVO> playerVOList = teamPlayerService.getList(team.getId(), null, null);

            if (playerVOList.size() > 0) {//球队有球员时才创建群组
                Long[] playerIds = new Long[playerVOList.size()];

                for (int i = 0; i < playerIds.length; i++) {
                    playerIds[i] = playerVOList.get(i).getPlayerId();
                }
                String groupId = EasemobHelper.createGroup(team.getName(), playerIds);
                team.setGroupId(groupId);
                //保存组ID
                teamService.update(team);
            } else {
                return new ResultEntity(HttpStatus.SC_BAD_REQUEST, "创建失败,该群无成员");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return new ResultEntity(HttpStatus.SC_OK, "创建成功");
    }


    @DELETE
    @Path("player/{playerId}")
    @Description("删除成员")
    public void delete(@PathParam("playerId") Long playerId, @CookieParam("rsessionid") String rsessionid) {

        EasemobHelper.deleteUser(playerId);

    }

    @DELETE
    @Path("team/{teamId}")
    @Description("删除群组")
    public void deleteGroup(@PathParam("teamId") Long teamId, @CookieParam("rsessionid") String rsessionid) {

        String groupId = super.getService(TeamService.class).select(teamId).getGroupId();
        EasemobHelper.deleteGroup(groupId);

    }

}
