package cn.com.weixunyun.child.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.model.pojo.RoleMenu;
import cn.com.weixunyun.child.model.service.RoleMenuService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/roleMenu")
@Produces(MediaType.APPLICATION_JSON)
public class RoleMenuResource extends AbstractResource {

	@GET
	public List<RoleMenu> selectAll(@QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("roleId") Long roleId) {
		try {
			RoleMenuService service = super.getService(RoleMenuService.class);

			int total = service.selectAllCount(roleId);
			List<RoleMenu> list = service.selectAll(page*rows, rows, roleId);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", list);

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void insert(MultivaluedMap<String, String> formData) {
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setRoleId(Long.parseLong(formData.getFirst("roleId")));
		roleMenu.setMenuId(Long.parseLong(formData.getFirst("menuId")));

		RoleMenuService service = super.getService(RoleMenuService.class);
		service.insert(roleMenu);
	}

	@DELETE
	public void delete(@QueryParam("roleId") Long roleId,
			@QueryParam("menuId") Long menuId) {
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setRoleId(roleId);
		roleMenu.setMenuId(menuId);

		RoleMenuService service = super.getService(RoleMenuService.class);
		service.delete(roleMenu);
	}
}
