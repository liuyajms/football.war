package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.bean.Friend;
import cn.com.weixunyun.child.model.service.FriendService;
import cn.com.weixunyun.child.model.vo.FriendVO;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/friend")
@Produces(MediaType.APPLICATION_JSON)
@Description("我的好友")
public class FriendResource extends AbstractResource {


    @GET
    @Description("列表")
    public List<FriendVO> getList(@CookieParam("rsessionid") String rsessionid,
                                  @QueryParam("keyword") String keyword,
                                  @QueryParam("page") long page, @QueryParam("rows") long rows) {

        return super.getService(FriendService.class)
                .getList(super.getAuthedId(rsessionid), keyword, rows, page * rows);
    }


    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("添加")
    public void insert(@FormParam("friendPlayerId") Long friendPlayerId, @CookieParam("rsessionid") String rsessionid)
            throws Exception {
        Friend friend = new Friend();
        friend.setPlayerId(super.getAuthedId(rsessionid));
        friend.setFriendPlayerId(friendPlayerId);

        super.getService(FriendService.class).insert(friend);
    }


    @DELETE
    @Path("{friendPlayerId}")
    @Description("删除")
    public void delete(@PathParam("friendPlayerId") Long friendPlayerId, @CookieParam("rsessionid") String rsessionid) {
        super.getService(FriendService.class).delete(super.getAuthedId(rsessionid), friendPlayerId);
    }


}
