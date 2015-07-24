package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.ResultEntity;
import cn.com.weixunyun.child.model.service.CourtServeService;
import cn.com.weixunyun.child.model.vo.CourtServeVO;
import org.apache.http.HttpStatus;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/courtServe")
@Produces(MediaType.APPLICATION_JSON)
@Description("球场服务")
public class CourtServeResource extends AbstractResource {


    @GET
    @Path("{courtId}")
    @Description("列表")
    public List<CourtServeVO> getList(@CookieParam("rsessionid") String rsessionid,
                                      @PathParam("courtId") Long courtId,
                                      @QueryParam("keyword") String keyword,
                                      @QueryParam("page") long page, @QueryParam("rows") long rows) {

        return super.getService(CourtServeService.class)
                .getList(courtId, keyword);
    }


    @POST
    @PathParam("{courtId}/{serveId}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加")
    public ResultEntity insert(@PathParam("courtId") Long courtId,
                               @PathParam("serveId") Long serveId,
                               @CookieParam("rsessionid") String rsessionid)
            throws Exception {

        super.getService(CourtServeService.class).insert(courtId, serveId);

        return new ResultEntity(HttpStatus.SC_OK, "添加成功");
    }


    @DELETE
    @Path("{courtId}/{serveId}")
    @Description("删除")
    public void delete(@PathParam("courtId") Long courtId,
                       @PathParam("serveId") Long serveId,
                       @CookieParam("rsessionid") String rsessionid) {
        super.getService(CourtServeService.class).delete(courtId, serveId);
    }


}
