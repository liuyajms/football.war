package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.pojo.Global;
import cn.com.weixunyun.child.model.service.GlobalService;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/global")
@Produces(MediaType.APPLICATION_JSON)
@Description("全局设置")
public class GlobalResource extends AbstractResource {

	/*@GET
    @Description("列表")
	public Map<String, ?> selectAll(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@QueryParam("con") String con) {
		try {
			page = page > 0 ? page - 1 : 0;

			GlobalService service = super.getService(GlobalService.class);

			int total = service.selectAllCount(con);
			List<Global> cList = service.selectAll(page, rows, con);
			List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
			Map<String, Object> m = new HashMap<String, Object>();
			for (Global global : cList) {
				m = new HashMap<String, Object>();
				m.put("codeParent", global.getCodeParent());
				m.put("code", global.getCode());
				m.put("value", global.getValue());
				m.put("group", global.getCodeParent());
				m.put("editor", "text");
				list.add(m);
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", list);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/

    // @GET
    // public Global select(@QueryParam("codeParent") String codeParent,
    // @QueryParam("code") String code) {
    // return super.getService(GlobalService.class).select(codeParent, code);
    // }
    //

    @GET
    @Description("全局设置列表")
    public List<Global> getList(@CookieParam("rsessionid") String rsessionid) {
        try {
            GlobalService service = super.getService(GlobalService.class);
            return service.selectAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GET
    @Path("/count")
    @Description("全局设置-值总数")
    public int selectClassesCount(@CookieParam("rsessionid") String rsessionid) {
        GlobalService service = super.getService(GlobalService.class);
        int total = service.selectAllCount();
        return total;
    }

    @GET
    @Path("{codeParent}.{code}")
    @Description("详情")
    public Global select(@CookieParam("rsessionid") String rsessionid, @PathParam("codeParent") String codeParent,
                         @PathParam("code") String code) {
        return super.getService(GlobalService.class).select(codeParent, code);
    }

    @GET
    @Path("0/{codeParent}.{code}")
    @Description("全局详情")
    public Global select0(@PathParam("codeParent") String codeParent, @PathParam("code") String code) {
        return super.getService(GlobalService.class).select(codeParent, code);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("修改")
    public void update(@QueryParam("codeParent") String codeParent, @QueryParam("code") String code,
                       @CookieParam("rsessionid") String rsessionid, MultivaluedMap<String, String> formData,
                       @QueryParam("value") String value) throws Exception {

        GlobalService service = super.getService(GlobalService.class);

        String code1, code2;

        Set<String> set = formData.keySet();
        for (String str : set) {
            if (str.contains("_")) {
                code1 = str.split("_")[0];
                code2 = str.split("_")[1];
            } else {
                code1 = "term";
                code2 = "default";
            }
            Global g = service.select(code1, code2);
            if (str.contains("_")) {
                g.setValue(URLDecoder.decode(formData.getFirst(str), "UTF-8"));
            } else {
                g.setValue(URLDecoder.decode(formData.getFirst("year") + formData.getFirst("term"), "UTF-8"));
            }
            service.update(g);
        }

    }

    @DELETE
    @Description("删除")
    public void delete(@QueryParam("codeParent") String codeParent, @QueryParam("code") String code,
                       @QueryParam("value") String value) {
        Global global = new Global();
        global.setCodeParent(codeParent);
        global.setCode(code);
        global.setValue(value);

        super.getService(GlobalService.class).delete(global);
    }
}
