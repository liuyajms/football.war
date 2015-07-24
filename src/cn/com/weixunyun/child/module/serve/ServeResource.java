package cn.com.weixunyun.child.module.serve;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import org.apache.wink.common.annotations.Workspace;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/serve")
@Produces(MediaType.APPLICATION_JSON)
@Description("球场提供的服务")
public class ServeResource extends AbstractResource {

    @Autowired
    private ServeService service;

    @GET
    @Description("列表")
    public List<Serve> getList(@CookieParam("rsessionid") String rsessionid,
                               @QueryParam("keyword") String keyword,
                               @QueryParam("page") long page, @QueryParam("rows") long rows) {
        return service.getList(keyword, rows, page * rows);
    }

    @GET
    @Path("{id}")
    @Description("详情")
    public Serve select(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {

        return service.get(id);
    }


    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("添加")
    public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
            throws Exception {
        Map<String, PartField> map = super.partMulti(request);

        Serve serve = super.buildBean(Serve.class, map, null);

        updateImage(map, serve.getId());

        super.getService(ServeService.class).insert(serve);

    }


    @PUT
    @Path("{id}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("修改")
    public void update(@Context HttpServletRequest request, @PathParam("id") Long id,
                       @CookieParam("rsessionid") String rsessionid) throws Exception {
        Map<String, PartField> map = super.partMulti(request);

        Serve serve = super.buildBean(Serve.class, map, id);

        updateImage(map, serve.getId());

        super.getService(ServeService.class).update(serve);

    }


    @PUT
    @Path("{id}/image")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("修改头像")
    public void updateImage(@Context HttpServletRequest request, @PathParam("id") Long id,
                            @CookieParam("rsessionid") String rsessionid) throws Exception {
        updateImage(super.partMulti(request), id);
        //同步记录修改时间
        service.updated(id);
    }

    @DELETE
    @Path("{id}")
    @Description("删除")
    public void delete(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid) {

        super.getService(ServeService.class).delete(id);
        super.deleteImage(id);

    }


}
