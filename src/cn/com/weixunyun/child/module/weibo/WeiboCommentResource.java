package cn.com.weixunyun.child.module.weibo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.control.AbstractResource.PartField;
import cn.com.weixunyun.child.util.sensitive.SensitivewordFilter;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/weiboComment")
@Produces(MediaType.APPLICATION_JSON)
@Description("微博评论")
public class WeiboCommentResource extends AbstractResource {

	@GET
	@Description("列表")
	public List<UserWeiboComment> select(@QueryParam("weiboId") Long weiboId, @QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("keyword") String keyword) {
		return super.getService(WeiboCommentService.class).queryComments(page * rows, rows, weiboId, keyword);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectCount(@QueryParam("weiboId") Long weiboId, @QueryParam("keyword") String keyword) {
		return super.getService(WeiboCommentService.class).queryCommentsCount(weiboId, keyword);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public WeiboComment select(@PathParam("id") Long id) {
		return super.getService(WeiboCommentService.class).select(id);

	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> form, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Map<String, PartField> map = super.part(form);
		WeiboComment weiboComment = super.buildBean(WeiboComment.class, map, null);

//		WeiboService weiboService = super.getService(WeiboService.class);
//		Weibo weibo = weiboService.get(weiboComment.getWeiboId(), super.getAuthedId(rsessionid));
		weiboComment.setWeiboId(Long.valueOf(form.getFirst("weiboId")));
		weiboComment.setCreateTime(new Timestamp(System.currentTimeMillis()));
		weiboComment.setCreateUserId(super.getAuthedId(rsessionid));
		weiboComment.setSchoolId(super.getAuthedSchool(rsessionid).getId());

		//敏感词过滤
		weiboComment.setDescription(SensitivewordFilter.replaceSensitiveWord(weiboComment.getDescription()));
		
		WeiboCommentService service = super.getService(WeiboCommentService.class);
		service.insert(weiboComment);

		// 消息推送
		// TODO 自己评价就不发了
		// String authedName = super.getAuthedName(rsessionid);
		//
		// Map<String, Object> notificationMap = new HashMap<String, Object>();
		// notificationMap.put("id", weibo.getId());
		// if (weibo.getCreateUserId() != super.getAuthedId(rsessionid)) {
		// PushUtils.sendPersonalNotification(weibo.getCreateUserId(),
		// weiboComment.getId(), null, authedName
		// + "回复了您的微博", "weibo", notificationMap);
		// }
		//
		// String description = weiboComment.getDescription();
		// List<String> nameList = new ArrayList<String>();
		// String[] ss = description.split(" ");
		// for (String s : ss) {
		// if (s.startsWith("@")) {
		// nameList.add(s.substring(1));
		// }
		// }
		//
		// if (nameList.size() > 0) {
		// UserService userService = super.getService(UserService.class);
		// List<Long> idList = userService.select(weibo.getClassesId(),
		// nameList);
		// for (Long id : idList) {
		// PushUtils.sendPersonalNotification(id, weiboComment.getId(), null,
		// authedName + "在微博留言中提到了您", "weibo",
		// notificationMap);
		// }
		// }
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void udpate(MultivaluedMap<String, String> form, @PathParam("id") Long id) throws Exception {
		WeiboComment weiboComment = super.buildBean(WeiboComment.class, form, id);
		WeiboCommentService service = super.getService(WeiboCommentService.class);
		service.update(weiboComment);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(WeiboCommentService.class).delete(id);
	}
}
