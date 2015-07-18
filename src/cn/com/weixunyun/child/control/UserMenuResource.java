package cn.com.weixunyun.child.control;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.pojo.UserMenu;
import cn.com.weixunyun.child.model.service.UserMenuService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/userMenu")
@Produces(MediaType.APPLICATION_JSON)
@Description("菜单")
public class UserMenuResource extends AbstractResource {

	@GET
	@Description("列表")
	public List<UserMenu> selectAll(@CookieParam("rsessionid") String rsessionid) {
		UserMenuService service = super.getService(UserMenuService.class);
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		Long userId = super.getAuthedId(rsessionid);
		return service.selectAll(userId, schoolId);
	}


	@Description("添加UserMenu")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void insert(@CookieParam("rsessionid") String rsessionid, MultivaluedMap<String, String> formData) {

		Long schoolId = super.getAuthedSchoolId(rsessionid);
		Long userId = super.getAuthedId(rsessionid);
		UserMenuService service = super.getService(UserMenuService.class);
		service.inserUserMenu(formData, userId, schoolId);
	}
	


	/**
	 * 删除, 如果menuID值为空，则删除所有符合条件数据；
	 * @param userId
	 * @param schoolId
	 */
	@DELETE
	@Path("{menuId}")
	public void delete(@CookieParam("rsessionid") String rsessionid, @PathParam("menuId") Long menuId) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		Long userId = super.getAuthedId(rsessionid);
		if (menuId !=null && !"".equals(menuId)){
			UserMenuService service = super.getService(UserMenuService.class);
			service.delete(userId, schoolId, menuId);
		} else {
			super.getService(UserMenuService.class).deleteAll(userId, schoolId);
		}
		
	}
	
}
