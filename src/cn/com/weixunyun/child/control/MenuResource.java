package cn.com.weixunyun.child.control;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.com.weixunyun.child.model.bean.MenuRole;
import cn.com.weixunyun.child.model.pojo.Menu;
import cn.com.weixunyun.child.model.service.MenuService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/menu")
@Produces(MediaType.APPLICATION_JSON)
@Description("菜单")
public class MenuResource extends AbstractResource {

	@GET
	@Description("列表")
	public List<Menu> selectAll(@PathParam("schoolId") Long schoolId) {
		MenuService service = super.getService(MenuService.class);
		return service.selectAll(schoolId);
	}

	@GET
	@Path("0")
	public Map selectMenus(@QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("roleId") Long roleId) {
		try {
			page = page > 0 ? page - 1 : 0;

			MenuService service = super.getService(MenuService.class);

			int total = service.selectAllCount();
			List<MenuRole> newsList = service.selectMenus(page, rows, roleId);
			List<MenuRole> menuList = newsList;
			Map<String, Object> m = new HashMap<String, Object>();
			Map<String, Object> child = new HashMap<String, Object>();
			List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
			List<Map<String, ?>> mList = new ArrayList<Map<String, ?>>();
			for (MenuRole dt : newsList) {
				if (dt.getIdParent() == null) {
					m = new HashMap<String, Object>();
					m.put("id", dt.getId());
					m.put("text", dt.getName());
					m.put("checked", "t".equals(dt.getCheck()) == true ? true : false);
					
					m.put("school_id", dt.getSchoolId()); //新增加字段
					m.put("code", dt.getCode());
					
					children = new ArrayList<Map<String, Object>>();
					for (MenuRole menu : menuList) {
						if (menu.getIdParent() == dt.getId()) {
							child = new HashMap<String, Object>();
							child.put("id", menu.getId());
							child.put("text", menu.getName());
							child.put("checked", "t".equals(menu.getCheck()) == true ? true : false);
							
							child.put("school_id", dt.getSchoolId());
							child.put("code", dt.getCode());
							
							children.add(child);
						}
					}
					
					m.put("children", children);
					mList.add(m);
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", mList);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("{id}")
	public Menu select(@PathParam("id") Long id) {
		return super.getService(MenuService.class).select(id);
	}	
	

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void insert(MultivaluedMap<String, String> formData) {

		Menu menu = super.buildBean(Menu.class, formData, null);

		MenuService service = super.getService(MenuService.class);
		service.insert(menu);
	}
	
	@POST
	@Path("roleMenu/{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void insertRoleMenu(MultivaluedMap<String, String> formData) throws UnsupportedEncodingException {
		MenuService service = super.getService(MenuService.class);
		
		Long roleId = Long.valueOf(formData.getFirst("roleId"));
		String menuList = URLDecoder.decode(formData.getFirst("menuList"), "UTF-8");
		service.insertRoleData(roleId, menuList);
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void update(MultivaluedMap<String, String> formData, @PathParam("id") Long id) {
		Menu menu = super.buildBean(Menu.class, formData, id);

		super.getService(MenuService.class).update(menu);
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") Long id) {
		super.getService(MenuService.class).delete(id);
	}
}
