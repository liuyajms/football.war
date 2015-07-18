package cn.com.weixunyun.child.module.point;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;

@Path("/point")
@Produces(MediaType.APPLICATION_JSON)
@Description("积分")
public class PointResource extends AbstractResource {
	
	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("积分兑换")
	public void updatePoint(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid,
			MultivaluedMap<String, String> form) {
		PointService service = super.getService(PointService.class);
		service.exchange(id, form);
	}

}
