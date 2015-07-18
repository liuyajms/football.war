package cn.com.weixunyun.child.control;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import cn.com.weixunyun.child.model.pojo.Halt;
import cn.com.weixunyun.child.model.service.HaltService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/halt")
@Produces(MediaType.APPLICATION_JSON)
@Description("停机")
public class HaltResource extends AbstractResource {
	
	@GET
	@Description("列表")
	public List<Halt> getList(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@CookieParam("rsessionid") String rsessionid) {
		System.out.println("--------------list-----------");
		try {
			HaltService service = super.getService(HaltService.class);
			return service.selectAll(page * rows, rows);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("/get")
	@Description("列表")
	public Halt getListAvailable(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@CookieParam("rsessionid") String rsessionid) {
		try {
			HaltService service = super.getService(HaltService.class);
			return service.selectAllAvailable();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("/count")
	@Description("列表")
	public int getListCount(@CookieParam("rsessionid") String rsessionid) {
		try {
			HaltService service = super.getService(HaltService.class);
			return service.selectAllCount();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Halt select(@PathParam("id") long id) {
		return super.getService(HaltService.class).select(id);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid) throws Exception {
		System.out.println("---------------insert---------------");
		System.out.println(formData);
	    String str = formData.getFirst("time");
	    
		Halt s = super.buildBean(Halt.class, formData, null);
		s.setAvailable( formData.getFirst("available") != null);
	    
	    if (str != null && !"".equals(str)) {
			String arr[] = str.split("-");
			Date date = new Date(Integer.valueOf(arr[0])-1900, Integer.valueOf(arr[1])-1, Integer.valueOf(arr[2]));
			s.setTime(date);
		}
		
		HaltService service = super.getService(HaltService.class);
		service.insert(s);
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void update(MultivaluedMap<String, String> formData, @PathParam("id") long id) throws Exception {
		System.out.println("---------------update---------------");
		System.out.println(formData);
		Halt s = super.buildBean(Halt.class, formData, id);
		s.setAvailable( formData.getFirst("available") != null);
		String str = formData.getFirst("time");
		if (str != null && !"".equals(str)) {
			String arr[] = str.split("-");
			Date date = new Date(Integer.valueOf(arr[0])-1900, Integer.valueOf(arr[1])-1, Integer.valueOf(arr[2]));
			System.out.println(date);
			s.setTime(date);
		}
		super.getService(HaltService.class).update(s);
	}
	
	@PUT
	@Path("{id}/readed")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改已读状态")
	public boolean updateRead(@PathParam("id") long id) throws Exception {
		super.getService(HaltService.class).updateReaded(id);
		return true;
	}
	
	@PUT
	@Path("{id}/available")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改可用状态")
	public boolean updateAvailable(@PathParam("id") long id, @QueryParam("available") Boolean available) throws Exception {
		super.getService(HaltService.class).updateAvailable(id, available);
		return true;
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") long id) {
		super.getService(HaltService.class).delete(id);
	}
}
