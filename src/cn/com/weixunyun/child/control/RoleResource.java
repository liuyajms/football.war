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

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.bean.RolePopedom;
import cn.com.weixunyun.child.model.pojo.Role;
import cn.com.weixunyun.child.model.service.RoleService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/role")
@Produces(MediaType.APPLICATION_JSON)
@Description("角色")
public class RoleResource extends AbstractResource {

	@GET
	@Path("count")
	@Description("总数")
	public int count(@CookieParam("rsessionid") String rsessionid) {
		RoleService service = super.getService(RoleService.class);
		return service.selectAllCount(super.getAuthedSchool(rsessionid).getId());
	}

	@GET
	@Description("列表")
	public List<Role> selectAll(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") Long page,
			@QueryParam("rows") Long rows) {
		RoleService service = super.getService(RoleService.class);
		return service.selectAll(page * rows, rows, super.getAuthedTeacher(rsessionid).getSchoolId());
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Role select(@PathParam("id") Long id, @QueryParam("r") Float r) {
		return super.getService(RoleService.class).select(id);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Role role = super.buildBean(Role.class, formData, null);
		role.setSchoolId(super.getAuthedTeacher(rsessionid).getSchoolId());

		RoleService service = super.getService(RoleService.class);
		service.insert(role);
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void update(@CookieParam("rsessionid") String rsessionid, MultivaluedMap<String, String> formData,
			@PathParam("id") long id) throws Exception {
		Role role = super.buildBean(Role.class, formData, id);
		role.setSchoolId(super.getAuthedTeacher(rsessionid).getSchoolId());

		super.getService(RoleService.class).update(role);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") long id) {
		super.getService(RoleService.class).delete(id);
	}

	@GET
	@Path("{id}/popedom")
	@Description("权限列表")
	public List<RolePopedom> getPopedomList(@PathParam("id") long id) {
		return super.getService(RoleService.class).getPopedomList(id);
	}

	@PUT
	@Path("{id}/popedom")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改权限")
	public void updatePopedom(@PathParam("id") long id, MultivaluedMap<String, String> formData) {
		List<String> popedomIdList = formData.get("popedom_id");
		List<String> actionList = formData.get("action");
		super.getService(RoleService.class).updatePopedom(id, popedomIdList, actionList);
	}
}
