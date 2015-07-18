package cn.com.weixunyun.child.control;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.service.MultiService;

@Path("/multi")
@Produces(MediaType.APPLICATION_JSON)
@Description("去重")
public class MultiResource extends AbstractResource {

	@GET
	@Path("/student/summary")
	@Description("学生汇总")
	public List<Map<String, ?>> getStudentSummaryList(@CookieParam("rsessionid") String rsessionid) {
		MultiService multiService = super.getService(MultiService.class);
		return multiService.getStudentSummaryList(super.getAuthedSchoolId(rsessionid));
	}

	@GET
	@Path("/student")
	@Description("学生")
	public List<Map<String, ?>> getStudentList(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("name") String name) {
		MultiService multiService = super.getService(MultiService.class);
		return multiService.getStudentList(super.getAuthedSchoolId(rsessionid), name);
	}

	@PUT
	@Path("/student")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void updateStudent(@CookieParam("rsessionid") String rsessionid, MultivaluedMap<String, String> form) {
		if (form.containsKey("id")) {
			Set<Long> idSet = new HashSet<Long>();

			for (String id : form.get("id")) {
				idSet.add(Long.parseLong(id));
			}

			MultiService multiService = super.getService(MultiService.class);
			multiService.updateStudent(super.getAuthedSchoolId(rsessionid), idSet);
		}
	}

	@GET
	@Path("/parents/summary")
	@Description("家长汇总")
	public List<Map<String, ?>> getParentsSummaryList(@CookieParam("rsessionid") String rsessionid) {
		MultiService multiService = super.getService(MultiService.class);
		return multiService.getParentsSummaryList(super.getAuthedSchoolId(rsessionid));
	}

	@GET
	@Path("/parents")
	@Description("家长")
	public List<Map<String, ?>> getParentsList(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("name") String name) {
		MultiService multiService = super.getService(MultiService.class);
		return multiService.getParentsList(super.getAuthedSchoolId(rsessionid), name);
	}

	@PUT
	@Path("/parents")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void updateParents(@CookieParam("rsessionid") String rsessionid, MultivaluedMap<String, String> form) {
		if (form.containsKey("id")) {
			Set<Long> idSet = new HashSet<Long>();

			for (String id : form.get("id")) {
				idSet.add(Long.parseLong(id));
			}

			MultiService multiService = super.getService(MultiService.class);
			multiService.updateParents(super.getAuthedSchoolId(rsessionid), idSet);
		}
	}

}
