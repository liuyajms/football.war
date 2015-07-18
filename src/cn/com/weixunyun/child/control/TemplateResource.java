package cn.com.weixunyun.child.control;

import java.net.URLDecoder;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
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

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Template;
import cn.com.weixunyun.child.model.service.TemplateService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/template")
@Produces(MediaType.APPLICATION_JSON)
@Description("模板")
public class TemplateResource extends AbstractResource {

	@GET
	@Path("{code}")
	public Template select(@PathParam("code") String code, @CookieParam("rsessionid") String rsessionid) {
		return super.getService(TemplateService.class).select(code, super.getAuthedSchool(rsessionid).getId());
	}

	@GET
	@Description("模板列表")
	public List<Template> selectAll(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@CookieParam("rsessionid") String rsessionid) {
		try {
			School school = super.getAuthedSchool(rsessionid);
			TemplateService service = super.getService(TemplateService.class);
			return service.selectAll(page * rows, rows, school.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/count")
	@Description("总数")
	public int selectCount(@CookieParam("rsessionid") String rsessionid) {
		TemplateService service = super.getService(TemplateService.class);
		return service.selectAllCount(super.getAuthedSchool(rsessionid).getId());
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Template Template = super.buildBean(Template.class, formData, null);
		Template.setSchoolId(super.getAuthedSchool(rsessionid).getId());

		TemplateService service = super.getService(TemplateService.class);
		service.insert(Template);
	}

	@PUT
	@Path("{code}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void update(@CookieParam("rsessionid") String rsessionid, MultivaluedMap<String, String> formData,
			@PathParam("code") String code) throws Exception {
		System.out.println("--------------------update------------------");

		Template template = new Template();
		template.setCode(code);
		template.setSchoolId(super.getAuthedSchool(rsessionid).getId());
		template.setDescription(URLDecoder.decode(formData.getFirst("remark"), "UTF-8"));

		TemplateService service = super.getService(TemplateService.class);
		service.update(template);
	}

}
