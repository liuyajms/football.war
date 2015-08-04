package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.ResultEntity;
import cn.com.weixunyun.child.model.bean.Favorite;
import cn.com.weixunyun.child.model.service.FavoriteService;
import cn.com.weixunyun.child.model.vo.CourtVO;
import org.apache.http.HttpStatus;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.List;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/favorite")
@Produces(MediaType.APPLICATION_JSON)
@Description("我的好友")
public class FavoriteResource extends AbstractResource {

    @Autowired
    private FavoriteService service;

    @GET
    @Description("获取我收藏的球场列表")
    public List<CourtVO> getList(@CookieParam("rsessionid") String rsessionid,
                                 @QueryParam("playerId") Long playerId,
                                 @QueryParam("keyword") String keyword,
                                 @QueryParam("page") long page, @QueryParam("rows") long rows) {

        return service.getList(playerId == null ? super.getAuthedId(rsessionid) : playerId, keyword, rows, page * rows);
    }


    @POST
    @Path("{courtId}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加")
    public ResultEntity insert(@PathParam("courtId") Long courtId, @CookieParam("rsessionid") String rsessionid)
            throws Exception {
        Favorite favorite = new Favorite();
        favorite.setPlayerId(super.getAuthedId(rsessionid));
        favorite.setCourtId(courtId);
        favorite.setCreateTime(new Timestamp(System.currentTimeMillis()));

        super.getService(FavoriteService.class).insert(favorite);

        return new ResultEntity(HttpStatus.SC_OK, "添加成功");
    }


    @DELETE
    @Path("{courtId}")
    @Description("删除")
    public void delete(@PathParam("courtId") Long courtId, @CookieParam("rsessionid") String rsessionid) {
        super.getService(FavoriteService.class).delete(super.getAuthedId(rsessionid), courtId);
    }


}
