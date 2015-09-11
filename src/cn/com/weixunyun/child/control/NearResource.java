package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.service.NearService;
import cn.com.weixunyun.child.model.vo.Near;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/near")
@Produces(MediaType.APPLICATION_JSON)
@Description("附近")
public class NearResource extends AbstractResource {


    @GET
    @Description("列表")
    public List<Near> getList(@CookieParam("rsessionid") String rsessionid,
                              @QueryParam("city") String city,
                              @QueryParam("px") Double px, @QueryParam("py") Double py,
                              @QueryParam("page") int page, @QueryParam("rows") int rows) {
        return super.getService(NearService.class).getList(city, px, py, rows, page * rows);
    }


}
