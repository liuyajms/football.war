package cn.com.weixunyun.child.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.pojo.Concern;
import cn.com.weixunyun.child.model.service.ConcernService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/concern")
@Produces(MediaType.APPLICATION_JSON)
@Description("关注")
public class ConcernResource extends AbstractResource {

	@GET
	@Description("列表")
	public Map<String, ?> select(@CookieParam("userIdConcern") Long userIdConcern,
			@QueryParam("userIdConcerned") Long userIdConcerned) {

		ConcernService service = super.getService(ConcernService.class);

		List<Concern> list = service.selectConcern(userIdConcern, userIdConcerned);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		return map;
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Concern concern = super.buildBean(Concern.class, formData, null);
		concern.setSchoolId(super.getAuthedSchool(rsessionid).getId());
		ConcernService service = super.getService(ConcernService.class);
		service.insert(concern);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(ConcernService.class).delete(id);
	}
}
