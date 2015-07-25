package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.ResultEntity;
import cn.com.weixunyun.child.model.bean.Court;
import cn.com.weixunyun.child.model.service.CourtServeService;
import cn.com.weixunyun.child.model.service.CourtService;
import cn.com.weixunyun.child.model.vo.CourtVO;
import org.apache.http.HttpStatus;
import org.apache.wink.common.annotations.Workspace;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/court")
@Produces(MediaType.APPLICATION_JSON)
@Description("球员")
public class CourtResource extends AbstractResource {

    @Autowired
    private CourtService service;

    @Autowired
    private CourtServeService serveService;

    @GET
    @Description("列表")
    public List<CourtVO> getList(@CookieParam("rsessionid") String rsessionid,
                                 @QueryParam("city") String city,
                                 @QueryParam("rule") Integer rule,
                                 @QueryParam("keyword") String keyword,
                                 @QueryParam("page") long page, @QueryParam("rows") long rows) {
        return service.getList(city, rule, keyword, rows, page * rows);
    }

    @GET
    @Path("{id}")
    @Description("详情")
    public CourtVO select(@PathParam("id") Long id) {

        try {
            return service.get(id);
        }catch (Exception e){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }


    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("添加球场")
    public ResultEntity insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
            throws Exception {
        Map<String, PartField> map = super.partMulti(request);

        Integer rule = getParamValue(map, "rule");

        Court court = super.buildBean(Court.class, map, null);

        court.setRule(rule);
        court.setCreatePlayerId(super.getAuthedId(rsessionid));

        updateImage(map, court.getId());

        //处理球场服务信息
        if (map.containsKey("serveIds") && map.get("serveIds").getValue() != null) {
            service.insertServe(court, map.get("serveIds").getValue().split(","));
        } else {
            service.insert(court);
        }

        return new ResultEntity(HttpStatus.SC_OK, "添加球场成功");

    }


    @PUT
    @Path("{id}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("修改")
    public void update(@Context HttpServletRequest request, @PathParam("id") Long id,
                       @CookieParam("rsessionid") String rsessionid) throws Exception {
        Map<String, PartField> map = super.partMulti(request);

        Integer rule = getParamValue(map, "rule");

        Court court = super.buildBean(Court.class, map, id);
        court.setRule(rule);

        updateImage(map, court.getId());

        //处理球场服务信息
        if (map.containsKey("serveIds") && map.get("serveIds").getValue() != null) {
            service.updateServe(court, map.get("serveIds").getValue().split(","));
        } else {
            service.update(court);
        }
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

        super.getService(CourtService.class).delete(id);
        super.deleteImage(id);

    }


}
