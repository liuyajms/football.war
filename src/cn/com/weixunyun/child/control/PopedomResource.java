package cn.com.weixunyun.child.control;

import java.util.List;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.pojo.Popedom;
import cn.com.weixunyun.child.model.service.PopedomService;

@Path("/popedom")
@Produces(MediaType.APPLICATION_JSON)
@Description("权限")
public class PopedomResource extends AbstractResource {

	@GET
	@Description("列表")
	public List<Popedom> selectAll(@CookieParam("rsessionid") String rsessionid) {
		PopedomService service = super.getService(PopedomService.class);
		return service.select();
	}

}
