package cn.com.weixunyun.child.control;

import java.util.List;

import javax.ws.rs.Consumes;
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

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.SchoolService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/school")
@Produces(MediaType.APPLICATION_JSON)
@Description("学校")
public class SchoolResource extends AbstractResource {

	@GET
	@Description("列表")
	public List<School> getList(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@QueryParam("countyCode") String countyCode, @QueryParam("keyword") String keyword) {
		SchoolService service = super.getService(SchoolService.class);
		if (countyCode != null) {
			return service.getAllSchools(countyCode);
		} else {
			return service.selectAll(page * rows, rows, keyword);
		}
	}

	@GET
	@Path("count")
	@Description("列表")
	public int getListCount(@QueryParam("countyCode") String countyCode, @QueryParam("keyword") String keyword) {
		SchoolService service = super.getService(SchoolService.class);
		return service.selectAllCount(keyword);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public School select(@PathParam("id") long id) {
		return super.getService(SchoolService.class).select(id);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData) throws Exception {

		School s = super.buildBean(School.class, formData, null);
		if (formData.getFirst("vx") != null && !"".equals(formData.getFirst("vx"))) {
			s.setVx(Double.parseDouble(formData.getFirst("vx")));
		}
		if (formData.getFirst("vy") != null && !"".equals(formData.getFirst("vy"))) {
			s.setVy(Double.parseDouble(formData.getFirst("vy")));
		}
		SchoolService service = super.getService(SchoolService.class);
		
		//service.insert(s);
		service.insertSchool(s);
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void update(MultivaluedMap<String, String> formData, @PathParam("id") long id) throws Exception {

		School s = super.buildBean(School.class, formData, id);
		if (formData.getFirst("vx") != null && !"".equals(formData.getFirst("vx"))) {
			s.setVx(Double.parseDouble(formData.getFirst("vx")));
		}
		if (formData.getFirst("vy") != null && !"".equals(formData.getFirst("vy"))) {
			s.setVy(Double.parseDouble(formData.getFirst("vy")));
		}

		super.getService(SchoolService.class).update(s);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") long id) {
		super.getService(SchoolService.class).delete(id);
	}
}
