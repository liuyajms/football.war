package cn.com.weixunyun.child.control;

import java.util.List;

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

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.pojo.Feedback;
import cn.com.weixunyun.child.model.service.FeedbackService;

@Path("/feedback")
@Produces(MediaType.APPLICATION_JSON)
@Description("反馈")
public class FeedbackResource extends AbstractResource {

	@GET
	@Description("列表")
	public List<Feedback> getList(@QueryParam("page") Long page, @QueryParam("rows") Long rows) {
		FeedbackService service = super.getService(FeedbackService.class);
		return service.selectAll(page * rows, rows);
	}

	@GET
	@Path("/count")
	@Description("列表")
	public int getListCount() {
		FeedbackService service = super.getService(FeedbackService.class);
		return service.selectAllCount();
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Feedback select(@PathParam("id") long id) {
		return super.getService(FeedbackService.class).select(id);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Feedback feedback = super.buildBean(Feedback.class, formData, null);
		feedback.setUserId(super.getAuthedId(rsessionid));
		feedback.setDate(new java.sql.Date(System.currentTimeMillis()));
		feedback.setSchoolId(super.getAuthedSchool(rsessionid).getId());

		FeedbackService service = super.getService(FeedbackService.class);
		service.insert(feedback);
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void update(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid,
			@PathParam("id") long id) throws Exception {
		Feedback feedback = super.buildBean(Feedback.class, formData, id);
		FeedbackService service = super.getService(FeedbackService.class);
		service.update(feedback);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") long id) {
		super.getService(FeedbackService.class).delete(id);
	}

}
