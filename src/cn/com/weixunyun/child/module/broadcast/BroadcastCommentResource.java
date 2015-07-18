package cn.com.weixunyun.child.module.broadcast;

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
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.bean.UserBroadcastComment;
import cn.com.weixunyun.child.util.sensitive.SensitivewordFilter;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/broadcastComment")
@Produces(MediaType.APPLICATION_JSON)
@Description("广播评论")
public class BroadcastCommentResource extends AbstractResource {

	@GET
	@Description("列表")
	public List<UserBroadcastComment> select(@QueryParam("broadcastId") Long broadcastId,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @QueryParam("keyword") String keyword) {
		return super.getService(BroadcastCommentService.class).getAllBroadCastComments(page * rows, rows, broadcastId,
				keyword);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectCount(@QueryParam("broadcastId") Long broadcastId, @QueryParam("keyword") String keyword) {
		return super.getService(BroadcastCommentService.class).getAllBroadCastCommentsCount(broadcastId, keyword);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws Exception {

		BroadcastComment comment = super.buildBean(BroadcastComment.class, formData, null);
		comment.setCreateUserId(super.getAuthedId(rsessionid));
		comment.setBroadcastId(Long.valueOf(formData.getFirst("broadcastId")));
		comment.setSchoolId(super.getAuthedSchool(rsessionid).getId());
		
		//敏感词替换
		if(comment.getDescription() != null){
			comment.setDescription(SensitivewordFilter.replaceSensitiveWord(comment.getDescription()));
		}
		
		BroadcastCommentService service = super.getService(BroadcastCommentService.class);
		service.insert(comment);
		// 消息推送
		// BroadcastService broadcastService =
		// super.getService(BroadcastService.class);
		// ClassesBroadcast broadcast =
		// broadcastService.get(comment.getBroadcastId());
		//
		// Map<String, Object> notificationMap = new HashMap<String, Object>();
		// notificationMap.put("id", broadcast.getId());
		// PushUtils.sendClassesNotification(broadcast.getClassesId(),
		// broadcast.getId(), null,
		// super.getAuthedName(rsessionid) + "班级广播 " + broadcast.getTitle() +
		// " 发表了留言", "broadcast",
		// notificationMap);
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void update(MultivaluedMap<String, String> formData, @PathParam("id") Long id) throws Exception {
		BroadcastComment comment = super.buildBean(BroadcastComment.class, formData, id);
		BroadcastCommentService service = super.getService(BroadcastCommentService.class);
		service.update(comment);
	}

	@DELETE
	@Path("{broadcastCommentsId}")
	@Description("删除")
	public void delete(@PathParam("broadcastCommentsId") Long broadcastCommentsId) {
		super.getService(BroadcastCommentService.class).delete(broadcastCommentsId);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public BroadcastComment select(@PathParam("id") Long id) {
		return super.getService(BroadcastCommentService.class).select(id);

	}
}
