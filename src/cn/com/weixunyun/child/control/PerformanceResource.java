package cn.com.weixunyun.child.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.model.bean.PerformanceStudent;
import cn.com.weixunyun.child.model.pojo.Performance;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.PerformanceService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/performance")
@Produces(MediaType.APPLICATION_JSON)
public class PerformanceResource extends AbstractResource {

	@GET
	public Map<String, ?> selectAll(@QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("con") String con,
			@CookieParam("rsessionid") String rsessionid) {
		try {

			PerformanceService service = super.getService(PerformanceService.class);

			School schoool = super.getAuthedSchool(rsessionid);
			int total = service.selectAllCount(schoool.getId(), con);
			List<PerformanceStudent> cList = service.selectAll(page * rows, rows, con, schoool.getId());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", cList);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("{id}")
	public Performance select(@PathParam("id") Long id) {
		return super.getService(PerformanceService.class).select(id);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void insert(MultivaluedMap<String, String> formData,
			@CookieParam("rsessionid") String rsessionid)
			throws Exception {

		Performance performance = super.buildBean(Performance.class, formData,
				null);

		Teacher teacher = super.getAuthedTeacher(rsessionid);
		performance.setSchoolId(teacher.getSchoolId());
		performance.setCreateTeacherId(teacher.getId());
		performance.setUpdateTeacherId(teacher.getId());

		PerformanceService service = super.getService(PerformanceService.class);
		service.insert(performance);
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void update(MultivaluedMap<String, String> formData,
			@CookieParam("rsessionid") String rsessionid,
			@PathParam("id") Long id) throws Exception {

		Performance performance = super.buildBean(Performance.class, formData,
				id);
		performance.setUpdateTeacherId(super.getAuthedTeacher(rsessionid)
				.getId());

		super.getService(PerformanceService.class).update(performance);
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") Long id) {
		super.getService(PerformanceService.class).delete(id);
	}
}
