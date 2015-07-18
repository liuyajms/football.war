package cn.com.weixunyun.child.control;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.bean.TeacherParentsMessage;
import cn.com.weixunyun.child.model.pojo.Message;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.MessageService;
import cn.com.weixunyun.child.model.service.SequenceService;
import cn.com.weixunyun.child.util.sensitive.SensitivewordFilter;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
@Description("消息")
public class MessageResource extends AbstractResource {

	@GET
	@Path("session")
	@Description("回话")
	public List<Map<String, ?>> selectSession(@CookieParam("rsessionid") String rsessionid) {
		Long id = super.getAuthedId(rsessionid);

		MessageService service = super.getService(MessageService.class);
		return service.getSessionList(id);
	}

	@GET
	@Description("列表")
	public List<TeacherParentsMessage> selectAll(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("userId") Long userId, @QueryParam("page") int page, @QueryParam("rows") int rows) {
		Long id0 = super.getAuthedId(rsessionid);
		MessageService service = super.getService(MessageService.class);

		Message message = new Message();
		message.setTimeRead(new Timestamp(System.currentTimeMillis()));
		service.update(message, id0);

		return service.queryList(page * rows, rows, id0, userId);
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Map<String, PartField> map = super.partMulti(request);
		Message messageFrom = new Message();
		Message messageTo = new Message();

		School school = super.getAuthedSchool(rsessionid);
		Long idFrom = super.getService(SequenceService.class).sequence();
		Long idTo = super.getService(SequenceService.class).sequence();

		messageFrom.setId(idFrom);
		messageFrom.setSchoolId(school.getId());
		messageFrom.setSource(true);

		messageTo.setId(idTo);
		messageTo.setSchoolId(school.getId());
		messageTo.setSource(false);

		int type = Integer.parseInt(map.get("type").getValue());
		File file = map.get("description").getFile();
		switch (type) {
		case 0:
//			messageFrom.setDescription(map.get("description").getValue());
			//敏感词过滤
			String des = SensitivewordFilter.replaceSensitiveWord(map.get("description").getValue());
			messageFrom.setDescription(des);
			
//			messageTo.setDescription(map.get("description").getValue());
			messageTo.setDescription(des);
			break;
		case 1:
			FileUtils.copyFile(file, new File(super.getFilePath(), school.getId() + "/message/" + messageFrom.getId()
					+ ".png"));
			FileUtils.copyFile(file, new File(super.getFilePath(), school.getId() + "/message/" + messageTo.getId()
					+ ".png"));
			break;
		case 2:
			FileUtils.copyFile(file, new File(super.getFilePath(), school.getId() + "/message/" + messageFrom.getId()
					+ ".amr"));
			FileUtils.copyFile(file, new File(super.getFilePath(), school.getId() + "/message/" + messageTo.getId()
					+ ".amr"));
			messageFrom.setVoiceLength(Long.parseLong(map.get("voiceLength").getValue()));
			messageTo.setVoiceLength(messageFrom.getVoiceLength());
			break;
		default:
		}

		Long userIdFrom = super.getAuthedId(rsessionid);
		messageFrom.setUserIdFrom(userIdFrom);
		messageTo.setUserIdFrom(userIdFrom);

		PartField userIdTo = map.get("userIdTo");
		if (userIdTo != null) {
			messageFrom.setUserIdTo(Long.parseLong(userIdTo.getValue()));
			messageTo.setUserIdTo(Long.parseLong(userIdTo.getValue()));
		}

		messageFrom.setTimeSend(new Timestamp(System.currentTimeMillis()));
		messageFrom.setTimeRead(new Timestamp(System.currentTimeMillis()));
		messageTo.setTimeSend(new Timestamp(System.currentTimeMillis()));

		messageFrom.setType((long) type);
		messageTo.setType(messageFrom.getType());

		MessageService service = super.getService(MessageService.class);
		service.insertData(messageFrom, messageTo);
		// PushUtils.sendPersonalNotification(messageFrom.getUserIdTo(),
		// messageFrom.getId(), , messageFrom.getDescription());
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(MessageService.class).delete(id);
	}

	@GET
	@Path("unreaded")
	@Description("未读总数")
	public int unreaded(@CookieParam("rsessionid") String rsessionid) {
		MessageService messageService = super.getService(MessageService.class);
		return messageService.unreaded(super.getAuthedId(rsessionid));
	}
}
