package cn.com.weixunyun.child.module.weibo;

import java.sql.Timestamp;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/weiboFavorit")
@Produces(MediaType.APPLICATION_JSON)
@Description("微博收藏")
public class WeiboFavoritResource extends AbstractResource {

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		WeiboFavorit favorit = super.buildBean(WeiboFavorit.class, formData, null);
		favorit.setUserId(super.getAuthedId(rsessionid));
		favorit.setTime(new Timestamp(System.currentTimeMillis()));
		favorit.setSchoolId(super.getAuthedSchool(rsessionid).getId());

		WeiboFavoritService service = super.getService(WeiboFavoritService.class);
		service.insert(favorit);
	}

	@DELETE
	@Description("删除")
	public void delete(@CookieParam("rsessionid") String rsessionid, @QueryParam("weiboId") Long weiboId) {
		super.getService(WeiboFavoritService.class).delete(weiboId, super.getAuthedId(rsessionid));
	}
}
